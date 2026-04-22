// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.service.onerecord;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.util.Values;

import java.net.URI;
import java.util.Objects;

public final class NeoneId {
    private final IRI iri;
    private final URI uri;
    private final boolean isInternal;
    private final boolean isLocal;

    private NeoneId(IRI iri, URI uri, boolean isInternal, boolean isLocal) {
        this.iri = iri;
        this.uri = uri;
        this.isInternal = isInternal;
        this.isLocal = isLocal;
    }

    public IRI getIri() {
        return iri;
    }

    public URI getUri() {
        return uri;
    }

    public static NeoneId fromUri(URI uri) {
        return fromUri(uri, false, true);
    }

    public static NeoneId fromUri(URI uri, boolean isInternal, boolean isLocal) {
        return new NeoneId(Values.iri(uri.toString()), uri, isInternal, isLocal);
    }

    public static NeoneId fromIri(IRI iri) {
        return fromIri(iri, false, true);
    }

    public static NeoneId fromIri(IRI iri, boolean isInternal, boolean isLocal) {
        return new NeoneId(iri, URI.create(iri.stringValue()), isInternal, isLocal);
    }

    public static NeoneId fromString(String iri) {
        return fromIri(Values.iri(iri));
    }

    public static NeoneId fromString(String iri, boolean isInternal, boolean isLocal) {
        return new NeoneId(Values.iri(iri), URI.create(iri), isInternal, isLocal);
    }

    @Deprecated
    public static boolean isInternalIri(IRI iri) {
        //return NeoneId.fromIri(iri).isInternal;
        return iri.getNamespace().equals("neone:");
    }

    public boolean isInternal() {
        return isInternal;
    }

    public boolean isLocal() {
        return isLocal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NeoneId neoneId = (NeoneId) o;
        return Objects.equals(iri, neoneId.iri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iri);
    }

    @Override
    public String toString() {
        return "NeoneId{" +
                "iri=" + iri.stringValue() +
                '}';
    }
}
