// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.repository;

import com.efreight.base.module.one.record.neone.handler.SnapshotHandler;
import com.efreight.base.module.one.record.neone.iata.onerecord.NEONE;
import com.efreight.base.module.one.record.neone.model.onerecord.IdProvider;
import com.efreight.base.module.one.record.neone.model.onerecord.Snapshot;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.sparqlbuilder.constraint.Expressions;
import org.eclipse.rdf4j.sparqlbuilder.core.SparqlBuilder;
import org.eclipse.rdf4j.sparqlbuilder.core.Variable;
import org.eclipse.rdf4j.sparqlbuilder.graphpattern.GraphPatterns;
import org.springframework.stereotype.Component;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Instant;
import java.util.Optional;

@Component
@ApplicationScoped
public class SnapshotRepository extends ModelRepository<Snapshot> {

    @Inject
    public SnapshotRepository(Repository repository, SnapshotHandler handler, IdProvider idProvider) {
        super(repository, handler, idProvider);
    }

    public Optional<Snapshot> getSnapshot(IRI loIri, Instant ts, RepositoryConnection connection) {
        Variable snapshot = SparqlBuilder.var("snapshot");
        Variable logisticsObjectIri = SparqlBuilder.var("loIri");
        Variable at = SparqlBuilder.var("at");

        String queryStr = createSnapshotQuery(snapshot, logisticsObjectIri, at);
        TupleQuery query = connection.prepareTupleQuery(queryStr);
        query.setBinding("loIri", loIri);
//        query.setBinding("at", Values.literal(ts.toString(), XMLSchema.DATETIME));

        try (TupleQueryResult result = query.evaluate()) {
            return result.stream()
                    .map(bindingSet -> {
                        IRI snaphostIri = (IRI) bindingSet.getValue("snapshot");
                        return findByIri(snaphostIri, connection);
                    })
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .findFirst();
        }
    }

    private String createSnapshotQuery(Variable snap, Variable loIri, Variable at) {
        Variable createdAt = SparqlBuilder.var("createdAt");
        // 修复 JDK 1.8 兼容性问题：使用 GraphPatterns.and 替代 .andHas() 链式调用
        return org.eclipse.rdf4j.sparqlbuilder.core.query.Queries.SELECT()
                .select(snap)
                .where(GraphPatterns.and(
//                        snap.isA(NEONE.Snapshot),
                        snap.has(NEONE.referencesLogisticsObject, loIri),
                        snap.has(NEONE.isCreatedAt, createdAt)
                ).filter(Expressions.lte(createdAt, at)))
                .orderBy(createdAt.desc())
                .limit(1)
                .getQueryString();
    }
}
