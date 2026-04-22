//  Copyright Open Logistics Foundation
//
//  Licensed under the Open Logistics Foundation License 1.3.
//  For details on the licensing terms, see the LICENSE file.
//  SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.model.onerecord;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.rdf4j.model.IRI;

import java.time.Instant;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessDelegationRequest implements ActionRequest {

    private IRI iri;
    private Optional<Error> error;
    private RequestStatus requestStatus;
    private Instant requestedAt;
    private IRI requestedBy;
    private Optional<Instant> revokedAt;
    private Optional<IRI> revokedBy;
    private AccessDelegation accessDelegation;

    @Override
    public IRI iri() {
        return iri;
    }

    @Override
    public Optional<Error> error() {
        return error;
    }

    @Override
    public RequestStatus requestStatus() {
        return requestStatus;
    }

    @Override
    public Instant requestedAt() {
        return requestedAt;
    }

    @Override
    public IRI requestedBy() {
        return requestedBy;
    }

    @Override
    public Optional<Instant> revokedAt() {
        return revokedAt;
    }

    @Override
    public Optional<IRI> revokedBy() {
        return revokedBy;
    }

    public AccessDelegationRequest withStatus(RequestStatus status) {
        return new AccessDelegationRequest(this.iri, this.error, status, this.requestedAt,
            this.requestedBy, this.revokedAt, this.revokedBy, this.accessDelegation);
    }

    public AccessDelegationRequest withRevocation(IRI revokedBy, Instant revokedAt) {
        return new AccessDelegationRequest(this.iri, this.error, RequestStatus.REQUEST_REVOKED, this.requestedAt,
            this.requestedBy, Optional.ofNullable(revokedAt), Optional.ofNullable(revokedBy),
            this.accessDelegation);
    }

}
