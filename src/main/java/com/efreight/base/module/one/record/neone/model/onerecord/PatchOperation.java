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

public enum PatchOperation {
    ADD(API.ADD),
    DELETE(API.DELETE);

    private static final Map<IRI, PatchOperation> reverseLookup = new HashMap<>();
    private final IRI iri;

    PatchOperation(IRI iri) {
        this.iri = iri;
    }

    public IRI iri() {
        return iri;
    }

    public static PatchOperation from(IRI value) {
        PatchOperation patchOperation = reverseLookup.get(value);
        if (patchOperation == null) {
            throw new NoSuchElementException(value.stringValue());
        }

        return patchOperation;
    }

    static {
        for (PatchOperation patchOperation : PatchOperation.values()) {
            reverseLookup.put(patchOperation.iri(), patchOperation);
        }
    }
}
