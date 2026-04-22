// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.model.onerecord;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

/**
 * Some generic logistics related event, not to be confused with LogisticsObjectEvent.
 */
public final class LogisticsEvent implements Referencable {
    private final IRI iri;
    private final Optional<Instant> creationDate;
    private final Optional<Instant> eventDate;
    private final Optional<IRI> eventCode;
    private final Optional<String> eventName;
    private final Optional<IRI> eventTimeType;
    private final Set<IRI> externalReferences;
    private final Optional<IRI> eventFor;
    private final Optional<IRI> eventLocation;
    private final Optional<IRI> recordingOrganization;
    private final Optional<IRI> recordingActor;
    private final Optional<Model> embeddedLo;
    private final Optional<Model> embeddedEventCodeModel;
    private final Optional<Boolean> partialEventIndicator;

    public LogisticsEvent(IRI iri,
                          Optional<Instant> creationDate,
                          Optional<Instant> eventDate,
                          Optional<IRI> eventCode,
                          Optional<String> eventName,
                          Optional<IRI> eventTimeType,
                          Set<IRI> externalReferences,
                          Optional<IRI> eventFor,
                          Optional<IRI> eventLocation,
                          Optional<IRI> recordingOrganization,
                          Optional<IRI> recordingActor,
                          Optional<Model> embeddedLo,
                          Optional<Model> embeddedEventCodeModel,
                          Optional<Boolean> partialEventIndicator) {
        this.iri = iri;
        this.creationDate = creationDate;
        this.eventDate = eventDate;
        this.eventCode = eventCode;
        this.eventName = eventName;
        this.eventTimeType = eventTimeType;
        this.externalReferences = externalReferences;
        this.eventFor = eventFor;
        this.eventLocation = eventLocation;
        this.recordingOrganization = recordingOrganization;
        this.recordingActor = recordingActor;
        this.embeddedLo = embeddedLo;
        this.embeddedEventCodeModel = embeddedEventCodeModel;
        this.partialEventIndicator = partialEventIndicator;
    }

    public IRI iri() {
        return iri;
    }

    public Optional<Instant> creationDate() {
        return creationDate;
    }

    public Optional<Instant> eventDate() {
        return eventDate;
    }

    public Optional<IRI> eventCode() {
        return eventCode;
    }

    public Optional<String> eventName() {
        return eventName;
    }

    public Optional<IRI> eventTimeType() {
        return eventTimeType;
    }

    public Set<IRI> externalReferences() {
        return externalReferences;
    }

    public Optional<IRI> eventFor() {
        return eventFor;
    }

    public Optional<IRI> eventLocation() {
        return eventLocation;
    }

    public Optional<IRI> recordingOrganization() {
        return recordingOrganization;
    }

    public Optional<IRI> recordingActor() {
        return recordingActor;
    }

    public Optional<Model> embeddedLo() {
        return embeddedLo;
    }

    public Optional<Model> embeddedEventCodeModel() {
        return embeddedEventCodeModel;
    }

    public Optional<Boolean> partialEventIndicator() {
        return partialEventIndicator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LogisticsEvent that = (LogisticsEvent) o;

        if (iri != null ? !iri.equals(that.iri) : that.iri != null) return false;
        if (creationDate != null ? !creationDate.equals(that.creationDate) : that.creationDate != null) return false;
        if (eventDate != null ? !eventDate.equals(that.eventDate) : that.eventDate != null) return false;
        if (eventCode != null ? !eventCode.equals(that.eventCode) : that.eventCode != null) return false;
        if (eventName != null ? !eventName.equals(that.eventName) : that.eventName != null) return false;
        if (eventTimeType != null ? !eventTimeType.equals(that.eventTimeType) : that.eventTimeType != null)
            return false;
        if (externalReferences != null ? !externalReferences.equals(that.externalReferences) : that.externalReferences != null)
            return false;
        if (eventFor != null ? !eventFor.equals(that.eventFor) : that.eventFor != null) return false;
        if (eventLocation != null ? !eventLocation.equals(that.eventLocation) : that.eventLocation != null)
            return false;
        if (recordingOrganization != null ? !recordingOrganization.equals(that.recordingOrganization) : that.recordingOrganization != null)
            return false;
        if (recordingActor != null ? !recordingActor.equals(that.recordingActor) : that.recordingActor != null)
            return false;
        if (embeddedLo != null ? !embeddedLo.equals(that.embeddedLo) : that.embeddedLo != null) return false;
        if (embeddedEventCodeModel != null ? !embeddedEventCodeModel.equals(that.embeddedEventCodeModel) : that.embeddedEventCodeModel != null)
            return false;
        return partialEventIndicator != null ? partialEventIndicator.equals(that.partialEventIndicator) : that.partialEventIndicator == null;
    }

