// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.repository;

import com.efreight.base.common.core.exception.EftException;
import com.efreight.base.module.one.record.neone.handler.LogisticsEventHandler;
import com.efreight.base.module.one.record.neone.iata.onerecord.ACL;
import com.efreight.base.module.one.record.neone.iata.onerecord.CARGO;
import com.efreight.base.module.one.record.neone.model.onerecord.IdProvider;
import com.efreight.base.module.one.record.neone.model.onerecord.LogisticsEvent;
import com.efreight.base.module.one.record.neone.security.InternalAccessSubject;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.model.vocabulary.XMLSchema;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.sparqlbuilder.constraint.Expression;
import org.eclipse.rdf4j.sparqlbuilder.constraint.Expressions;
import org.eclipse.rdf4j.sparqlbuilder.core.SparqlBuilder;
import org.eclipse.rdf4j.sparqlbuilder.core.Variable;
import org.eclipse.rdf4j.sparqlbuilder.core.query.Queries;
import org.eclipse.rdf4j.sparqlbuilder.graphpattern.GraphPatterns;
import org.eclipse.rdf4j.sparqlbuilder.rdf.Rdf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@ApplicationScoped
public class LogisticsEventRepository extends ModelRepository<LogisticsEvent> {

    private static final Logger log = LoggerFactory.getLogger(LogisticsEventRepository.class);

    private final InternalAccessSubject accessSubject;

    @Inject
    public LogisticsEventRepository(Repository repository,
                                    LogisticsEventHandler handler,
                                    InternalAccessSubject accessSubject,
                                    IdProvider idProvider) {

        super(repository, handler, idProvider);
        this.accessSubject = accessSubject;
    }

    public List<LogisticsEvent> findEventsOfLogisticsObject(IRI lo,
                                                            Optional<String> eventType,
                                                            Optional<Instant> createdFrom,
                                                            Optional<Instant> createdUntil,
                                                            Optional<Instant> occurredFrom,
                                                            Optional<Instant> occurredUntil,
                                                            Integer limit,
                                                            RepositoryConnection connection) {

        List<LogisticsEvent> loEvents = new ArrayList<>();

        Variable varLoEvent = SparqlBuilder.var("loEvent");
        TupleQuery eventsQuery = getEventsOfLogisticsObject(lo, varLoEvent,
                eventType, createdFrom, createdUntil, occurredFrom, occurredUntil, limit, connection);

        try (TupleQueryResult result = eventsQuery.evaluate()) {
            result.forEach(bindingSet -> {
                Value subject = bindingSet.getValue("varLoEvent");
                if (subject.isIRI()) {
                    LogisticsEvent loEventIri = findByIri((IRI) subject, connection)
                            .orElseThrow(() -> new EftException("Cannot find logistics event " + subject));
                    loEvents.add(loEventIri);
                } else {
                    log.warn("Found invalid LogisticsEvent, subject is not an IRI, is [{}]", subject);
                }
            });
        }
        return loEvents;
    }

