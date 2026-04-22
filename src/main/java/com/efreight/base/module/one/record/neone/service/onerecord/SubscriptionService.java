// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.service.onerecord;

import com.efreight.base.module.one.record.neone.client.ProposalResponse;
import com.efreight.base.module.one.record.neone.client.RestClientBuilderProducer;
import com.efreight.base.module.one.record.neone.exception.NeoneException;
import com.efreight.base.module.one.record.neone.handler.SubscriptionRequestHandler;
import com.efreight.base.module.one.record.neone.model.onerecord.*;
import com.efreight.base.module.one.record.neone.repository.*;
import com.efreight.base.module.one.record.neone.security.InternalAccessSubject;
import io.smallrye.common.annotation.Blocking;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.util.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

@Component
@ApplicationScoped
public class SubscriptionService {

    private static final Logger log = LoggerFactory.getLogger(SubscriptionService.class);

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionRequestRepository subscriptionRequestRepository;
    private final SubscriptionRequestHandler subscriptionRequestHandler;
    private final SubscriptionMetadataRepository subscriptionMetadataRepository;

    private final RepositoryTransaction transaction;

    private final IdProvider idProvider;
    private final RestClientBuilderProducer restClientBuilderProducer;
    private final ActionRequestService actionRequestService;
    private final ActionRequestNotificationService notificationService;
    private final AclAuthorizationRepository aclAuthorizationRepository;
    private final InternalAccessSubject accessSubject;

    @Value("${authorize-creator:false}")
    boolean authorizeCreator;

    @Value("${auto-accept-action-requests:true}")
    private boolean autoAccept;

    @ConfigProperty(name = "quarkus.rest-client.subscription-client.url", defaultValue = "test")
    String baseUrl;

    @ConfigProperty(name = "quarkus.rest-client.subscription-client.connect-timeout", defaultValue = "1m")
    int connectTimeout;

    @ConfigProperty(name = "quarkus.rest-client.subscription-client.read-timeout", defaultValue = "1m")
    int readTimeout;


    @ConfigProperty(name = "auto-accept-subscription-proposals", defaultValue = "true")
    boolean autoAcceptSubscriptionProposals;

    @ConfigProperty(name = "default-subscription-lifespan", defaultValue = "1h")
    Duration defaultSubscriptionLifespan;

    @Inject
    public SubscriptionService(SubscriptionRepository subscriptionRepository,
                               SubscriptionRequestRepository subscriptionRequestRepository,
                               SubscriptionMetadataRepository subscriptionMetadataRepository,
                               AclAuthorizationRepository aclAuthorizationRepository,
                               RepositoryTransaction transaction,
                               SubscriptionRequestHandler subscriptionRequestHandler,
                               IdProvider idProvider,
                               RestClientBuilderProducer restClientBuilderProducer,
                               ActionRequestNotificationService notificationService,
                               ActionRequestService actionRequestService,
                               InternalAccessSubject accessSubject) {
        this.subscriptionRepository = subscriptionRepository;
        this.subscriptionRequestRepository = subscriptionRequestRepository;
        this.subscriptionMetadataRepository = subscriptionMetadataRepository;
        this.aclAuthorizationRepository = aclAuthorizationRepository;
        this.transaction = transaction;
        this.subscriptionRequestHandler = subscriptionRequestHandler;
        this.idProvider = idProvider;
        this.restClientBuilderProducer = restClientBuilderProducer;
        this.notificationService = notificationService;
        this.actionRequestService = actionRequestService;
        this.accessSubject = accessSubject;
    }

    public SubscriptionRequest handleSubscription(Subscription subscription) {
        try {
            //获取用于发送通知的服务器的基础URL，这个基础URL实际上就是订阅者(subscriber)的URL。
            //获取通知发送到的服务器基本URL，也就是订阅者(subscriber)的URL，并且去掉了路径部分，只保留基础地址。
            URL callbackUrl = new URL(new URL(subscription.subscriber().stringValue()), "/");
            // iri: neone:d8896b5a-60f7-4d19-8345-6b59136ba629
            SubscriptionMetadata metadata = new SubscriptionMetadata(idProvider.createInternalIri().getIri(),
                    subscription.iri(), callbackUrl);
            IRI requestIri = idProvider.createUniqueSubscriptionRequestUri().getIri();
            RequestStatus status = autoAccept ? RequestStatus.REQUEST_ACCEPTED : RequestStatus.REQUEST_PENDING;
            SubscriptionRequest request = new SubscriptionRequest(requestIri, status, Instant.now(),
                    subscription.subscriber(), Optional.empty(), Optional.empty(),
                    Optional.empty(), subscription);

            Model subscriptionRequest = subscriptionRequestHandler.fromJava(request);
            transaction.transactionallyDo(connection -> {
                subscriptionRequestRepository.persist(subscriptionRequest, connection);
                subscriptionMetadataRepository.persist(metadata, connection);
                IRI creatorIri = authorizeCreator ? accessSubject.iri() : null;
                aclAuthorizationRepository.grantDefaultAccess(requestIri, creatorIri, false, connection);
            });
            return request;
        } catch (MalformedURLException e) {
            throw new NeoneException("Malformed URL in Subscription service: " + subscription.subscriber().stringValue(), e);
        }
    }