    @Override
    public int hashCode() {
        int result = iri != null ? iri.hashCode() : 0;
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + (eventDate != null ? eventDate.hashCode() : 0);
        result = 31 * result + (eventCode != null ? eventCode.hashCode() : 0);
        result = 31 * result + (eventName != null ? eventName.hashCode() : 0);
        result = 31 * result + (eventTimeType != null ? eventTimeType.hashCode() : 0);
        result = 31 * result + (externalReferences != null ? externalReferences.hashCode() : 0);
        result = 31 * result + (eventFor != null ? eventFor.hashCode() : 0);
        result = 31 * result + (eventLocation != null ? eventLocation.hashCode() : 0);
        result = 31 * result + (recordingOrganization != null ? recordingOrganization.hashCode() : 0);
        result = 31 * result + (recordingActor != null ? recordingActor.hashCode() : 0);
        result = 31 * result + (embeddedLo != null ? embeddedLo.hashCode() : 0);
        result = 31 * result + (embeddedEventCodeModel != null ? embeddedEventCodeModel.hashCode() : 0);
        result = 31 * result + (partialEventIndicator != null ? partialEventIndicator.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LogisticsEvent{" +
                "iri=" + iri +
                ", creationDate=" + creationDate +
                ", eventDate=" + eventDate +
                ", eventCode=" + eventCode +
                ", eventName=" + eventName +
                ", eventTimeType=" + eventTimeType +
                ", externalReferences=" + externalReferences +
                ", eventFor=" + eventFor +
                ", eventLocation=" + eventLocation +
                ", recordingOrganization=" + recordingOrganization +
                ", recordingActor=" + recordingActor +
                ", embeddedLo=" + embeddedLo +
                ", embeddedEventCodeModel=" + embeddedEventCodeModel +
                ", partialEventIndicator=" + partialEventIndicator +
                '}';
    }

    /*
     * Complete the LogisticsEvent. The client might not have the link to the LO properly set, so it is established here.
     */
    public LogisticsEvent withLinkedObjectAndCreatedAt(IRI loIri, Instant createdAt) {
        return new LogisticsEvent(this.iri, Optional.ofNullable(createdAt),
                this.eventDate, this.eventCode, this.eventName,
                this.eventTimeType, this.externalReferences, Optional.ofNullable(loIri),
                this.eventLocation, this.recordingOrganization, this.recordingActor,
                this.embeddedLo, this.embeddedEventCodeModel, this.partialEventIndicator);
    }

    public LogisticsEvent withEmbeddedLoModel(Model loModel) {
        return new LogisticsEvent(this.iri, this.creationDate, this.eventDate, this.eventCode, this.eventName,
                this.eventTimeType, this.externalReferences, this.eventFor,
                this.eventLocation, this.recordingOrganization, this.recordingActor,
                Optional.of(loModel), this.embeddedEventCodeModel, this.partialEventIndicator);
    }

    public LogisticsEvent withId(IRI id) {
        return new LogisticsEvent(id, this.creationDate, this.eventDate, this.eventCode, this.eventName,
                this.eventTimeType, this.externalReferences, this.eventFor,
                this.eventLocation, this.recordingOrganization, this.recordingActor,
                this.embeddedLo, this.embeddedEventCodeModel, this.partialEventIndicator);
    }
}
