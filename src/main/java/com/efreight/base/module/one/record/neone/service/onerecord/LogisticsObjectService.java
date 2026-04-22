// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.service.onerecord;

import com.efreight.base.module.one.record.neone.controller.onerecord.LogisticsObjectList;
import com.efreight.base.module.one.record.neone.exception.*;
import com.efreight.base.module.one.record.neone.iata.onerecord.ACL;
import com.efreight.base.module.one.record.neone.iata.onerecord.API;
import com.efreight.base.module.one.record.neone.iata.onerecord.CARGO;
import com.efreight.base.module.one.record.neone.iata.onerecord.NEONE;
import com.efreight.base.module.one.record.neone.model.onerecord.*;
import com.efreight.base.module.one.record.neone.repository.*;
import com.efreight.base.module.one.record.neone.security.InternalAccessSubject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.util.Models;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.springframework.stereotype.Component;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Logistics Object Service
 * <p>
 * 使用 CDI 和 Spring 混合注解，支持两种依赖注入框架
 */
@Slf4j
@Component  // Spring 注解：让 Spring Boot 能够识别这个 Bean
@ApplicationScoped  // CDI 注解：支持 CDI 容器
public class LogisticsObjectService {

    @org.springframework.beans.factory.annotation.Value("${authorize-creator:false}")
    boolean authorizeCreator;

    private final LogisticsObjectRepository loRepository;

    private final LogisticsObjectMetadataRepository metadataRepository;

    private final NeoneEventRepository neoneEventRepository;

    private final RepositoryTransaction transaction;

    private final AuditTrailService auditTrailService;

    private final SnapshotService snapshotService;

    private final ActionRequestService actionRequestService;

    private final IdProvider idProvider;

    private final ChangeRequestRepository changeRequestRepository;

    private final InternalAccessSubject accessSubject;

    private final ActionRequestNotificationService notificationService;

    private final AclAuthorizationRepository aclRepository;

    private final NeoneEventService neoneEventService;

    @Inject
    public LogisticsObjectService(LogisticsObjectRepository loRepository,
                                  LogisticsObjectMetadataRepository metadataRepository,
                                  NeoneEventRepository neoneEventRepository,
                                  RepositoryTransaction transaction,
                                  AuditTrailService auditTrailService,
                                  SnapshotService snapshotService,
                                  ActionRequestService actionRequestService,
                                  ChangeRequestRepository changeRequestRepository,
                                  IdProvider idProvider,
                                  InternalAccessSubject accessSubject,
                                  ActionRequestNotificationService notificationService,
                                  AclAuthorizationRepository aclRepository,
                                  NeoneEventService neoneEventService) {
        this.loRepository = loRepository;
        this.metadataRepository = metadataRepository;
        this.neoneEventRepository = neoneEventRepository;
        this.transaction = transaction;
        this.auditTrailService = auditTrailService;
        this.actionRequestService = actionRequestService;
        this.snapshotService = snapshotService;
        this.idProvider = idProvider;
        this.changeRequestRepository = changeRequestRepository;
        this.accessSubject = accessSubject;
        this.notificationService = notificationService;
        this.aclRepository = aclRepository;
        this.neoneEventService = neoneEventService;
    }

    public void createLogisticsObject(LogisticsObject logisticsObject) {
        createLogisticsObjects(LogisticsObjectList.fromLogisticsObject(logisticsObject));
    }

    public void createLogisticsObject(LogisticsObject logisticsObject, boolean publicAccess) {
        createLogisticsObjects(LogisticsObjectList.fromLogisticsObject(logisticsObject), publicAccess);
    }

    public void createLogisticsObject(LogisticsObject logisticsObject, boolean authorizeCreator, boolean publicAccess) {
        createLogisticsObject(LogisticsObjectList.fromLogisticsObject(logisticsObject), authorizeCreator, publicAccess);
    }

    public void createLogisticsObjects(LogisticsObjectList logisticsObjects) {
        createLogisticsObjects(logisticsObjects, false);
    }

    public void createLogisticsObjects(LogisticsObjectList logisticsObjects, boolean publicAccess) {
        createLogisticsObject(logisticsObjects, authorizeCreator, publicAccess);
    }

