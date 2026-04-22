// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.service.onerecord;

import com.efreight.base.module.one.record.neone.exception.NeoneException;
import com.efreight.base.module.one.record.neone.exception.StatusChangeDeniedException;
import com.efreight.base.module.one.record.neone.exception.SubjectNotFoundException;
import com.efreight.base.module.one.record.neone.iata.onerecord.API;
import com.efreight.base.module.one.record.neone.model.onerecord.ActionRequest;
import com.efreight.base.module.one.record.neone.model.onerecord.IdProvider;
import com.efreight.base.module.one.record.neone.model.onerecord.RequestStatus;
import com.efreight.base.module.one.record.neone.repository.ActionRequestRepository;
import com.efreight.base.module.one.record.neone.repository.ModelHandler;
import com.efreight.base.module.one.record.neone.repository.RepositoryTransaction;
import com.efreight.base.module.one.record.neone.security.InternalAccessSubject;
import com.google.common.eventbus.EventBus;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.util.Models;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.query.QueryResults;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
@ApplicationScoped
public class ActionRequestService {
    private static final Logger log = LoggerFactory.getLogger(ActionRequestService.class);

    @Value("${action-request.client.url:AAA}")
    private String baseUrl;

    @Value("${action-request.client.connect-timeout:5000}")
    private int connectTimeout;

    @Value("${action-request.client.read-timeout:30000}")
    private int readTimeout;

    @Value("${auto-accept-action-requests:true}")
    private boolean autoAccept;

    private final InternalAccessSubject accessSubject;

    private final EventBus eventBus;

    private final RepositoryTransaction transaction;

    private final List<ActionRequestRepository<?>> actionRequestRepositories;

//    private final RestClientBuilderProducer restClientBuilderProducer;

    private final IdProvider idProvider;

    @Inject
    public ActionRequestService(RepositoryTransaction transaction,
//                                RestClientBuilderProducer restClientBuilderProducer,
                                EventBus eventBus,
                                InternalAccessSubject accessSubject,
                                List<ActionRequestRepository<?>> actionRequestRepositories,
                                IdProvider idProvider
    ) {
        this.transaction = transaction;
//        this.restClientBuilderProducer = restClientBuilderProducer;
        this.accessSubject = accessSubject;
        this.idProvider = idProvider;
        this.eventBus = eventBus;
        this.actionRequestRepositories = actionRequestRepositories;
//
    }

    public Model getActionRequest(IRI actionRequestIri) {
        return transaction.transactionallyGet(connection -> {
            ActionRequestRepository<?> requestRepository = determineRepository(actionRequestIri, connection);
            ActionRequest actionRequest = requestRepository.findGraphByIri(actionRequestIri, connection)
                    .orElseThrow(() -> new SubjectNotFoundException("Subject Not Found  " + actionRequestIri.stringValue()));
            ModelHandler<ActionRequest> modelHandler = requestRepository.getActionRequestModelHandler();
            return modelHandler.fromJava(actionRequest);
        });
    }

    public void revokeActionRequest(IRI requestIri) {
        updateActionRequestStatus(requestIri, RequestStatus.REQUEST_REVOKED);
    }

    public void updateActionRequestStatus(IRI requestIri, RequestStatus newStatus) {
        log.debug("Updating action request for iri [{}] to [{}]", requestIri, newStatus);
        transaction.transactionallyDo((connection, hooks) -> {
            IRI type = getActionRequestType(requestIri, connection);
            ActionRequestRepository<?> repo = determineRepository(requestIri, connection);
            ActionRequest actionRequest = repo.findGraphByIri(requestIri, connection)
                    .orElseThrow(() -> new SubjectNotFoundException("Subject Not Found  " + requestIri.stringValue()));
            if (actionRequest.requestStatus() != RequestStatus.REQUEST_PENDING && type.equals(API.ChangeRequest)) {
                throw new StatusChangeDeniedException(requestIri);
            }
            ActionRequest updatedActionRequest = (newStatus == RequestStatus.REQUEST_REVOKED) ?
                    actionRequest.withRevocation(accessSubject.iri(), Instant.now()) :
                    actionRequest.withStatus(newStatus);
//            hooks.registerPostCommitHook(() -> eventBus.post(type.stringValue(), updatedActionRequest));
            hooks.registerPostCommitHook(() -> eventBus.post(updatedActionRequest));
        });
    }

