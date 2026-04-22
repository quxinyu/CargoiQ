// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.model.onerecord;

import org.eclipse.rdf4j.model.IRI;

import java.time.Instant;
import java.util.Optional;

public interface ActionRequest extends Referencable {
    Optional<Error> error();

    RequestStatus requestStatus();

    Instant requestedAt();

    IRI requestedBy();

    Optional<Instant> revokedAt();

    Optional<IRI> revokedBy();

    ActionRequest withStatus(RequestStatus status);

    ActionRequest withRevocation(IRI revokedBy, Instant revokedAt);
}