    public void createLogisticsObject(LogisticsObjectList logisticsObjects, boolean authorizeCreator,
                                      boolean publicAccess) {
        transaction.transactionallyDo((connection, hooks) -> {
            for (LogisticsObject logisticsObject : logisticsObjects.getLogisticsObjects()) {
                Set<IRI> loTypes = getLogisticsObjectTypes(logisticsObject.iri(), logisticsObject.model());
                log.info("Persisting model with root IRI [{}] of type(s) [{}]",
                        logisticsObject.iri().stringValue(),
                        loTypes.stream().map(IRI::stringValue).collect(Collectors.joining(",")));
                boolean hasPredefIri = hasPredefinedIri(logisticsObject);
                if (hasPredefIri) {
                    checkIri(logisticsObject, connection);
                }
                removeHasPredefinedIriPredicate(logisticsObject);
                loRepository.persist(logisticsObject, connection);
                log.info(">>>>>>>>>业务数据 保存完成:{}<<<<<<<<", logisticsObject);
                createAndPersistLoMetadata(logisticsObject.iri(), hasPredefIri, connection);
                snapshotService.createSnapshot(logisticsObject, AuditTrailService.INITIAL_REVISION, connection);
                auditTrailService.createAuditTrail(logisticsObject.iri(), connection);
                NeoneEvent loCreatedEvent = createAndPersistLoCreatedEvent(logisticsObject.iri(), loTypes, connection);
                hooks.registerPostCommitHook(() -> neoneEventService.publishEvent(loCreatedEvent));
                IRI creatorIri = null;
                if (authorizeCreator) {
                    if (accessSubject.isResolvable()) {
                        creatorIri = accessSubject.iri();
                    } else {
                        // Creating a logistics object does not necessarily require authentication.
                        log.warn("Creator not authenticated. Access permission to logistics object [{}] not established!",
                                logisticsObject.iri());
                    }
                }
                aclRepository.grantDefaultAccess(logisticsObject.iri(), creatorIri, publicAccess, connection);
            }
        });
    }

    private boolean hasPredefinedIri(LogisticsObject logisticsObject) {
        // Remove predicate temporarily introduced by LogisticsObjectBodyReader. Information is still part
        // of metadata.
        Optional<org.eclipse.rdf4j.model.Literal> hasPredefIri = Models.objectLiteral(logisticsObject.model().filter(logisticsObject.iri(), NEONE.hasPredefinedIri, null));
        return hasPredefIri.map(org.eclipse.rdf4j.model.Literal::booleanValue).orElse(false);
    }

    private void checkIri(LogisticsObject logisticsObject, RepositoryConnection connection) {
        IRI iri = logisticsObject.iri();
        // Check for proper address path.
        if (!iri.toString().contains(idProvider.getLogisticsObjectBaseIri().toString())) {
            throw new InvalidAddressException(iri, idProvider.getLogisticsObjectBaseIri());
        }

        // Check if IRI is already taken.
        Optional<LogisticsObject> entity = loRepository.findByIri(iri, connection);
        entity.ifPresent(e -> {
            throw new AlreadyExistsException(e.iri().stringValue());
        });
    }

    private void removeHasPredefinedIriPredicate(LogisticsObject logisticsObject) {
        IRI iri = logisticsObject.iri();
        logisticsObject.model().remove(iri, NEONE.hasPredefinedIri, null);
    }

    public Model getAllLogisticsObjects(int limit, int offset, String loType) {
        IRI loTypeIri;
        if (loType == null) {
            loTypeIri = CARGO.LogisticsObject;
        } else {
            loTypeIri = Values.iri(loType);
        }
        return transaction.transactionallyGet(connection ->
                loRepository.getLogisticsObjects(limit, offset, loTypeIri, connection)
        );
    }

    public LogisticsObject getLogisticsObject(URI uri, boolean embedded) {
        log.debug("Fetching LO for uri [{}]", uri);
        IRI iri = Values.iri(uri.toString());
        log.debug("Generated IRI to fetch logistic object: [{}]", iri.toString());

        Optional<LogisticsObject> lo = transaction.transactionallyGet(connection ->
                embedded ?
                        loRepository.findGraphByIri(iri, accessSubject.iri(), connection) :
                        loRepository.findByIri(iri, connection)
        );
        log.warn(">>>>>>>>>>>>>iri.stringValue():{}<<<<<<<<<<<<<<<<<", iri.stringValue());
        return lo.orElseThrow(() -> new SubjectNotFoundException("Subject Not Found  " + iri.stringValue()));
    }

    public Optional<Snapshot> getSnapshot(IRI loIri, Instant at) {
        log.debug("Fetching snapshot of Logistics Object [{}] at [{}]", loIri, at);
        return snapshotService.getSnapshot(loIri, at);
    }

    public Optional<LogisticsObject> getLogisticsObjectSnapshot(IRI loIri, Instant at) {
        return getSnapshot(loIri, at).map(LogisticsObjectService::toLogisticsObject);
    }

    public static LogisticsObject toLogisticsObject(Snapshot snapshot) {
        try (InputStream is = IOUtils.toInputStream(snapshot.payload(), "UTF-8")) {
            Model model = Rio.parse(is, "", RDFFormat.JSONLD);
            return new LogisticsObject(snapshot.loIri(), model);
        } catch (IOException e) {
            throw new NeoneException("Cannot retrieve Logistics Object from snapshot", e);
        }
    }

