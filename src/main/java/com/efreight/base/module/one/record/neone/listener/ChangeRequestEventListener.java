//  Copyright Open Logistics Foundation
//
//  Licensed under the Open Logistics Foundation License 1.3.
//  For details on the licensing terms, see the LICENSE file.
//  SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.listener;

import com.efreight.base.common.core.utils.SpringUtils;
import com.efreight.base.module.one.record.neone.exception.BadRevisionNumberException;
import com.efreight.base.module.one.record.neone.exception.NeoneException;
import com.efreight.base.module.one.record.neone.exception.SubjectNotFoundException;
import com.efreight.base.module.one.record.neone.iata.onerecord.API;
import com.efreight.base.module.one.record.neone.iata.onerecord.NEONE;
import com.efreight.base.module.one.record.neone.model.onerecord.*;
import com.efreight.base.module.one.record.neone.repository.LogisticsObjectMetadataRepository;
import com.efreight.base.module.one.record.neone.repository.LogisticsObjectRepository;
import com.efreight.base.module.one.record.neone.repository.NeoneEventRepository;
import com.efreight.base.module.one.record.neone.repository.RepositoryTransaction;
import com.efreight.base.module.one.record.neone.service.onerecord.*;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * 变更通知事件监听器
 * 监听 ChangeRequest
 *
 * @author quxinyu
 * @since 2025-01-22
 */
@Slf4j
@Component
public class ChangeRequestEventListener {

    private final LogisticsObjectRepository loRepository;

    private final ActionRequestService actionRequestService;

    private final AuditTrailService auditTrailService;

    private final NeoneEventRepository neoneEventRepository;

    private final LogisticsObjectMetadataRepository metadataRepository;

    private final SnapshotService snapshotService;

    private final IdProvider idProvider;

    private final ActionRequestNotificationService notificationService;

    private final RepositoryTransaction transaction;


    /**
     * 构造函数
     *
     * @param loRepository
     * @param actionRequestService
     * @param auditTrailService
     * @param neoneEventRepository
     * @param metadataRepository
     * @param snapshotService
     * @param idProvider
     * @param notificationService
     */
    public ChangeRequestEventListener(LogisticsObjectRepository loRepository,
                                      ActionRequestService actionRequestService,
                                      AuditTrailService auditTrailService,
                                      NeoneEventRepository neoneEventRepository,
                                      LogisticsObjectMetadataRepository metadataRepository,
                                      SnapshotService snapshotService,
                                      IdProvider idProvider,
                                      ActionRequestNotificationService notificationService,
                                      RepositoryTransaction transaction) {
        this.loRepository = loRepository;
        this.actionRequestService = actionRequestService;
        this.auditTrailService = auditTrailService;
        this.neoneEventRepository = neoneEventRepository;
        this.metadataRepository = metadataRepository;
        this.snapshotService = snapshotService;
        this.idProvider = idProvider;
        this.notificationService = notificationService;
        this.transaction = transaction;
    }

    /**
     * 监听 Neone 事件并处理通知
     *
     * @param changeRequest changeRequest 事件对象
     */
    @Subscribe
    public void onChangeRequestUpdated(ChangeRequest changeRequest) {
        RequestStatus status = changeRequest.requestStatus();
        IRI loIri = changeRequest.change().hasLogisticsObject();

        transaction.transactionallyDo((connection, hooks) -> {
            Set<IRI> loTypes = loRepository.getLogisticsObjectTypes(loIri, connection);
            actionRequestService.overwriteActionRequestStatus(changeRequest.iri(), status);
            if (status.equals(RequestStatus.REQUEST_REJECTED)) {
                rejectChangeRequest(changeRequest, loIri, loTypes, connection, hooks);
            } else if (status.equals(RequestStatus.REQUEST_ACCEPTED)) {
                acceptChangeRequest(changeRequest, loIri, loTypes, connection, hooks);
            } else if (status.equals(RequestStatus.REQUEST_REVOKED)) {
                revokeChangeRequest(changeRequest);
            }
        });
    }

