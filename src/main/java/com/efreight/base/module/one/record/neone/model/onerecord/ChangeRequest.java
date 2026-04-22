// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.model.onerecord;

import org.eclipse.rdf4j.model.IRI;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

public class ChangeRequest implements ActionRequest {

    private final IRI iri;
    private final Optional<Error> error;
    private final RequestStatus requestStatus;
    private final Instant requestedAt;
    private final IRI requestedBy;
    private final Optional<Instant> revokedAt;
    private final Optional<IRI> revokedBy;
    private final Change change;

    public ChangeRequest(IRI iri, Optional<Error> error, RequestStatus requestStatus,
                         Instant requestedAt, IRI requestedBy, Optional<Instant> revokedAt,
                         Optional<IRI> revokedBy, Change change) {
        this.iri = iri;
        this.error = error;
        this.requestStatus = requestStatus;
        this.requestedAt = requestedAt;
        this.requestedBy = requestedBy;
        this.revokedAt = revokedAt;
        this.revokedBy = revokedBy;
        this.change = change;
    }

    @Override
    public IRI iri() {
        return iri;
    }

    public Optional<Error> error() {
        return error;
    }

    public RequestStatus requestStatus() {
        return requestStatus;
    }

    public Instant requestedAt() {
        return requestedAt;
    }

    public IRI requestedBy() {
        return requestedBy;
    }

    public Optional<Instant> revokedAt() {
        return revokedAt;
    }

    public Optional<IRI> revokedBy() {
        return revokedBy;
    }

    public Change change() {
        return change;
    }

    public ChangeRequest withStatus(RequestStatus status) {
        return new ChangeRequest(this.iri, this.error, status, this.requestedAt,
                this.requestedBy, this.revokedAt, this.revokedBy, this.change);
    }

    public ChangeRequest withRevocation(IRI revokedBy, Instant revokedAt) {
        return new ChangeRequest(this.iri, this.error, RequestStatus.REQUEST_REVOKED, this.requestedAt,
                this.requestedBy, Optional.ofNullable(revokedAt), Optional.ofNullable(revokedBy),
                this.change);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChangeRequest that = (ChangeRequest) o;
        return Objects.equals(iri, that.iri) &&
                Objects.equals(error, that.error) &&
                requestStatus == that.requestStatus &&
                Objects.equals(requestedAt, that.requestedAt) &&
                Objects.equals(requestedBy, that.requestedBy) &&
                Objects.equals(revokedAt, that.revokedAt) &&
                Objects.equals(revokedBy, that.revokedBy) &&
                Objects.equals(change, that.change);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iri, error, requestStatus, requestedAt, requestedBy, revokedAt, revokedBy, change);
    }

    @Override
    public String toString() {
        return "ChangeRequest{" +
                "iri=" + iri +
                ", error=" + error +
                ", requestStatus=" + requestStatus +
                ", requestedAt=" + requestedAt +
                ", requestedBy=" + requestedBy +
                ", revokedAt=" + revokedAt +
                ", revokedBy=" + revokedBy +
                ", change=" + change +
                '}';
    }
}
