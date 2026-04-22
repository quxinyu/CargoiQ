// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.model.onerecord;

import org.eclipse.rdf4j.model.IRI;

import java.util.List;
import java.util.Objects;

public class AuditTrail implements Referencable {

    private final IRI iri;
    private final List<ChangeRequest> changeRequests;
    private final int latestRevision;

    public AuditTrail(IRI iri, List<ChangeRequest> changeRequests, int latestRevision) {
        this.iri = iri;
        this.changeRequests = changeRequests;
        this.latestRevision = latestRevision;
    }

    @Override
    public IRI iri() {
        return iri;
    }

    public List<ChangeRequest> changeRequests() {
        return changeRequests;
    }

    public int latestRevision() {
        return latestRevision;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuditTrail auditTrail = (AuditTrail) o;
        return latestRevision == auditTrail.latestRevision &&
                Objects.equals(iri, auditTrail.iri) &&
                Objects.equals(changeRequests, auditTrail.changeRequests);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iri, changeRequests, latestRevision);
    }

    @Override
    public String toString() {
        return "AuditTrail{" +
                "iri=" + iri +
                ", changeRequests=" + changeRequests +
                ", latestRevision=" + latestRevision +
                '}';
    }
}
