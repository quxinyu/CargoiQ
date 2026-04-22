//  Copyright Open Logistics Foundation
//
//  Licensed under the Open Logistics Foundation License 1.3.
//  For details on the licensing terms, see the LICENSE file.
//  SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.service.onerecord;

import com.efreight.base.module.one.record.neone.exception.InvalidAccessDelegationObjectException;
import com.efreight.base.module.one.record.neone.handler.AccessDelegationRequestHandler;
import com.efreight.base.module.one.record.neone.model.onerecord.*;
import com.efreight.base.module.one.record.neone.repository.AccessDelegationRequestRepository;
import com.efreight.base.module.one.record.neone.repository.AclAuthorizationRepository;
import com.efreight.base.module.one.record.neone.repository.LogisticsObjectRepository;
import com.efreight.base.module.one.record.neone.repository.RepositoryTransaction;
import com.efreight.base.module.one.record.neone.security.InternalAccessSubject;
import org.apache.commons.math3.util.Pair;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class AccessDelegationService {

    private static final Logger log = LoggerFactory.getLogger(AccessDelegationService.class);

    @Value("${authorize-creator:false}")
    boolean authorizeCreator;

    private final AccessDelegationRequestHandler accessDelegationRequestHandler;
    private final AccessDelegationRequestRepository accessDelegationRequestRepository;
    private final AclAuthorizationRepository aclAuthorizationRepository;
    private final LogisticsObjectRepository logisticsObjectRepository;
    private final RepositoryTransaction transaction;
    private final ActionRequestService actionRequestService;
    private final IdProvider idProvider;
    private final InternalAccessSubject accessSubject;
    private final ActionRequestNotificationService notificationService;

    public AccessDelegationService(AccessDelegationRequestHandler accessDelegationRequestHandler,
                                   AccessDelegationRequestRepository accessDelegationRequestRepository,
                                   LogisticsObjectRepository logisticsObjectRepository,
                                   AclAuthorizationRepository aclAuthorizationRepository,
                                   RepositoryTransaction transaction,
                                   ActionRequestService actionRequestService,
                                   IdProvider idProvider,
                                   InternalAccessSubject accessSubject,
                                   ActionRequestNotificationService notificationService) {
        this.accessDelegationRequestHandler = accessDelegationRequestHandler;
        this.accessDelegationRequestRepository = accessDelegationRequestRepository;
        this.logisticsObjectRepository = logisticsObjectRepository;
        this.aclAuthorizationRepository = aclAuthorizationRepository;
        this.transaction = transaction;
        this.actionRequestService = actionRequestService;
        this.idProvider = idProvider;
        this.accessSubject = accessSubject;
        this.notificationService = notificationService;
    }

    public AccessDelegationRequest handleAccessDelegation(AccessDelegation accessDelegation) {
        checkAccessDelegation(accessDelegation);
        IRI requestIri = idProvider.createUniqueAccessDelegationRequestUri().getIri();
        IRI requestedBy = accessSubject.iri();
        AccessDelegationRequest request = new AccessDelegationRequest(requestIri, Optional.empty(),
                RequestStatus.REQUEST_PENDING, Instant.now(),
                requestedBy, Optional.empty(), Optional.empty(), accessDelegation);

        Model accessDelegationRequest = accessDelegationRequestHandler.fromJava(request);
        transaction.transactionallyDo(connection -> {
            accessDelegationRequestRepository.persist(accessDelegationRequest, connection);
            IRI creatorIri = authorizeCreator ? requestedBy : null;
            aclAuthorizationRepository.grantDefaultAccess(requestIri, creatorIri, false, connection);
        });
        return request;
    }

    private void checkAccessDelegation(AccessDelegation accessDelegation) {
        // Check if referenced LOs exist.
        HashSet<IRI> logisticsObjects = new HashSet<>(accessDelegation.getHasLogisticsObject());
        transaction.transactionallyDo(conn -> {
            List<IRI> existing = logisticsObjects.stream().filter(iri ->
                    logisticsObjectRepository.exists(iri, conn)).collect(Collectors.toList());
            logisticsObjects.removeAll(existing);
            if (!logisticsObjects.isEmpty()) {
                throw new InvalidAccessDelegationObjectException(logisticsObjects);
            }
        });
    }

    //    @ConsumeEvent(ActionRequestType.ACCESS_DELEGATION_REQUEST)
//    @Blocking
    public void onAccessDelegationRequestChanged(AccessDelegationRequest accessDelegationRequest) {
        RequestStatus status = accessDelegationRequest.requestStatus();
        log.info("Status of accessDelegationRequest [{}] has changed to [{}].",
                accessDelegationRequest.iri(), status);
        transaction.transactionallyDo(connection -> {
            actionRequestService.overwriteActionRequestStatus(accessDelegationRequest.iri(), status);
            if (status.equals(RequestStatus.REQUEST_ACCEPTED)) {
                try {
                    addToAcl(accessDelegationRequest.getAccessDelegation(), connection);
                    sendNotification(accessDelegationRequest, NotificationEventType.ACCESS_DELEGATION_REQUEST_ACCEPTED);
                } catch (RuntimeException ex) {
                    actionRequestService.overwriteActionRequestStatus(accessDelegationRequest.iri(),
                            RequestStatus.REQUEST_FAILED);
                    sendNotification(accessDelegationRequest, NotificationEventType.ACCESS_DELEGATION_REQUEST_FAILED);
                    throw ex;
                }
            } else if (status.equals(RequestStatus.REQUEST_REJECTED)) {
                sendNotification(accessDelegationRequest, NotificationEventType.ACCESS_DELEGATION_REQUEST_REJECTED);
            } else if (status.equals(RequestStatus.REQUEST_REVOKED)) {
                revokeAccessDelegationRequest(accessDelegationRequest, connection);
                sendNotification(accessDelegationRequest, NotificationEventType.ACCESS_DELEGATION_REQUEST_REVOKED);
            }
        });
    }

    private void addToAcl(AccessDelegation delegation, RepositoryConnection connection) {
        Set<IRI> targets = delegation.getHasLogisticsObject();
        Set<IRI> agents = delegation.getIsRequestedFor();
        Set<IRI> modes = delegation.getPermissions().stream().map(AccessDelegation.Permission::mode)
                .collect(Collectors.toSet());

        cartesianProductOf(targets, agents).forEach(pair -> {
            IRI aclIri = idProvider.createAclAuthorizationId().getIri();
            AclAuthorization aclAuth = new AclAuthorization(aclIri, pair.getFirst(), pair.getSecond(), modes);
            aclAuthorizationRepository.persist(aclAuth, connection);
        });
    }

    private List<Pair<IRI, IRI>> cartesianProductOf(Set<IRI> targets, Set<IRI> agents) {
        // create set of any combination of (target, agent)
        return targets.stream()
                .flatMap(targetIri -> agents.stream()
                        .map(agentIri -> new Pair<>(targetIri, agentIri)))
                .collect(Collectors.toList());
    }

    private void revokeAccessDelegationRequest(AccessDelegationRequest r, RepositoryConnection connection) {
        Set<IRI> agents = r.getAccessDelegation().getIsRequestedFor();
        Set<IRI> targets = r.getAccessDelegation().getHasLogisticsObject();
        cartesianProductOf(agents, targets).forEach(pair ->
                revokeDelegatedAccess(pair.getFirst(), pair.getSecond(), connection));
    }

    private void revokeDelegatedAccess(IRI agent, IRI accessTo, RepositoryConnection connection) {
        List<AclAuthorization> acls = aclAuthorizationRepository.findAclAuthorizations(Optional.of(agent),
                Optional.of(accessTo), connection);
        // Look for transitively delegated access:
        Optional<IRI> requestedFor = accessDelegationRequestRepository.findRequestedFor(agent, accessTo, connection);
        acls.forEach(acl -> aclAuthorizationRepository.deleteAll(acl.iri(), connection));
        requestedFor.ifPresent(iri -> revokeDelegatedAccess(iri, accessTo, connection));
    }

    private void sendNotification(AccessDelegationRequest actionRequest, NotificationEventType eventType) {
        if (!actionRequest.getAccessDelegation().getNotifyRequestStatusChange().orElse(false)) {
            return;
        }

        Notification notification = new Notification(
                idProvider.createInternalIri().getIri(),
                eventType.iri(),
                Optional.of(Subscription.TopicType.LOGISTICS_OBJECT_TYPE.getValue().stringValue()),
                Optional.empty(),
                Optional.of(actionRequest.requestedBy()),
                Collections.emptySet());
        notificationService.notifyRequestor(actionRequest, notification);
    }
}
