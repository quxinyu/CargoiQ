package com.efreight.base.module.one.record.neone.model.onerecord;

import com.efreight.base.module.one.record.neone.iata.onerecord.NEONE;
import org.eclipse.rdf4j.model.IRI;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Event that is raised for certain cargo actions. Also transports the state of processing
 * (NEW, PENDING, PROCESSED).
 */
public class NeoneEvent implements Referencable {

    public static final String ADDRESS = "lo-event";

    private final IRI iri;
    private final IRI loId;
    private final IRI notificationEventType;
    private final State state;
    private final Optional<IRI> triggeredBy;
    private final Set<IRI> changedProperties;
    private final Set<IRI> loTypes;

    public NeoneEvent(IRI iri, IRI loId, IRI notificationEventType, State state,
                      Optional<IRI> triggeredBy, Set<IRI> changedProperties, Set<IRI> loTypes) {
        this.iri = iri;
        this.loId = loId;
        this.notificationEventType = notificationEventType;
        this.state = state;
        this.triggeredBy = triggeredBy;
        this.changedProperties = changedProperties;
        this.loTypes = loTypes;
    }

    @Override
    public IRI iri() {
        return iri;
    }

    public IRI loId() {
        return loId;
    }

    public IRI notificationEventType() {
        return notificationEventType;
    }

    public State state() {
        return state;
    }

    public Optional<IRI> triggeredBy() {
        return triggeredBy;
    }

    public Set<IRI> changedProperties() {
        return changedProperties;
    }

    public Set<IRI> loTypes() {
        return loTypes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NeoneEvent neoneEvent = (NeoneEvent) o;
        return Objects.equals(iri, neoneEvent.iri) &&
                Objects.equals(loId, neoneEvent.loId) &&
                Objects.equals(notificationEventType, neoneEvent.notificationEventType) &&
                state == neoneEvent.state &&
                Objects.equals(triggeredBy, neoneEvent.triggeredBy) &&
                Objects.equals(changedProperties, neoneEvent.changedProperties) &&
                Objects.equals(loTypes, neoneEvent.loTypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iri, loId, notificationEventType, state, triggeredBy, changedProperties, loTypes);
    }

    @Override
    public String toString() {
        return "NeoneEvent{" +
                "iri=" + iri +
                ", loId=" + loId +
                ", notificationEventType=" + notificationEventType +
                ", state=" + state +
                ", triggeredBy=" + triggeredBy +
                ", changedProperties=" + changedProperties +
                ", loTypes=" + loTypes +
                '}';
    }

    public enum State {
        NEW(NEONE.NEW),
        PENDING(NEONE.PENDING),
        PROCESSED(NEONE.PROCESSED);

        private static final Map<IRI, State> reverseLookup = new HashMap<>();

        private final IRI value;

        State(IRI value) {
            this.value = value;
        }

        public IRI getValue() {
            return value;
        }

        public static State from(IRI value) {
            State state = reverseLookup.get(value);
            if (state == null) {
                throw new NoSuchElementException(value.stringValue());
            }

            return state;
        }

        static {
            for (State state : State.values()) {
                reverseLookup.put(state.getValue(), state);
            }
        }
    }
}
