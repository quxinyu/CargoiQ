//  Copyright Open Logistics Foundation
//
//  Licensed under the Open Logistics Foundation License 1.3.
//  For details on the licensing terms, see the LICENSE file.
//  SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.model.onerecord;

import com.efreight.base.module.one.record.neone.iata.onerecord.API;
import org.eclipse.rdf4j.model.IRI;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public enum NotificationEventType {

    LOGISTICS_OBJECT_CREATED(API.LOGISTICS_OBJECT_CREATED),
    LOGISTICS_OBJECT_UPDATED(API.LOGISTICS_OBJECT_UPDATED),
    LOGISTICS_EVENT_RECEIVED(API.LOGISTICS_EVENT_RECEIVED),
    CHANGE_REQUEST_PENDING(API.CHANGE_REQUEST_PENDING),
    CHANGE_REQUEST_ACCEPTED(API.CHANGE_REQUEST_ACCEPTED),
    CHANGE_REQUEST_REJECTED(API.CHANGE_REQUEST_REJECTED),
    CHANGE_REQUEST_FAILED(API.CHANGE_REQUEST_FAILED),
    CHANGE_REQUEST_REVOKED(API.CHANGE_REQUEST_REVOKED),
    ACCESS_DELEGATION_REQUEST_PENDING(API.ACCESS_DELEGATION_REQUEST_PENDING),
    ACCESS_DELEGATION_REQUEST_ACCEPTED(API.ACCESS_DELEGATION_REQUEST_ACCEPTED),
    ACCESS_DELEGATION_REQUEST_REJECTED(API.ACCESS_DELEGATION_REQUEST_REJECTED),
    ACCESS_DELEGATION_REQUEST_FAILED(API.ACCESS_DELEGATION_REQUEST_FAILED),
    ACCESS_DELEGATION_REQUEST_REVOKED(API.ACCESS_DELEGATION_REQUEST_REVOKED),
    SUBSCRIPTION_REQUEST_PENDING(API.SUBSCRIPTION_REQUEST_PENDING),
    SUBSCRIPTION_REQUEST_ACCEPTED(API.SUBSCRIPTION_REQUEST_ACCEPTED),
    SUBSCRIPTION_REQUEST_REJECTED(API.SUBSCRIPTION_REQUEST_REJECTED),
    SUBSCRIPTION_REQUEST_FAILED(API.SUBSCRIPTION_REQUEST_FAILED),
    SUBSCRIPTION_REQUEST_REVOKED(API.SUBSCRIPTION_REQUEST_REVOKED);

    private static final Map<IRI, NotificationEventType> reverseLookup = new HashMap<>();
    private final IRI iri;

    NotificationEventType(IRI iri) {
        this.iri = iri;
    }

    public IRI iri() {
        return iri;
    }

    public static NotificationEventType from(IRI value) {
        NotificationEventType type = reverseLookup.get(value);
        if (type == null) {
            throw new NoSuchElementException(value.stringValue());
        }

        return type;
    }

    static {
        for (NotificationEventType type : NotificationEventType.values()) {
            reverseLookup.put(type.iri(), type);
        }
    }
}
