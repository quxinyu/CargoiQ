// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.model.onerecord;

import org.eclipse.rdf4j.model.IRI;

import java.util.Set;

public final class AclAuthorization implements Referencable {
    private final IRI iri;
    private final IRI accessTo;
    private final IRI agent;
    private final Set<IRI> modes;

    public AclAuthorization(IRI iri, IRI accessTo, IRI agent, Set<IRI> modes) {
        this.iri = iri;
        this.accessTo = accessTo;
        this.agent = agent;
        this.modes = modes;
    }

    public IRI iri() {
        return iri;
    }

    public IRI accessTo() {
        return accessTo;
    }

    public IRI agent() {
        return agent;
    }

    public Set<IRI> modes() {
        return modes;
    }

    public AclAuthorization withIri(IRI iri) {
        return new AclAuthorization(iri, this.accessTo, this.agent, this.modes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AclAuthorization that = (AclAuthorization) o;

        if (iri != null ? !iri.equals(that.iri) : that.iri != null) return false;
        if (accessTo != null ? !accessTo.equals(that.accessTo) : that.accessTo != null) return false;
        if (agent != null ? !agent.equals(that.agent) : that.agent != null) return false;
        return modes != null ? modes.equals(that.modes) : that.modes == null;
    }

    @Override
    public int hashCode() {
        int result = iri != null ? iri.hashCode() : 0;
        result = 31 * result + (accessTo != null ? accessTo.hashCode() : 0);
        result = 31 * result + (agent != null ? agent.hashCode() : 0);
        result = 31 * result + (modes != null ? modes.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AclAuthorization{" +
                "iri=" + iri +
                ", accessTo=" + accessTo +
                ", agent=" + agent +
                ", modes=" + modes +
                '}';
    }
}