    //    @ConsumeEvent(ActionRequestType.SUBSCRIPTION_REQUEST)
    @Blocking
    public void onSubscriptionRequestChanged(SubscriptionRequest subscriptionRequest) {
        RequestStatus status = subscriptionRequest.requestStatus();
        log.info("Status of subscriptionRequest [{}] has changed to [{}].",
                subscriptionRequest.iri(), status);
        transaction.transactionallyDo(connection -> {
            // Persist not yet persisted new status:
            actionRequestService.overwriteActionRequestStatus(subscriptionRequest.iri(), status);

            if (status == RequestStatus.REQUEST_ACCEPTED) {
                sendNotification(subscriptionRequest, NotificationEventType.SUBSCRIPTION_REQUEST_ACCEPTED);
            } else if (status == RequestStatus.REQUEST_REJECTED) {
                sendNotification(subscriptionRequest, NotificationEventType.SUBSCRIPTION_REQUEST_REJECTED);
            } else if (status == RequestStatus.REQUEST_REVOKED) {
                Subscription subscription = subscriptionRequest.subscription();
                subscriptionRepository.delete(subscription.iri(), null, connection);
                sendNotification(subscriptionRequest, NotificationEventType.SUBSCRIPTION_REQUEST_REVOKED);
            }
        });
    }

    public Subscription handleSubscriptionProposal(Subscription.TopicType topicType, String topic) {
        ProposalResponse subscriptionRes;
        if (autoAcceptSubscriptionProposals) {
            subscriptionRes = new ProposalResponse(true, false, null,
                    Instant.now().plus(defaultSubscriptionLifespan));
        } else {
            log.info("Forwarding Subscription Proposal topicType={} topic={}", topicType, topic);
            subscriptionRes = forwardProposal(topicType, topic);
        }
        return createProposalResponse(topicType, topic, subscriptionRes);
    }

    /*
     * Ask some external system if it wants to subscribe a given topicType/topic.
     */
    private ProposalResponse forwardProposal(Subscription.TopicType topicType, String topic) {
        URI uri = URI.create(baseUrl);
//        SubscriptionClient client = restClientBuilderProducer.restClientBuilder()
//                .baseUri(uri)
//                .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
//                .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
//                .build(SubscriptionClient.class);
//        return client.handleSubscription(topicType.name(), topic);
        return null;
    }

    private Subscription createProposalResponse(Subscription.TopicType topicType,
                                                String topic,
                                                ProposalResponse proposalResponse) {
        NeoneId subscriber = idProvider.getDataHolderId();
        IRI iri = idProvider.createInternalIri().getIri();
        return new Subscription(iri, subscriber.getIri(), topicType, topic,
                Optional.of("some description"),
                Optional.ofNullable(proposalResponse.expiresAt()),
                Optional.of(proposalResponse.sendLoBody()),
                Optional.ofNullable(new HashSet<>(Collections.singleton(proposalResponse.includeEventType()))), Optional.empty());
    }

    private void sendNotification(SubscriptionRequest actionRequest, NotificationEventType eventType) {
        if (!actionRequest.subscription().notifyRequestStatusChange().orElse(false)) {
            return;
        }
        Subscription subscription = actionRequest.subscription();
        IRI lo = subscription.topic().equals(Subscription.TopicType.LOGISTICS_OBJECT_TYPE.toString()) ?
                Values.iri(actionRequest.subscription().topic()) : null;
        Notification notification = new Notification(
                idProvider.createInternalIri().getIri(),
                eventType.iri(),
                Optional.ofNullable(subscription.topic()),
                Optional.ofNullable(lo),
                Optional.ofNullable(actionRequest.requestedBy()),
                Collections.emptySet());
        notificationService.notifyRequestor(actionRequest, notification);
    }
}
