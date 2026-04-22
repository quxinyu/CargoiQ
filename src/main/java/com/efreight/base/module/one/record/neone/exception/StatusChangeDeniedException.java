//  Copyright Open Logistics Foundation
//
//  Licensed under the Open Logistics Foundation License 1.3.
//  For details on the licensing terms, see the LICENSE file.
//  SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.exception;

import org.eclipse.rdf4j.model.IRI;

public class StatusChangeDeniedException extends RuntimeException {

    private final IRI actionRequestIri;

    public StatusChangeDeniedException(IRI actionRequestIri) {
        this.actionRequestIri = actionRequestIri;
    }

    public IRI getActionRequestIri() {
        return actionRequestIri;
    }
}
