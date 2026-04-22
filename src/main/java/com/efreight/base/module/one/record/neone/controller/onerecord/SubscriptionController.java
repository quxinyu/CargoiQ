// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.controller.onerecord;

import com.efreight.base.module.one.record.neone.annotations.Authenticated;
import com.efreight.base.module.one.record.neone.exception.InvalidApiRequestException;
import com.efreight.base.module.one.record.neone.handler.SubscriptionHandler;
import com.efreight.base.module.one.record.neone.model.entity.NeOneSubscriptions;
import com.efreight.base.module.one.record.neone.model.onerecord.Subscription;
import com.efreight.base.module.one.record.neone.model.onerecord.SubscriptionRequest;
import com.efreight.base.module.one.record.neone.service.SubscriptionsService;
import com.efreight.base.module.one.record.neone.service.onerecord.SubscriptionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.StringWriter;
import java.net.URI;
import java.util.Set;

/**
 * Public endpoint to ...
 * 1) ... receive subscriptions ("Pub/Sub initiated by the Subscriber")
 * 2) ... answer subscription proposal requests sent by another 1R server (Pub/Sub initiated by the Publisher).
 */
@Slf4j
@Authenticated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/" + SubscriptionController.RESOURCE_NAME,
        consumes = {"application/ld+json", "application/x-turtle", "text/turtle"},
        produces = {"application/ld+json"})
public class SubscriptionController {

    public static final String RESOURCE_NAME = "v2/subscriptions";
    private final SubscriptionService subscriptionService;
    private final ObjectMapper objectMapper;
    private final SubscriptionHandler subscriptionHandler;
    private final SubscriptionsService neOneSubscriptionsService;

    /**
     * Subscriber initiated Pub/Sub, i.e. we receive a subscription. It will be wrapped in a SubscriptionRequest object
     * and enqueued for asynchronous processing where the decision is made to accept or to deny the request.
     *
     * @return HTTP 201 Created with Location header pointing to the created subscription
     */
    @PostMapping
    public ResponseEntity<Void> handleSubscription(@RequestBody Subscription subscription) {
        log.info("Receiving subscription request [{}]...", subscription);
        SubscriptionRequest subscriptionRequest = subscriptionService.handleSubscription(subscription);
        NeOneSubscriptions neOneSubscriptions = new NeOneSubscriptions();
        neOneSubscriptions.setActionRequestIri(subscriptionRequest.iri().stringValue());
        neOneSubscriptions.setSubType(subscription.topicType().name());
        if (StringUtils.isNotBlank(subscription.topic())){
            String topic = subscription.topic();
            String[] split = topic.split("#");
            neOneSubscriptions.setSubTypeLoType(split[1]);
        }
        neOneSubscriptions.setCallbackHost(subscription.subscriber().getNamespace());
        neOneSubscriptions.setTopic(subscription.topic());
        neOneSubscriptions.setSubscriptionCompanyUrl(subscription.subscriber().stringValue());
        neOneSubscriptions.setSubVersion("v2");
        // 将 Subscription 对象序列化为 JSON-LD 格式字符串
        try {
            String subscriptionJsonLd = serializeSubscriptionToJsonLd(subscription);
            neOneSubscriptions.setSubscriptionJsonBody(subscriptionJsonLd);
        } catch (Exception e) {
            log.error("Failed to serialize subscription to JSON-LD", e);
        }
        // 处理 includeSubscriptionEventType 字段，转换为 JSON 数组格式
        if (subscription.includeSubscriptionEventType().isPresent()) {
            Set<String> eventTypes = subscription.includeSubscriptionEventType().get();
            try {
                // 将完整 IRI 转换为短名称格式 (api:LOGISTICS_OBJECT_UPDATED)
                String[] shortNames = eventTypes.stream()
                        .map(this::convertToShortName)
                        .toArray(String[]::new);
                String eventTypesJson = objectMapper.writeValueAsString(shortNames);
                neOneSubscriptions.setIncludeSubscriptionType(eventTypesJson);
            } catch (Exception e) {
                log.error("Failed to serialize includeSubscriptionEventType", e);
            }
        }
        neOneSubscriptionsService.save(neOneSubscriptions);
        return ResponseEntity.created(URI.create(subscriptionRequest.iri().stringValue())).build();
    }

    /**
     * Publisher initiated Pub/Sub, i.e. we are invited to subscribe and have to reply with a
     * corresponding Subscription object.
     *
     * @param topicType either LOGISTICS_OBJECT_TYPE or LOGISTICS_OBJECT_URI
     * @param topic     either a specific LO type or LO iri, depending on topicType
     * @return Subscription
     */
    @GetMapping
    @Authenticated
    public Subscription handleSubscriptionProposal(@RequestParam(value = "topicType", required = false) String topicType,
                                                   @RequestParam(value = "topic", required = false) String topic) {
        log.info("Receiving subscription proposal for topicType [{}] and topic [{}]...",
                topicType, topic);

        if (StringUtils.isBlank(topicType)) {
            throw new InvalidApiRequestException("Missing query parameter 'topicType' or 'topic'.");
        }

        Subscription.TopicType tt = Subscription.TopicType.from(Values.iri(topicType));
        Subscription subscription = subscriptionService.handleSubscriptionProposal(tt, topic);
        log.info("Proposal response: [{}]", subscription);
        return subscription;
    }

    /**
     * 将 Subscription 对象序列化为 JSON-LD 格式字符串
     *
     * @param subscription Subscription 对象
     * @return JSON-LD 格式字符串
     */
    private String serializeSubscriptionToJsonLd(Subscription subscription) {
        try {
            // 使用 SubscriptionHandler 将 Subscription 转换为 RDF Model
            Model model = subscriptionHandler.fromJava(subscription);
            // 将 RDF Model 写入 JSON-LD 格式
            StringWriter writer = new StringWriter();
            Rio.write(model, writer, RDFFormat.JSONLD);
            return writer.toString();
        } catch (Exception e) {
            log.error("Failed to serialize subscription to JSON-LD", e);
            throw new RuntimeException("Failed to serialize subscription to JSON-LD", e);
        }
    }

    /**
     * 将完整 IRI 转换为短名称格式
     * 例如: https://onerecord.iata.org/ns/api#LOGISTICS_OBJECT_UPDATED -> api:LOGISTICS_OBJECT_UPDATED
     *
     * @param iri 完整的 IRI
     * @return 短名称格式
     */
    private String convertToShortName(String iri) {
        if (StringUtils.isBlank(iri)) {
            return iri;
        }
        // 提取命名空间和本地名称
        String apiNamespace = "https://onerecord.iata.org/ns/api#";
        String neoneNamespace = "https://www.openlogisticsfoundation.org/neone#";

        if (iri.startsWith(apiNamespace)) {
            return "api:" + iri.substring(apiNamespace.length());
        } else if (iri.startsWith(neoneNamespace)) {
            return "neone:" + iri.substring(neoneNamespace.length());
        }
        // 如果不是已知的命名空间，直接返回原值
        return iri;
    }
}
