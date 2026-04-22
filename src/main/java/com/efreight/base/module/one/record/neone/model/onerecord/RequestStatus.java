// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.model.onerecord;

import com.efreight.base.module.one.record.neone.iata.onerecord.API;
import org.eclipse.rdf4j.model.IRI;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

//@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum RequestStatus {
    REQUEST_ACCEPTED(API.REQUEST_ACCEPTED),
    REQUEST_FAILED(API.REQUEST_FAILED),
    REQUEST_PENDING(API.REQUEST_PENDING),
    REQUEST_REJECTED(API.REQUEST_REJECTED),
    REQUEST_REVOKED(API.REQUEST_REVOKED);

    private static final Map<IRI, RequestStatus> reverseLookup = new HashMap<>();
    private final IRI iri;

    RequestStatus(IRI iri) {
        this.iri = iri;
    }

    public IRI iri() {
        return iri;
    }

    public static RequestStatus from(IRI value) {
        RequestStatus requestStatus = reverseLookup.get(value);
        if (requestStatus == null) {
            throw new NoSuchElementException(value.stringValue());
        }

        return requestStatus;
    }

    static {
        for (RequestStatus patchOperation : RequestStatus.values()) {
            reverseLookup.put(patchOperation.iri(), patchOperation);
        }
    }
}