    private void rejectChangeRequest(ChangeRequest cr, IRI loIri, Set<IRI> loTypes, RepositoryConnection connection,
                                     RepositoryTransaction.RepositoryTransactionHooks hooks) {

        auditTrailService.updateAuditTrail(loIri, cr, connection);
        NeoneEvent changeRequestRejectedEvent = createChangeRequestRejectedEvent(loIri, loTypes, connection);
        NeoneEventService neoneEventService = SpringUtils.getBean("neoneEventService");
        hooks.registerPostCommitHook(() -> neoneEventService.publishEvent(changeRequestRejectedEvent));
        sendNotification(cr, NotificationEventType.CHANGE_REQUEST_REJECTED);
    }

    private void acceptChangeRequest(ChangeRequest cr, IRI loIri, Set<IRI> loTypes, RepositoryConnection connection,
                                     RepositoryTransaction.RepositoryTransactionHooks hooks) {
        try {
            Change change = cr.change();
            // check revision
            int currentRevision = metadataRepository.getRevision(loIri, connection);
            if (change.revision() != currentRevision) {
                throw new BadRevisionNumberException(change.revision());
            }
            Set<IRI> changedProperties = applyOperations(change, connection);
            int revision = increaseRevision(loIri, change.revision(), connection);
            createSnapshot(loIri, revision, connection);
            auditTrailService.updateAuditTrail(loIri, cr, connection);
            NeoneEventService neoneEventService = SpringUtils.getBean("neoneEventService");
            NeoneEvent changeRequestAcceptedEvent = createChangeRequestAcceptedEvent(loIri, loTypes, connection);
            hooks.registerPostCommitHook(() -> neoneEventService.publishEvent(changeRequestAcceptedEvent));
            NeoneEvent loUpdatedEvent = createAndPersistLoUpdatedEvent(loIri, changedProperties, cr.iri(), loTypes, connection);
            hooks.registerPostCommitHook(() -> neoneEventService.publishEvent(loUpdatedEvent));
            sendNotification(cr, NotificationEventType.CHANGE_REQUEST_ACCEPTED);
        } catch (RuntimeException ex) {
            hooks.registerPostRollbackHook(() -> {
                actionRequestService.overwriteActionRequestStatus(cr.iri(), RequestStatus.REQUEST_FAILED);
                sendNotification(cr, NotificationEventType.CHANGE_REQUEST_FAILED);
            });
            throw ex;
        }
    }

    private NeoneEvent createChangeRequestRejectedEvent(IRI loIri, Set<IRI> loTypes, RepositoryConnection connection) {
        log.debug("Creating and persisting event Change Request rejected...");
        return neoneEventRepository.addEvent(loIri, API.CHANGE_REQUEST_REJECTED,
                Optional.empty(), Collections.emptySet(), loTypes, connection);
    }

    private void sendNotification(ChangeRequest cr, NotificationEventType eventType) {
        Notification notification = new Notification(
                idProvider.createInternalIri().getIri(),
                eventType.iri(),
                Optional.of(Subscription.TopicType.LOGISTICS_OBJECT_TYPE.getValue().stringValue()),
                Optional.ofNullable(cr.change().hasLogisticsObject()),
                Optional.ofNullable(cr.requestedBy()),
                Collections.emptySet());
        if (cr.change().notifyRequestStatusChange().orElse(false)) {
            notificationService.notifyRequestor(cr, notification);
        }
    }

