//  Copyright Open Logistics Foundation
//
//  Licensed under the Open Logistics Foundation License 1.3.
//  For details on the licensing terms, see the LICENSE file.
//  SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.repository;

import com.efreight.base.module.one.record.neone.exception.MissingMetadataException;
import com.efreight.base.module.one.record.neone.handler.LogisticsObjectMetadataHandler;
import com.efreight.base.module.one.record.neone.iata.onerecord.NEONE;
import com.efreight.base.module.one.record.neone.model.onerecord.IdProvider;
import com.efreight.base.module.one.record.neone.model.onerecord.LogisticsObjectMetadata;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.util.Literals;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.sparqlbuilder.core.SparqlBuilder;
import org.eclipse.rdf4j.sparqlbuilder.core.Variable;
import org.eclipse.rdf4j.sparqlbuilder.core.query.Queries;
import org.eclipse.rdf4j.sparqlbuilder.graphpattern.GraphPatterns;
import org.eclipse.rdf4j.sparqlbuilder.rdf.Rdf;
import org.springframework.stereotype.Component;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Component
@ApplicationScoped
public class LogisticsObjectMetadataRepository extends MetadataRepository<LogisticsObjectMetadata> {

    @Inject
    public LogisticsObjectMetadataRepository(Repository repository,
                                             LogisticsObjectMetadataHandler modelHandler,
                                             IdProvider idProvider) {
        super(repository, modelHandler, idProvider);
    }

    public int getRevision(IRI loIri, RepositoryConnection connection) {
        Variable revisionVar = SparqlBuilder.var("revision");
        try (TupleQueryResult result = connection.prepareTupleQuery(Q.getRevisionOfLo(revisionVar, loIri)).evaluate()) {
            if (result.hasNext()) {
                BindingSet bindingSet = result.next();
                int revision = Literals.getIntValue(bindingSet.getValue("revisionVar"), -1);
                if (revision == -1) {
                    throw new MissingMetadataException("Missing revision for " + loIri.stringValue() + ".");
                }
                return revision;
            } else  {
                throw new MissingMetadataException("Missing revision for " + loIri.stringValue() + ".");
            }
        }
    }

    private interface Q {
        static String getRevisionOfLo(Variable revision, IRI loIri) {
            Variable metadataSubject = SparqlBuilder.var("loMetadata");

            // 修复 JDK 1.8 兼容性问题：使用 GraphPatterns.and 替代 .andHas() 链式调用
            return Queries.SELECT()
                .select(revision)
                .where(GraphPatterns.and(
//                    metadataSubject.isA(NEONE.LogisticsObjectMetadata),
                    metadataSubject.has(NEONE.describes, Rdf.iri(loIri)),
                    metadataSubject.has(NEONE.hasRevision, revision)
                ))
                .getQueryString();
        }
    }
}
