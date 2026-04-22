//  Copyright Open Logistics Foundation
//
//  Licensed under the Open Logistics Foundation License 1.3.
//  For details on the licensing terms, see the LICENSE file.
//  SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.exception;

import org.eclipse.rdf4j.model.IRI;

import java.util.Set;

public class InvalidAccessDelegationObjectException extends RuntimeException {

    public final Set<IRI> loIris;

    public InvalidAccessDelegationObjectException(Set<IRI> loIris) {
        this.loIris = loIris;
    }
}
