// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.model.onerecord;

import org.eclipse.rdf4j.model.IRI;

import java.time.Instant;
import java.util.Objects;

/**
 * Snapshot of some LO for a given point in time.
 */
public class Snapshot implements Referencable {

    private final IRI iri;
    private final IRI loIri;
    private final String payload;
    private final Instant at;
    private final Integer revision;

    public Snapshot(IRI iri, IRI loIri, String payload, Instant at, Integer revision) {
        this.iri = iri;
        this.loIri = loIri;
        this.payload = payload;
        this.at = at;
        this.revision = revision;
    }

    @Override
    public IRI iri() {
        return iri;
    }

    public IRI loIri() {
        return loIri;
    }

    public String payload() {
        return payload;
    }

    public Instant at() {
        return at;
    }

    public Integer revision() {
        return revision;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Snapshot snapshot = (Snapshot) o;
        return Objects.equals(iri, snapshot.iri) &&
                Objects.equals(loIri, snapshot.loIri) &&
                Objects.equals(payload, snapshot.payload) &&
                Objects.equals(at, snapshot.at) &&
                Objects.equals(revision, snapshot.revision);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iri, loIri, payload, at, revision);
    }

    @Override
    public String toString() {
        return "Snapshot{" +
                "iri=" + iri +
                ", loIri=" + loIri +
                ", payload='" + payload + '\'' +
                ", at=" + at +
                ", revision=" + revision +
                '}';
    }
}