    private Set<IRI> applyOperations(Change change, RepositoryConnection connection) {
        log.debug("Applying operations...");
        Set<IRI> changedProperties = new HashSet<>();
        Set<Resource> affectedSubjects = new HashSet<>();
        Supplier<Stream<Operation>> deletes = () -> change.operations().stream()
                .filter(operation -> operation.op() == PatchOperation.DELETE);
        // assemble triples to delete
        deletes.get().forEach(operation -> {
            changedProperties.add(operation.p());
            // get the subject
            Resource subject = Values.iri(operation.s());
            affectedSubjects.add(subject);
//             either the datatype id a core XSD datatype or an IRI
//            CoreDatatype literalDatatype = CoreDatatype.from(Values.iri(operation.o().datatype()));
//            Value object;
//            if (literalDatatype.isXSDDatatype()) {
//                object = Values.literal(operation.o().value(), literalDatatype);
//            } else {
//                object = Values.iri(operation.o().value());
//            }
//            // delete the triple
//            int deletedTriples = loRepository.delete(subject, operation.p(), object, connection);
//
//            // if not exactly one triple is deleted throw an exception, this cancels the transaction and
//            // nothing is changed
//            if (deletedTriples != 1) {
//                throw new NeoneException("Number of affected triples should be 1 but was " + deletedTriples);
//            }
        });
        // now to the add operations
        Supplier<Stream<Operation>> adds = () -> change.operations().stream()
                .filter(operation -> operation.op() == PatchOperation.ADD);
        // assign internal IDs for non literal values (embedded objects)
//        Map<String, IRI> bnodeToIri = adds.get()
//                .filter(operation -> !CoreDatatype.from(Values.iri(operation.o().datatype())).isXSDDatatype())
//                .collect(Collectors.toMap(
//                        operation -> operation.o().value(),
//                        s -> idProvider.createInternalIri().getIri(),
//                        (iri, iri2) -> iri
//                ));
        adds.get().forEach(operation -> {
            changedProperties.add(operation.p());
            // either the subject is an IRI or a blank node that is mapped to an internal IRI
//            IRI subject = bnodeToIri.computeIfAbsent(operation.s(), s -> Values.iri(operation.s()));
//            CoreDatatype literalDatatype = CoreDatatype.from(Values.iri(operation.o().datatype()));
            Value object;
//            if (literalDatatype.isXSDDatatype()) {
//                // a literal is added
//                object = Values.literal(operation.o().value(), literalDatatype);
//            } else {
//                IRI objectIRI;
//                //check if the object is an embedded object by finding another add operation with the object as subject
//                if (adds.get().anyMatch(op -> op.s().equals(operation.o().value()))) {
//                    // calculate the internal IRI of the "embedded object"
//                    objectIRI = bnodeToIri.computeIfAbsent(
//                            operation.o().value(), s -> Values.iri(operation.o().value())
//                    );
//                    // if an "embedded" object is to be added, add type statement using operations datatype value
//                    loRepository.add(objectIRI, RDF.TYPE, Values.iri(operation.o().datatype()), connection);
//                } else {
//                    // an IRI, i.e. a reference to another resource is to be added
//                    objectIRI = Values.iri(operation.o().value());
//                }
//                object = objectIRI;
//            }
//            loRepository.add(subject, operation.p(), object, connection);
        });
        return changedProperties;
    }

    private int increaseRevision(IRI loIri, int previousRevision, RepositoryConnection connection) {
        int newRevision = previousRevision + 1;
        log.debug("Increasing revision for LO [{}] to [{}])...", loIri, newRevision);
        Metadata metadata = metadataRepository.getMetadataOfSubject(loIri, connection)
                .orElseThrow(() -> new SubjectNotFoundException("Subject Not Found  " + loIri.stringValue()));
        metadataRepository.delete(metadata.iri(), NEONE.hasRevision, connection);
        metadataRepository.add(metadata.iri(), NEONE.hasRevision, Values.literal(newRevision), connection);
        return newRevision;
    }

    private void createSnapshot(IRI loIri, int revision, RepositoryConnection connection) {
        log.debug("Creating snapshot of LO [{}]...", loIri);
        // Ensure to have the newest LO at hand, in full detail!
        LogisticsObject lo = loRepository.findByIri(loIri, connection)
                .orElseThrow(() -> new NeoneException("Cannot find logistics object " + loIri));
        snapshotService.createSnapshot(lo, revision, connection);
    }

    private NeoneEvent createChangeRequestAcceptedEvent(IRI loIri, Set<IRI> loTypes, RepositoryConnection connection) {
        log.debug("Creating and persisting event Change Request accepted...");
        return neoneEventRepository.addEvent(loIri, API.CHANGE_REQUEST_ACCEPTED,
                Optional.empty(), Collections.emptySet(), loTypes, connection);
    }

    private NeoneEvent createAndPersistLoUpdatedEvent(IRI loIri, Set<IRI> changedProperties,
                                                      IRI changeRequestIri, Set<IRI> loTypes,
                                                      RepositoryConnection connection) {
        log.debug("Creating and persisting LogisticsObjectEvent (UPDATED)...");
        return neoneEventRepository.addEvent(loIri, API.LOGISTICS_OBJECT_UPDATED,
                Optional.of(changeRequestIri), changedProperties, loTypes, connection
        );
    }

    private void revokeChangeRequest(ChangeRequest cr) {
        sendNotification(cr, NotificationEventType.CHANGE_REQUEST_REVOKED);
    }

}
