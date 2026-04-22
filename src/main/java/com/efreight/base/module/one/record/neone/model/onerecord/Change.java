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

public class Change implements Referencable {

    private final IRI iri;
    private final Optional<String> description;
    private final Set<Operation> operations;
    private final Integer revision;
    private final IRI hasLogisticsObject;
    private final Optional<Boolean> notifyRequestStatusChange;

    public Change(IRI iri, Optional<String> description, Set<Operation> operations,
                 Integer revision, IRI hasLogisticsObject,
                 Optional<Boolean> notifyRequestStatusChange) {
        this.iri = iri;
        this.description = description;
        this.operations = operations;
        this.revision = revision;
        this.hasLogisticsObject = hasLogisticsObject;
        this.notifyRequestStatusChange = notifyRequestStatusChange;
    }

    @Override
    public IRI iri() {
        return iri;
    }

    public Optional<String> description() {
        return description;
    }

    public Set<Operation> operations() {
        return operations;
    }

    public Integer revision() {
        return revision;
    }

    public IRI hasLogisticsObject() {
        return hasLogisticsObject;
    }

    public Optional<Boolean> notifyRequestStatusChange() {
        return notifyRequestStatusChange;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Change change = (Change) o;
        return Objects.equals(iri, change.iri) &&
                Objects.equals(description, change.description) &&
                Objects.equals(operations, change.operations) &&
                Objects.equals(revision, change.revision) &&
                Objects.equals(hasLogisticsObject, change.hasLogisticsObject) &&
                Objects.equals(notifyRequestStatusChange, change.notifyRequestStatusChange);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iri, description, operations, revision, hasLogisticsObject, notifyRequestStatusChange);
    }

    @Override
    public String toString() {
        return "Change{" +
                "iri=" + iri +
                ", description=" + description +
                ", operations=" + operations +
                ", revision=" + revision +
                ", hasLogisticsObject=" + hasLogisticsObject +
                ", notifyRequestStatusChange=" + notifyRequestStatusChange +
                '}';
    }
}