    public void overwriteActionRequestStatus(IRI requestIri, RequestStatus newStatus) {
        // Just change the status value without all the fuss around usual status updates.
        log.debug("Updating action request status for iri [{}]: [{}]", requestIri, newStatus);
        transaction.transactionallyDo(connection -> {
            ActionRequestRepository<?> repo = determineRepository(requestIri, connection);
            repo.delete(requestIri, API.hasRequestStatus, connection);
            repo.add(requestIri, API.hasRequestStatus, newStatus.iri(), connection);
        });
    }

    private ActionRequestRepository<?> determineRepository(IRI actionRequestIri, RepositoryConnection connection) {
        IRI actionRequestType = getActionRequestType(actionRequestIri, connection);
        return actionRequestRepositories.stream()
                .filter(actionRequestRepository -> actionRequestRepository.getRepositoryType().equals(actionRequestType))
                .findFirst()
                .orElseThrow(() -> new NeoneException("Unknown ActionRequest type [" + actionRequestType + "]"));
    }

    private IRI getActionRequestType(IRI actionRequestIri, RepositoryConnection connection) {
        Model model = QueryResults.asModel(connection.getStatements(actionRequestIri, RDF.TYPE, null));
        if (model.isEmpty()) {
            throw new SubjectNotFoundException("Subject Not Found  " + actionRequestIri.stringValue());
        }
        return Models.objectIRI(model)
                .orElseThrow(() -> new NeoneException("ActionRequest [" + actionRequestIri.stringValue() + "] not found"));
    }

    private IRI getActionRequestType(IRI actionRequestIri, Model model) {
        return Models.objectIRI(model.filter(actionRequestIri, RDF.TYPE, null))
                .orElseThrow(() -> new NeoneException("ActionRequest [" + actionRequestIri.stringValue() + "] not found"));
    }

    /**
     * // TODO
     * 自动更新actionRequest
     * 自动更新subscriptionRequest 为  REQUEST_ACCEPTED
     * 通过发补事件
     */
//    @Scheduled(cron = "0/10 * * * * ?")
    public void evaluatePendingActionRequests() {
        log.info("Fetching all pending action requests.");
        List<IRI> pending = fetchPendingActionRequests();
        pending.forEach(iri ->
                        transaction.transactionallyDo((connection, hooks) -> {
                            ActionRequestRepository<?> repo = determineRepository(iri, connection);
                            ActionRequest actionRequest = repo.findGraphByIri(iri, connection)
                                    .orElseThrow(() -> new NeoneException("Cannot find action request " + iri.stringValue()));
                            log.info("Querying for action request status.");
                            ModelHandler<ActionRequest> handler = repo.getActionRequestModelHandler();
                            hooks.registerPostCommitHook(() -> {
                                RequestStatus status = queryRequestStatus(handler, actionRequest);
                                if (status != RequestStatus.REQUEST_PENDING) {
                                    // Status has changed!
                                    Model actionRequestModel = handler.fromJava(actionRequest);
                                    IRI type = getActionRequestType(iri, actionRequestModel);
                                    //  TODO
//                            eventBus.publish(type.stringValue(), actionRequest.withStatus(status));
                                }
                            });
                        })
        );
    }

    private List<IRI> fetchPendingActionRequests() {
        return transaction.transactionallyGet(connection -> {
                    List<IRI> iriList = new ArrayList<>();
                    actionRequestRepositories.forEach(r -> {
                        IRI type = r.getRepositoryType();
                        List<IRI> iris = r.getAllPending(connection, type);
                        iriList.addAll(iris);
                    });
                    return iriList;
                }
        );
    }

    public RequestStatus queryRequestStatus(ModelHandler handler, ActionRequest actionRequest) {
        Model actionRequestModel = handler.fromJava(actionRequest);
        IRI requestedBy = actionRequest.requestedBy();
//        if (autoAccept && requestedBy.equals(idProvider.getDataHolderId().getIri())) {
        if (autoAccept) {
            log.info("Action request [{}] automatically accepted.", actionRequest.iri());
            return RequestStatus.REQUEST_ACCEPTED;
        } else {
            return requestStatusExternally(actionRequestModel);
        }
    }

    public RequestStatus requestStatusExternally(Model actionRequest) {
        URI uri = URI.create(baseUrl);
//        var client = restClientBuilderProducer.restClientBuilder()
//            .baseUri(uri)
//            .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
//            .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
//            .build(ActionRequestClient.class);
//        var response = client.evaluate(actionRequest);
//        return RequestStatus.valueOf(response.actionRequestStatus());
        return RequestStatus.valueOf(null);
    }
}
