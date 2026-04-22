package com.efreight.base.module.one.record.neone.model.onerecord;

import com.efreight.base.module.one.record.neone.iata.onerecord.API;
import org.eclipse.rdf4j.model.IRI;

import java.time.Instant;
import java.util.*;

public class Subscription implements Referencable {

    private final IRI iri;
    private final IRI subscriber;
    private final TopicType topicType;
    private final String topic;
    private final Optional<String> description;
    private final Optional<Instant> expiresAt;
    private final Optional<Boolean> sendLoBody;
    private final Optional<Set<String>> includeSubscriptionEventType;
    private final Optional<Boolean> notifyRequestStatusChange;

    public Subscription(IRI iri, IRI subscriber, TopicType topicType, String topic,
                        Optional<String> description, Optional<Instant> expiresAt,
                        Optional<Boolean> sendLoBody, Optional<Set<String>> includeSubscriptionEventType,
                        Optional<Boolean> notifyRequestStatusChange) {
        this.iri = iri;
        this.subscriber = subscriber;
        this.topicType = topicType;
        this.topic = topic;
        this.description = description;
        this.expiresAt = expiresAt;
        this.sendLoBody = sendLoBody;
        this.includeSubscriptionEventType = includeSubscriptionEventType;
        this.notifyRequestStatusChange = notifyRequestStatusChange;
    }

    public static List<String> contentTypes() {
        return Arrays.asList("application/ld+json", "application/x-turtle", "text/turtle");
    }

    @Override
    public IRI iri() {
        return iri;
    }

    public IRI subscriber() {
        return subscriber;
    }

    public TopicType topicType() {
        return topicType;
    }

    public String topic() {
        return topic;
    }

    public Optional<String> description() {
        return description;
    }

    public Optional<Instant> expiresAt() {
        return expiresAt;
    }

    public Optional<Boolean> sendLoBody() {
        return sendLoBody;
    }

    public Optional<Set<String>> includeSubscriptionEventType() {
        return includeSubscriptionEventType;
    }

    public Optional<Boolean> notifyRequestStatusChange() {
        return notifyRequestStatusChange;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subscription that = (Subscription) o;
        return Objects.equals(iri, that.iri) &&
                Objects.equals(subscriber, that.subscriber) &&
                topicType == that.topicType &&
                Objects.equals(topic, that.topic) &&
                Objects.equals(description, that.description) &&
                Objects.equals(expiresAt, that.expiresAt) &&
                Objects.equals(sendLoBody, that.sendLoBody) &&
                Objects.equals(includeSubscriptionEventType, that.includeSubscriptionEventType) &&
                Objects.equals(notifyRequestStatusChange, that.notifyRequestStatusChange);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iri, subscriber, topicType, topic, description, expiresAt, sendLoBody,
                includeSubscriptionEventType, notifyRequestStatusChange);
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "iri=" + iri +
                ", subscriber=" + subscriber +
                ", topicType=" + topicType +
                ", topic='" + topic + '\'' +
                ", description=" + description +
                ", expiresAt=" + expiresAt +
                ", sendLoBody=" + sendLoBody +
                ", includeSubscriptionEventType=" + includeSubscriptionEventType +
                ", notifyRequestStatusChange=" + notifyRequestStatusChange +
                '}';
    }

    public enum TopicType {
        LOGISTICS_OBJECT_TYPE(API.LOGISTICS_OBJECT_TYPE),
        LOGISTICS_OBJECT_IDENTIFIER(API.LOGISTICS_OBJECT_IDENTIFIER);

        private static final Map<IRI, TopicType> reverseLookup = new HashMap<>();
        private final IRI value;

        TopicType(IRI value) {
            this.value = value;
        }

        public IRI getValue() {
            return value;
        }

        public static TopicType from(IRI value) {
            TopicType type = reverseLookup.get(value);
            if (type == null) {
                throw new NoSuchElementException(value.stringValue());
            }

            return type;
        }

        static {
            for (TopicType type : TopicType.values()) {
                reverseLookup.put(type.getValue(), type);
            }
        }
    }
}
