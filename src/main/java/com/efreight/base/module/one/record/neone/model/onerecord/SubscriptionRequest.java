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

public class SubscriptionRequest implements ActionRequest {

    private final IRI iri;
    private final RequestStatus requestStatus;
    private final Instant requestedAt;
    private final IRI requestedBy;
    private final Optional<Instant> revokedAt;
    private final Optional<IRI> revokedBy;
    private final Optional<Error> error;
    private final Subscription subscription;

    public SubscriptionRequest(IRI iri, RequestStatus requestStatus, Instant requestedAt,
                               IRI requestedBy, Optional<Instant> revokedAt, Optional<IRI> revokedBy,
                               Optional<Error> error, Subscription subscription) {
        this.iri = iri;
        this.requestStatus = requestStatus;
        this.requestedAt = requestedAt;
        this.requestedBy = requestedBy;
        this.revokedAt = revokedAt;
        this.revokedBy = revokedBy;
        this.error = error;
        this.subscription = subscription;
    }

    // Getter methods
    public IRI iri() {
        return iri;
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

    public Optional<Error> error() {
        return error;
    }

    public Subscription subscription() {
        return subscription;
    }

    public SubscriptionRequest withStatus(RequestStatus status) {
        return new SubscriptionRequest(this.iri, status, this.requestedAt,
                this.requestedBy, this.revokedAt, this.revokedBy, this.error, this.subscription);
    }

    public SubscriptionRequest withRevocation(IRI revokedBy, Instant revokedAt) {
        return new SubscriptionRequest(this.iri, RequestStatus.REQUEST_REVOKED, this.requestedAt,
                this.requestedBy, Optional.ofNullable(revokedAt), Optional.ofNullable(revokedBy),
                this.error, this.subscription);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubscriptionRequest that = (SubscriptionRequest) o;
        return Objects.equals(iri, that.iri) &&
                requestStatus == that.requestStatus &&
                Objects.equals(requestedAt, that.requestedAt) &&
                Objects.equals(requestedBy, that.requestedBy) &&
                Objects.equals(revokedAt, that.revokedAt) &&
                Objects.equals(revokedBy, that.revokedBy) &&
                Objects.equals(error, that.error) &&
                Objects.equals(subscription, that.subscription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iri, requestStatus, requestedAt, requestedBy, revokedAt, revokedBy, error, subscription);
    }

    @Override
    public String toString() {
        return "SubscriptionRequest{" +
                "iri=" + iri +
                ", requestStatus=" + requestStatus +
                ", requestedAt=" + requestedAt +
                ", requestedBy=" + requestedBy +
                ", revokedAt=" + revokedAt +
                ", revokedBy=" + revokedBy +
                ", error=" + error +
                ", subscription=" + subscription +
                '}';
    }
}
