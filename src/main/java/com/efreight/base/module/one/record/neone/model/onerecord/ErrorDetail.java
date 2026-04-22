// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.model.onerecord;

import org.eclipse.rdf4j.model.IRI;

import java.util.Objects;
import java.util.Optional;

public class ErrorDetail implements Referencable {

    private final IRI iri;
    private final String code;
    private final Optional<String> message;
    private final Optional<IRI> property;
    private final Optional<IRI> resource;

    public ErrorDetail(IRI iri, String code, Optional<String> message,
                      Optional<IRI> property, Optional<IRI> resource) {
        this.iri = iri;
        this.code = code;
        this.message = message;
        this.property = property;
        this.resource = resource;
    }

    @Override
    public IRI iri() {
        return iri;
    }

    public String code() {
        return code;
    }

    public Optional<String> message() {
        return message;
    }

    public Optional<IRI> property() {
        return property;
    }

    public Optional<IRI> resource() {
        return resource;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorDetail that = (ErrorDetail) o;
        return Objects.equals(iri, that.iri) &&
                Objects.equals(code, that.code) &&
                Objects.equals(message, that.message) &&
                Objects.equals(property, that.property) &&
                Objects.equals(resource, that.resource);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iri, code, message, property, resource);
    }

    @Override
    public String toString() {
        return "ErrorDetail{" +
                "iri=" + iri +
                ", code=" + code +
                ", message=" + message +
                ", property=" + property +
                ", resource=" + resource +
                '}';
    }
}
