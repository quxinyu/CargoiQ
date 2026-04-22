// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.model.onerecord;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;

public final class LogisticsObject implements Referencable {
    private final IRI iri;
    private final Model model;

    public LogisticsObject(IRI iri, Model model) {
        this.iri = iri;
        this.model = model;
    }

    public IRI iri() {
        return iri;
    }

    public Model model() {
        return model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LogisticsObject that = (LogisticsObject) o;

        if (iri != null ? !iri.equals(that.iri) : that.iri != null) return false;
        return model != null ? model.equals(that.model) : that.model == null;
    }

    @Override
    public int hashCode() {
        int result = iri != null ? iri.hashCode() : 0;
        result = 31 * result + (model != null ? model.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LogisticsObject{" +
                "iri=" + iri +
                ", model=" + model +
                '}';
    }
}
