// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.controller.onerecord;

import com.efreight.base.module.one.record.neone.model.onerecord.LogisticsObject;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.util.Models;
import org.eclipse.rdf4j.model.vocabulary.RDF;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LogisticsObjectList {

    private List<LogisticsObject> logisticsObjects;
    private IRI rootIRI;
    private Set<IRI> rootLoTypes;

    public LogisticsObjectList() {
    }

    public LogisticsObjectList(List<LogisticsObject> logisticsObjects, IRI rootIRI, Set<IRI> rootLoTypes) {
        this.logisticsObjects = logisticsObjects;
        this.rootIRI = rootIRI;
        this.rootLoTypes = rootLoTypes;
    }

    public static LogisticsObjectList fromLogisticsObject(LogisticsObject logisticsObject) {
        Set<IRI> types = Models.objectIRIs(logisticsObject.model().filter(logisticsObject.iri(), RDF.TYPE, null));
        List<LogisticsObject> list = new ArrayList<>();
        list.add(logisticsObject);
        return new LogisticsObjectList(list, logisticsObject.iri(), types);
    }

    public List<LogisticsObject> getLogisticsObjects() {
        return logisticsObjects;
    }

    public void setLogisticsObjects(List<LogisticsObject> logisticsObjects) {
        this.logisticsObjects = logisticsObjects;
    }

    public IRI getRootIRI() {
        return rootIRI;
    }

    public void setRootIRI(IRI rootIRI) {
        this.rootIRI = rootIRI;
    }

    public Set<IRI> getRootLoTypes() {
        return rootLoTypes;
    }

    public void setRootLoTypes(Set<IRI> rootLoTypes) {
        this.rootLoTypes = rootLoTypes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LogisticsObjectList that = (LogisticsObjectList) o;

        if (logisticsObjects != null ? !logisticsObjects.equals(that.logisticsObjects) : that.logisticsObjects != null)
            return false;
        if (rootIRI != null ? !rootIRI.equals(that.rootIRI) : that.rootIRI != null) return false;
        return rootLoTypes != null ? rootLoTypes.equals(that.rootLoTypes) : that.rootLoTypes == null;
    }

    @Override
    public int hashCode() {
        int result = logisticsObjects != null ? logisticsObjects.hashCode() : 0;
        result = 31 * result + (rootIRI != null ? rootIRI.hashCode() : 0);
        result = 31 * result + (rootLoTypes != null ? rootLoTypes.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LogisticsObjectList{" +
                "logisticsObjects=" + logisticsObjects +
                ", rootIRI=" + rootIRI +
                ", rootLoTypes=" + rootLoTypes +
                '}';
    }
}