    private TupleQuery getEventsOfLogisticsObject(IRI lo,
                                                  Variable varEvent,
                                                  Optional<String> eventType,
                                                  Optional<Instant> createdFrom,
                                                  Optional<Instant> createdUntil,
                                                  Optional<Instant> occurredFrom,
                                                  Optional<Instant> occurredUntil,
                                                  Integer limit,
                                                  RepositoryConnection connection) {

        Variable varCreationDate = SparqlBuilder.var("creationDate");
        Variable varCreatedFrom = SparqlBuilder.var("createdFrom");
        Variable varCreatedUntil = SparqlBuilder.var("createdUntil");
        Variable varOccurredFrom = SparqlBuilder.var("occurredFrom");
        Variable varOccurredUntil = SparqlBuilder.var("occurredUntil");
        Variable varEventDate = SparqlBuilder.var("eventDate");
        Variable varAuthorization = SparqlBuilder.var("authorization");
        Variable varAgent = SparqlBuilder.var("agent");

        Optional<Expression> filterExpressionCreationDate = Optional.empty();
        if (createdFrom.isPresent() && createdUntil.isPresent()) {
            filterExpressionCreationDate = Optional.of(Expressions.and(Expressions.gte(varCreationDate, varCreatedFrom), Expressions.lte(varCreationDate, varCreatedUntil)));
        } else if (createdFrom.isPresent()) {
            filterExpressionCreationDate = Optional.of(Expressions.gte(varCreationDate, varCreatedFrom));
        } else if (createdUntil.isPresent()) {
            filterExpressionCreationDate = Optional.of(Expressions.lte(varCreationDate, varCreatedUntil));
        }

        Optional<Expression> filterExpressionEventDate = Optional.empty();
        if (occurredFrom.isPresent() && occurredUntil.isPresent()) {
            filterExpressionEventDate = Optional.of(Expressions.and(Expressions.gte(varEventDate, varOccurredFrom), Expressions.lte(varEventDate, varOccurredUntil)));
        } else if (occurredFrom.isPresent()) {
            filterExpressionEventDate = Optional.of(Expressions.gte(varEventDate, varOccurredFrom));
        } else if (occurredUntil.isPresent()) {
            filterExpressionEventDate = Optional.of(Expressions.lte(varEventDate, varOccurredUntil));
        }

        // 修复 JDK 1.8 兼容性问题：使用 GraphPatterns.and 替代 .andHas() 链式调用
        org.eclipse.rdf4j.sparqlbuilder.graphpattern.GraphPattern authPattern = GraphPatterns.and(
//                varAuthorization.isA(ACL.Authorization),
                varAuthorization.has(ACL.agent, varAgent),
                varAuthorization.has(ACL.accessTo, varEvent)
        );
        org.eclipse.rdf4j.sparqlbuilder.graphpattern.GraphPattern eventPattern = GraphPatterns.and(
//                varEvent.isA(CARGO.LogisticsEvent),
                varEvent.has(CARGO.eventFor, Rdf.iri(lo)),
                varEvent.has(CARGO.creationDate, varCreationDate),
                GraphPatterns.optional(varEvent.has(CARGO.eventDate, varEventDate)),
                authPattern
        );

//        eventType.ifPresent(type -> eventPattern.and(varEvent.has(CARGO.eventCode, type)));
        filterExpressionCreationDate.ifPresent(eventPattern::filter);
        filterExpressionEventDate.ifPresent(eventPattern::filter);

        String query = Queries.SELECT()
                .select(varEvent)
                .where(eventPattern)
                .orderBy(varCreationDate)
                .limit(limit)
                .getQueryString();

        TupleQuery tupleQuery = connection.prepareTupleQuery(query);
        tupleQuery.setBinding("varAgent", accessSubject.iri());
        createdFrom.map(i -> Values.literal(i.toString(), XMLSchema.DATETIME))
                .ifPresent(created -> tupleQuery.setBinding("varCreatedFrom", created));
        createdUntil.map(i -> Values.literal(i.toString(), XMLSchema.DATETIME))
                .ifPresent(created -> tupleQuery.setBinding("varCreatedUntil", created));
        occurredFrom.map(i -> Values.literal(i.toString(), XMLSchema.DATETIME))
                .ifPresent(occurred -> tupleQuery.setBinding("varOccurredFrom", occurred));
        occurredUntil.map(i -> Values.literal(i.toString(), XMLSchema.DATETIME))
                .ifPresent(occurred -> tupleQuery.setBinding("varOccurredUntil", occurred));

        return tupleQuery;
    }
}

/*
SELECT ?loEvent
WHERE {
    ?loEvent a <https://onerecord.iata.org/ns/cargo#LogisticsEvent> ;
        <https://onerecord.iata.org/ns/cargo#linkedObject> <http://localhost:8080/logistics-objects/21f3d7be-9cd8-4301-b017-28b808c664cf> ;
        <https://onerecord.iata.org/ns/cargo#creationDate> ?creationDate ;
        <https://onerecord.iata.org/ns/cargo#linkedObject> ?linkedObject .
    OPTIONAL { ?loEvent <https://onerecord.iata.org/ns/cargo#eventDate> ?eventDate . }
    ?authorization a <http://www.w3.org/ns/auth/acl#Authorization> ;
        <http://www.w3.org/ns/auth/acl#agent> ?agent ;
        <http://www.w3.org/ns/auth/acl#accessTo> ?linkedObject .
}
ORDER BY ?creationDate
*/
