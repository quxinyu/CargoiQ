//  Copyright Open Logistics Foundation
//
//  Licensed under the Open Logistics Foundation License 1.3.
//  For details on the licensing terms, see the LICENSE file.
//  SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.exception;

import org.eclipse.rdf4j.model.IRI;

import java.util.Optional;

public class AccessDeniedException extends RuntimeException {

    final Optional<IRI> agent;

    final Optional<IRI> accessTo;

    public AccessDeniedException() {
        agent = Optional.empty();
        accessTo = Optional.empty();
    }

    public AccessDeniedException(IRI agent, IRI accessTo) {
        this.agent = Optional.ofNullable(agent);
        this.accessTo = Optional.ofNullable(accessTo);
    }
}
