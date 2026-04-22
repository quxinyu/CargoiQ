// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.model.onerecord;

import org.eclipse.rdf4j.model.IRI;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Error implements Referencable {

    private final IRI iri;
    private final String title;
    private final Set<ErrorDetail> errorDetail;


    public Error(IRI iri, String title, Set<ErrorDetail> errorDetail) {
        this.iri = iri;
        this.title = title;
        // 兼容null，且转为不可变集合（保持原record特性）
        this.errorDetail = errorDetail == null
                ? Collections.emptySet()
                : Collections.unmodifiableSet(new HashSet<>(errorDetail));
    }

    // 仅保留核心getter（保证equals/hashCode能访问属性）
    public org.eclipse.rdf4j.model.IRI iri() {
        return iri;
    }

    public String title() {
        return title;
    }

    public Set<ErrorDetail> errorDetail() {
        return errorDetail;
    }

    // 静态工厂方法（保留原逻辑，替换JDK9+的Set.of()）
    public static Error createError(IRI iri, String title) {
        return new Error(iri, title, Collections.emptySet());
    }

    public static Error createError(IRI iri, String title, ErrorDetail detail) {
        Set<ErrorDetail> detailSet = new HashSet<>();
        if (detail != null) {
            detailSet.add(detail);
        }
        return new Error(iri, title, Collections.unmodifiableSet(detailSet));
    }

    public static Error createError(IRI iri, String title,
                                    IRI detailIri, String code, String detailMessage) {
        return createError(iri,
                title,
                new ErrorDetail(detailIri,
                        code,
                        Optional.ofNullable(detailMessage),
                        Optional.empty(),
                        Optional.empty()));
    }

    // ========== 仅保留的核心方法：equals ==========
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Error error = (Error) o;

        if (iri != null ? !iri.equals(error.iri) : error.iri != null) return false;
        if (title != null ? !title.equals(error.title) : error.title != null) return false;
        return errorDetail != null ? errorDetail.equals(error.errorDetail) : error.errorDetail == null;
    }

    // ========== 仅保留的核心方法：hashCode ==========
    @Override
    public int hashCode() {
        int result = iri != null ? iri.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (errorDetail != null ? errorDetail.hashCode() : 0);
        return result;
    }

    // ========== 仅保留的核心方法：toString ==========
    @Override
    public String toString() {
        return "Error{" +
                "iri=" + iri +
                ", title='" + title + '\'' +
                ", errorDetail=" + errorDetail +
                '}';
    }
}
