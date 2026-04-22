//  Copyright Open Logistics Foundation
//
//  Licensed under the Open Logistics Foundation License 1.3.
//  For details on the licensing terms, see the LICENSE file.
//  SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.model.onerecord;

import org.eclipse.rdf4j.model.IRI;

import java.net.URL;
import java.util.Objects;

public class SubscriptionMetadata implements Metadata {

    private final IRI iri;
    private final IRI describes; // => Subscription IRI
    private final URL callbackUrl;

    public SubscriptionMetadata(IRI iri, IRI describes, URL callbackUrl) {
        this.iri = iri;
        this.describes = describes;
        this.callbackUrl = callbackUrl;
    }

    @Override
    public IRI iri() {
        return iri;
    }

    public IRI describes() {
        return describes;
    }

    public URL callbackUrl() {
        return callbackUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubscriptionMetadata that = (SubscriptionMetadata) o;
        return Objects.equals(iri, that.iri) &&
                Objects.equals(describes, that.describes) &&
                Objects.equals(callbackUrl, that.callbackUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iri, describes, callbackUrl);
    }

    @Override
    public String toString() {
        return "SubscriptionMetadata{" +
                "iri=" + iri +
                ", describes=" + describes +
                ", callbackUrl=" + callbackUrl +
                '}';
    }
}
