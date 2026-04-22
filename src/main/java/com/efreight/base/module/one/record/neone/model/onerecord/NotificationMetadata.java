//  Copyright Open Logistics Foundation
//
//  Licensed under the Open Logistics Foundation License 1.3.
//  For details on the licensing terms, see the LICENSE file.
//  SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.model.onerecord;

import org.eclipse.rdf4j.model.IRI;

import java.net.URL;
import java.util.Objects;

public class NotificationMetadata implements Metadata {

    private final IRI iri;
    private final IRI describes; // => Notification Iri
    private final URL callbackUrl;
    private final Boolean includeLoBody;
    private final IRI loEventIri;
    private final IRI companyId;

    public NotificationMetadata(IRI iri, IRI describes, URL callbackUrl,
                                Boolean includeLoBodyi, IRI loEventIr, IRI companyId) {
        this.iri = iri;
        this.describes = describes;
        this.callbackUrl = callbackUrl;
        this.includeLoBody = includeLoBodyi;
        this.loEventIri = loEventIr;
        this.companyId = companyId;
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

    public Boolean includeLoBody() {
        return includeLoBody;
    }

    public IRI loEventIri() {
        return loEventIri;
    }
    public IRI companyId() {
        return companyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationMetadata that = (NotificationMetadata) o;
        return Objects.equals(iri, that.iri) &&
                Objects.equals(describes, that.describes) &&
                Objects.equals(callbackUrl, that.callbackUrl) &&
                Objects.equals(includeLoBody, that.includeLoBody) &&
                Objects.equals(loEventIri, that.loEventIri) &&
                Objects.equals(companyId, that.companyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iri, describes, callbackUrl, includeLoBody, loEventIri, companyId);
    }

    @Override
    public String toString() {
        return "NotificationMetadata{" +
                "iri=" + iri +
                ", describes=" + describes +
                ", callbackUrl=" + callbackUrl +
                ", includeLoBody=" + includeLoBody +
                ", loEventIri=" + loEventIri +
                ", companyId=" + companyId +
                '}';
    }
}
