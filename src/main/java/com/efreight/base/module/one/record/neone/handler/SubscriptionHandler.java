//  Copyright Open Logistics Foundation
//
//  Licensed under the Open Logistics Foundation License 1.3.
//  For details on the licensing terms, see the LICENSE file.
//  SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.handler;

import com.efreight.base.module.one.record.neone.iata.onerecord.API;
import com.efreight.base.module.one.record.neone.model.onerecord.Subscription;
import com.efreight.base.module.one.record.neone.repository.ModelHandler;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.XMLSchema;
import org.springframework.stereotype.Component;

import javax.enterprise.context.ApplicationScoped;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;

@Component
@ApplicationScoped
public class SubscriptionHandler extends ModelHandler<Subscription> {

    @Override
    public Subscription fromModel(IRI iri, Model model) {
        IRI subscriber = getObject(null, API.hasSubscriber, model);
        String topic = getObjectUri(null, API.hasTopic, model);
        IRI typeIri = getObject(null, API.hasTopicType, model);
        Subscription.TopicType topicType = Subscription.TopicType.from(typeIri);
        Optional<String> description = findObjectString(null, API.hasDescription, model);
        Optional<Instant> expiresAt = findObjectString(null, API.expiresAt, model)
                .map(Instant::parse);
        Set<IRI> eventTypeIris = findObjects(null, API.includeSubscriptionEventType, model);
        Optional<Set<String>> includeSubscriptionEventType = eventTypeIris.isEmpty()
                ? Optional.empty()
                : Optional.of(eventTypeIris.stream().map(IRI::stringValue).collect(java.util.stream.Collectors.toSet()));
        Optional<Boolean> sendLoBody = findObjectString(null, API.sendLogisticsObjectBody, model)
                .map(Boolean::valueOf);
        Optional<Boolean> notifyRequestStatusChange = findObjectString(null, API.notifyRequestStatusChange, model)
                .map(Boolean::valueOf);

        return new Subscription(iri, subscriber, topicType, topic, description, expiresAt, sendLoBody,
                includeSubscriptionEventType, notifyRequestStatusChange);
    }

    @Override
    public Model fromJava(Subscription entity) {
        ModelBuilder builder = new ModelBuilder()
                .subject(entity.iri())
                .add(RDF.TYPE, API.Subscription)
                .add(API.hasSubscriber, entity.subscriber())
                .add(API.hasTopic, Values.literal(entity.topic(), XMLSchema.ANYURI))
                .add(API.hasTopicType, entity.topicType().getValue());
        entity.description().ifPresent(description -> builder.add(API.hasDescription, description));
        entity.includeSubscriptionEventType().ifPresent(eventTypes ->
                eventTypes.forEach(eventType -> builder.add(API.includeSubscriptionEventType, Values.iri(eventType))));
        entity.expiresAt()
                .map(Instant::toString)
                .ifPresent(e -> builder.add(API.expiresAt, Values.literal(e, XMLSchema.DATETIME)));
        entity.sendLoBody().ifPresent(e -> builder.add(API.sendLogisticsObjectBody, Values.literal(e)));
        Subscription.contentTypes().forEach(ct -> builder.add(API.hasSupportedContentType, ct));
        return builder.build();
    }
}
