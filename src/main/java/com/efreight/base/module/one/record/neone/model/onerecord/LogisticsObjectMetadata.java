//  Copyright Open Logistics Foundation
//
//  Licensed under the Open Logistics Foundation License 1.3.
//  For details on the licensing terms, see the LICENSE file.
//  SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.model.onerecord;

import org.eclipse.rdf4j.model.IRI;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

public class LogisticsObjectMetadata implements Metadata {

    private final IRI iri;
    private final IRI describes;
    private final Integer revision; // latest revision
    private final Instant createdAt;
    private final Optional<Boolean> hasPredefinedIri;

    public LogisticsObjectMetadata(IRI iri, IRI describes, Integer revision, Instant createdAt, Optional<Boolean> hasPredefinedIri) {
        this.iri = iri;
        this.describes = describes;
        this.revision = revision;
        this.createdAt = createdAt;
        this.hasPredefinedIri = hasPredefinedIri;
    }

    public IRI getIri() {
        return iri;
    }

    public IRI getDescribes() {
        return describes;
    }

    public Integer getRevision() {
        return revision;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Optional<Boolean> getHasPredefinedIri() {
        return hasPredefinedIri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogisticsObjectMetadata that = (LogisticsObjectMetadata) o;
        return Objects.equals(iri, that.iri) &&
                Objects.equals(describes, that.describes) &&
                Objects.equals(revision, that.revision) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(hasPredefinedIri, that.hasPredefinedIri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iri, describes, revision, createdAt, hasPredefinedIri);
    }

    @Override
    public String toString() {
        return "LogisticsObjectMetadata{" +
                "iri=" + iri +
                ", describes=" + describes +
                ", revision=" + revision +
                ", createdAt=" + createdAt +
                ", hasPredefinedIri=" + hasPredefinedIri +
                '}';
    }

    @Override
    public IRI describes() {
        return null;
    }

    @Override
    public IRI iri() {
        return null;
    }
}