    public ChangeRequest createChangeRequest(NeoneId loId, Change change) {
        log.debug("Creating Change Request for Logistics Object [{}]", loId.getIri());
        IRI loIri = loId.getIri();
        return transaction.transactionallyGet((connection, hooks) -> {
            LogisticsObject logisticsObject = loRepository.findByIri(loIri, connection)
                    .orElseThrow(() -> new SubjectNotFoundException("Subject Not Found  " + loIri.stringValue()));
            checkRevision(logisticsObject.iri(), change, connection);
            ChangeRequest changeRequest = createAndPersistChangeRequest(change, connection);
            auditTrailService.updateAuditTrail(loIri, changeRequest, connection);
            Set<IRI> loTypes = getLogisticsObjectTypes(loIri, logisticsObject.model());
            NeoneEvent changeRequestPendingEvent = createChangeRequestPendingEvent(loIri, loTypes, connection);
            hooks.registerPostCommitHook(() -> neoneEventService.publishEvent(changeRequestPendingEvent));
            Set<IRI> dataHolderPermissions = new HashSet<>();
            dataHolderPermissions.add(ACL.Read);
            dataHolderPermissions.add(ACL.Write);
            aclRepository.grantAccess(
                    idProvider.getDataHolderId().getIri(), changeRequest.iri(), dataHolderPermissions, connection
            );
            if (authorizeCreator && !idProvider.getDataHolderId().getIri().equals(accessSubject.iri())) {
                Set<IRI> creatorPermissions = new HashSet<>();
                creatorPermissions.add(ACL.Read);
                creatorPermissions.add(ACL.Write);
                aclRepository.grantAccess(
                        accessSubject.iri(), changeRequest.iri(), creatorPermissions, connection
                );
            }
            return changeRequest;
        });
    }

    private Set<IRI> getLogisticsObjectTypes(IRI loIri, Model model) {
        Set<IRI> types = Models.objectIRIs(model.filter(loIri, RDF.TYPE, null));
        if (types.isEmpty()) {
            throw new NeoneException("LogisticsObject [" + loIri.stringValue() + "] not found");
        }
        return types;
    }


    private void checkRevision(IRI loIri, Change change, RepositoryConnection connection) {
        log.debug("Checking revision...");
        int revisionFromRequest = change.revision();
        int revision = metadataRepository.getMetadataOfSubject(loIri, connection)
                .map(LogisticsObjectMetadata::getRevision)
                .orElseThrow(() -> new RevisionMissingException(loIri));
        if (revision != revisionFromRequest) {
            throw new BadRevisionNumberException(revisionFromRequest);
        }
    }

    private ChangeRequest createAndPersistChangeRequest(Change change,
                                                        RepositoryConnection connection) {
        log.debug("Creating and persisting ChangeRequest...");

        NeoneId changeRequestId = idProvider.createActionRequestId();

        ChangeRequest changeRequest = new ChangeRequest(
                changeRequestId.getIri(),
                Optional.empty(),
                RequestStatus.REQUEST_PENDING,
                Instant.now(),
                accessSubject.iri(),
                Optional.empty(),
                Optional.empty(),
                change);

        changeRequestRepository.persist(changeRequest, connection);

        return changeRequest;
    }

    private void createAndPersistLoMetadata(IRI loIri, boolean hasPredefinedIri, RepositoryConnection connection) {
        LogisticsObjectMetadata metadata = new LogisticsObjectMetadata(
                idProvider.createInternalIri().getIri(), loIri, 1, Instant.now(), Optional.of(hasPredefinedIri)
        );
        metadataRepository.persist(metadata, connection);
        log.info(">>>>>>>>>元数据数据 保存完成:{}<<<<<<<<", metadata);
    }

    private NeoneEvent createAndPersistLoCreatedEvent(IRI loIri, Set<IRI> loTypes, RepositoryConnection connection) {
        log.debug("Creating and persisting LogisticsObjectEvent (CREATED)...");
        return neoneEventRepository.addEvent(loIri, API.LOGISTICS_OBJECT_CREATED,
                Optional.empty(), Collections.emptySet(), loTypes, connection);
    }

    private NeoneEvent createChangeRequestPendingEvent(IRI loIri, Set<IRI> loTypes, RepositoryConnection connection) {
        log.debug("Creating and persisting event Change Request Pending...");
        return neoneEventRepository.addEvent(loIri, API.CHANGE_REQUEST_PENDING,
                Optional.empty(), Collections.emptySet(), loTypes, connection);
    }
}
