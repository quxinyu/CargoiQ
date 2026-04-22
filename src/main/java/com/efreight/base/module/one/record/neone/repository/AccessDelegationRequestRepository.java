//  Copyright Open Logistics Foundation
//
//  Licensed under the Open Logistics Foundation License 1.3.
//  For details on the licensing terms, see the LICENSE file.
//  SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.repository;

import com.efreight.base.module.one.record.neone.handler.AccessDelegationRequestHandler;
import com.efreight.base.module.one.record.neone.iata.onerecord.API;
import com.efreight.base.module.one.record.neone.model.onerecord.AccessDelegationRequest;
import com.efreight.base.module.one.record.neone.model.onerecord.IdProvider;
import com.efreight.base.module.one.record.neone.model.onerecord.RequestStatus;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.sparqlbuilder.core.SparqlBuilder;
import org.eclipse.rdf4j.sparqlbuilder.core.Variable;
import org.eclipse.rdf4j.sparqlbuilder.graphpattern.GraphPattern;
import org.eclipse.rdf4j.sparqlbuilder.graphpattern.GraphPatterns;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AccessDelegationRequestRepository extends ActionRequestRepository<AccessDelegationRequest> {

    private static final Logger log = LoggerFactory.getLogger(AccessDelegationRequestRepository.class);

    public AccessDelegationRequestRepository(Repository repository,
                                             AccessDelegationRequestHandler modelHandler,
                                             IdProvider idProvider) {
        super(repository, modelHandler, idProvider);
    }

    @Override
    public IRI getRepositoryType() {
        return API.AccessDelegationRequest;
    }

    public Optional<IRI> findRequestedFor(IRI requestedBy, IRI hasLogisticsObject, RepositoryConnection connection) {
        log.info("Looking for access delegation requested by [{}] for Lo [{}]", requestedBy, hasLogisticsObject);

        Variable requestedForVar = SparqlBuilder.var("requestedFor");
        Variable requestedByVar = SparqlBuilder.var("requestedBy");
        Variable hasLogisticsObjectVar = SparqlBuilder.var("hasLogisticsObject");
        Variable hasRequestStatusVar = SparqlBuilder.var("hasRequestStatus");

        String queryString = createDelegationRequestQuery(requestedForVar, requestedByVar,
                hasLogisticsObjectVar, hasRequestStatusVar);

        TupleQuery query = connection.prepareTupleQuery(queryString);
        query.setBinding("requestedBy", requestedBy);
        query.setBinding("hasLogisticsObject", hasLogisticsObject);
        query.setBinding("hasRequestStatus", RequestStatus.REQUEST_ACCEPTED.iri());

        final IRI[] resultIri = new IRI[1];
        try (TupleQueryResult result = query.evaluate()) {
            result.forEach(bindingSet -> {
                if (resultIri[0] == null) {
                    resultIri[0] = (IRI) bindingSet.getValue("requestedFor");
                }
            });
        }
        return Optional.ofNullable(resultIri[0]);
    }

    private String createDelegationRequestQuery(Variable requestedForVar,
                                                Variable requestedByVar,
                                                Variable logisticsObjectVar,
                                                Variable requestStatus) {
        Variable delegationRequestVar = SparqlBuilder.var("accessDelegationRequest");
        Variable delegationVar = SparqlBuilder.var("accessDelegation");

        // 修复 JDK 1.8 兼容性问题：使用 GraphPatterns.and 替代链式调用
        GraphPattern c1 = GraphPatterns.and(
                delegationRequestVar.has(RDF.TYPE, API.AccessDelegationRequest),
                delegationRequestVar.has(API.isRequestedBy, requestedByVar),
                delegationRequestVar.has(API.hasRequestStatus, requestStatus),
                delegationRequestVar.has(API.hasAccessDelegation, delegationVar)
        );

        GraphPattern c2 = GraphPatterns.and(
                delegationVar.has(RDF.TYPE, API.AccessDelegation),
                delegationVar.has(API.hasLogisticsObject, logisticsObjectVar),
                delegationVar.has(API.isRequestedFor, requestedForVar)
        );

        return org.eclipse.rdf4j.sparqlbuilder.core.query.Queries.SELECT()
                .select(requestedForVar)
                .where(c1, c2)
                .limit(1)
                .getQueryString();
    }
}
