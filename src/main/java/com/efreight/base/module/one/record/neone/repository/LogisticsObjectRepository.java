// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.repository;

import com.efreight.base.module.one.record.neone.exception.MissingLogisticsObjectTypeException;
import com.efreight.base.module.one.record.neone.handler.LogisticsObjectHandler;
import com.efreight.base.module.one.record.neone.iata.onerecord.ACL;
import com.efreight.base.module.one.record.neone.iata.onerecord.NEONE;
import com.efreight.base.module.one.record.neone.model.onerecord.IdProvider;
import com.efreight.base.module.one.record.neone.model.onerecord.LogisticsObject;
import com.efreight.base.module.one.record.neone.service.onerecord.NeoneId;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.impl.DynamicModelFactory;
import org.eclipse.rdf4j.model.util.Models;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryResults;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.sparqlbuilder.constraint.Expressions;
import org.eclipse.rdf4j.sparqlbuilder.core.Assignment;
import org.eclipse.rdf4j.sparqlbuilder.core.SparqlBuilder;
import org.eclipse.rdf4j.sparqlbuilder.core.Variable;
import org.eclipse.rdf4j.sparqlbuilder.core.query.Queries;
import org.eclipse.rdf4j.sparqlbuilder.graphpattern.GraphPatterns;
import org.eclipse.rdf4j.sparqlbuilder.graphpattern.SubSelect;
import org.springframework.stereotype.Component;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

@Component
@ApplicationScoped
public class LogisticsObjectRepository extends ModelRepository<LogisticsObject> {

    private final AclAuthorizationRepository authorizationRepository;

    @Inject
    public LogisticsObjectRepository(Repository repository,
                                     LogisticsObjectHandler modelHandler,
                                     IdProvider idProvider,
                                     AclAuthorizationRepository authorizationRepository) {
        super(repository, modelHandler, idProvider);
        this.authorizationRepository = authorizationRepository;
    }

    public Set<IRI> getLogisticsObjectTypes(IRI iri, RepositoryConnection connection) {
        Set<IRI> types = Models.objectIRIs(QueryResults.asModel(connection.getStatements(iri, RDF.TYPE, null)));
        if (types.isEmpty()) {
            throw new MissingLogisticsObjectTypeException(iri);
        }
        return types;
    }

    public boolean hasType(IRI subject, IRI type, RepositoryConnection connection) {
        return Models.objectIRI(QueryResults.asModel(connection.getStatements(subject, RDF.TYPE, type)))
                .isPresent();
    }

    public Optional<LogisticsObject> findGraphByIri(IRI iri, IRI accessSubject, RepositoryConnection connection) {
        Predicate<IRI> continueIf = subject -> {
            NeoneId subjectId = idProvider.parse(subject);
            if (!subjectId.isInternal() && subjectId.isLocal()) {
                return authorizationRepository.aclExists(accessSubject, subject, ACL.Read, connection);
            } else {
                return true;
            }
        };
        return findRecursively(iri, continueIf, connection);
    }

    public Model getLogisticsObjects(int limit, int offset, IRI loType, RepositoryConnection connection) {
        Variable subjectVar = SparqlBuilder.var("s");
        Variable predicateVar = SparqlBuilder.var("p");
        Variable objectVar = SparqlBuilder.var("o");
        Variable countVar = SparqlBuilder.var("count");
        Variable typeVar = SparqlBuilder.var("type");

        Model model = new DynamicModelFactory().createEmptyModel();

        TupleQuery countQuery = connection.prepareTupleQuery(Q.countLogisticsObjects(countVar, typeVar));
        countQuery.setBinding("typeVar", loType);

        try (TupleQueryResult result = countQuery.evaluate()) {
            if (result.hasNext()) {
                BindingSet bindingSet = result.next();
                Value countValue = bindingSet.getValue("countVar");
                model.add(Values.bnode("tmp"), NEONE.hasTotalCount, countValue);
            }
        }

        TupleQuery loQuery = connection.prepareTupleQuery(
                Q.findLogisticsObjects(subjectVar, predicateVar, objectVar, limit, offset, typeVar)
        );

        loQuery.setBinding("typeVar", loType);

        try (TupleQueryResult result = loQuery.evaluate()) {

            while (result.hasNext()) {
                BindingSet bindingSet = result.next();
                Resource subject = (Resource) bindingSet.getValue("subjectVar");
                IRI predicate = (IRI) bindingSet.getValue("predicateVar");
                Value object = bindingSet.getValue("objectVar");

                model.add(subject, predicate, object);
            }

        }
        return model;
    }

    interface Q {

        static String findLogisticsObjects(Variable subjectVar, Variable predicateVar, Variable objectVar,
                                           int limit, int offset, Variable typeVar) {

            SubSelect logisticsObjects = GraphPatterns.select(subjectVar, predicateVar, objectVar)
                    .where(subjectVar
                            .isA(typeVar)
                    )
                    .limit(limit)
                    .offset(offset)
                    .orderBy(subjectVar);

            return Queries.SELECT()
                    .select(subjectVar, predicateVar, objectVar)
                    .where(subjectVar
                            .has(predicateVar, objectVar)
                            .and(logisticsObjects)
                    )
                    .orderBy(subjectVar)
                    .getQueryString();
        }

        static String countLogisticsObjects(Variable countVar, Variable typeVar) {
            Variable subjectVar = SparqlBuilder.var("s");
            Assignment count = Expressions.count(subjectVar).as(countVar);

            return Queries.SELECT(count)
                    .where(subjectVar
                            .isA(typeVar)
                    )
                    .getQueryString();
        }

    }
}
