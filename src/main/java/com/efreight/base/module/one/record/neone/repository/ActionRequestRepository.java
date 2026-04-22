// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.repository;

import com.efreight.base.module.one.record.neone.iata.onerecord.API;
import com.efreight.base.module.one.record.neone.model.onerecord.ActionRequest;
import com.efreight.base.module.one.record.neone.model.onerecord.IdProvider;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.sparqlbuilder.core.SparqlBuilder;
import org.eclipse.rdf4j.sparqlbuilder.core.Variable;
import org.eclipse.rdf4j.sparqlbuilder.core.query.Queries;
import org.eclipse.rdf4j.sparqlbuilder.graphpattern.GraphPattern;
import org.eclipse.rdf4j.sparqlbuilder.graphpattern.GraphPatterns;

import java.util.ArrayList;
import java.util.List;

public abstract class ActionRequestRepository<T extends ActionRequest> extends ModelRepository<T> {

    public ActionRequestRepository() {
    }

    public ActionRequestRepository(Repository repository, ModelHandler<T> modelHandler, IdProvider idProvider) {
        super(repository, modelHandler, idProvider);
    }

    public abstract IRI getRepositoryType();

    public <AR extends ActionRequest> ModelHandler<AR> getActionRequestModelHandler() {
        return (ModelHandler<AR>) modelHandler;
    }

    public List<IRI> getAllPending(RepositoryConnection connection, IRI typeIri) {
        Variable arVar = SparqlBuilder.var("actionRequest");
        Variable stateIri = SparqlBuilder.var("stateIri");
        Variable type = SparqlBuilder.var("type");

        String queryString = getQueryForState(arVar, type, stateIri);
        TupleQuery query = connection.prepareTupleQuery(queryString);
        query.setBinding("stateIri", API.REQUEST_PENDING);
        query.setBinding("type", typeIri);
        final List<IRI> actionRequestIris = new ArrayList<>();
        try (TupleQueryResult result = query.evaluate()) {
            result.forEach(bindingSet -> {
                IRI crIri = (IRI) bindingSet.getValue("actionRequest");
                actionRequestIris.add(crIri);
            });
        }
        return actionRequestIris;
    }

    static String getQueryForState(Variable actionRequest, Variable type, Variable state) {
        // 修复 JDK 1.8 兼容性问题：使用 GraphPatterns.and 替代 .andHas() 链式调用
        GraphPattern condition = GraphPatterns.and(
                actionRequest.has(API.hasRequestStatus, state),
                actionRequest.has(RDF.TYPE, type)
        );
        return Queries.SELECT()
                .select(actionRequest)
                .where(condition)
                .getQueryString();
    }
}
