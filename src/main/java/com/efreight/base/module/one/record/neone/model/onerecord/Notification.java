// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.model.onerecord;

import org.eclipse.rdf4j.model.IRI;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class Notification implements Referencable {

    private final IRI iri;
    private final IRI eventType;
    private final Optional<String> hasLogisticsObjectType;
    private final Optional<IRI> hasLogisticsObject;
    private final Optional<IRI> triggeredBy;
    private final Set<IRI> changedProperties;

    public Notification(IRI iri, IRI eventType, Optional<String> hasLogisticsObjectType,
                        Optional<IRI> hasLogisticsObject, Optional<IRI> triggeredBy,
                        Set<IRI> changedProperties) {
        this.iri = iri;
        this.eventType = eventType;
        this.hasLogisticsObjectType = hasLogisticsObjectType;
        this.hasLogisticsObject = hasLogisticsObject;
        this.triggeredBy = triggeredBy;
        this.changedProperties = changedProperties;
    }

    @Override
    public IRI iri() {
        return iri;
    }

    public IRI eventType() {
        return eventType;
    }

    public Optional<String> hasLogisticsObjectType() {
        return hasLogisticsObjectType;
    }

    public Optional<IRI> hasLogisticsObject() {
        return hasLogisticsObject;
    }

    public Optional<IRI> triggeredBy() {
        return triggeredBy;
    }

    public Set<IRI> changedProperties() {
        return changedProperties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return Objects.equals(iri, that.iri) &&
                Objects.equals(eventType, that.eventType) &&
                Objects.equals(hasLogisticsObjectType, that.hasLogisticsObjectType) &&
                Objects.equals(hasLogisticsObject, that.hasLogisticsObject) &&
                Objects.equals(triggeredBy, that.triggeredBy) &&
                Objects.equals(changedProperties, that.changedProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iri, eventType, hasLogisticsObjectType, hasLogisticsObject, triggeredBy, changedProperties);
    }

    @Override
    public String toString() {
        return "Notification{" +
                "iri=" + iri +
                ", eventType=" + eventType +
                ", hasLogisticsObjectType=" + hasLogisticsObjectType +
                ", hasLogisticsObject=" + hasLogisticsObject +
                ", triggeredBy=" + triggeredBy +
                ", changedProperties=" + changedProperties +
                '}';
    }
}
