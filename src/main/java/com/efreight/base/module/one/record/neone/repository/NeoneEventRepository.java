// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.repository;


import com.efreight.base.module.one.record.neone.handler.NeoneEventHandler;
import com.efreight.base.module.one.record.neone.iata.onerecord.NEONE;
import com.efreight.base.module.one.record.neone.model.onerecord.IdProvider;
import com.efreight.base.module.one.record.neone.model.onerecord.NeoneEvent;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.sparqlbuilder.core.SparqlBuilder;
import org.eclipse.rdf4j.sparqlbuilder.core.Variable;
import org.eclipse.rdf4j.sparqlbuilder.graphpattern.GraphPatterns;
import org.eclipse.rdf4j.sparqlbuilder.rdf.Rdf;
import org.springframework.stereotype.Component;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@ApplicationScoped
public class NeoneEventRepository extends ModelRepository<NeoneEvent> {

    @Inject
    public NeoneEventRepository(Repository repository, NeoneEventHandler handler, IdProvider idProvider) {
        super(repository, handler, idProvider);
    }

    public void setEventState(IRI eventId, NeoneEvent.State state, RepositoryConnection connection) {
        connection.remove(eventId, NEONE.hasState, null);
        connection.add(eventId, NEONE.hasState, state.getValue());
    }

    public void addNotification(IRI eventId, IRI notificationId, RepositoryConnection connection) {
        connection.add(eventId, NEONE.hasNotification, notificationId);
    }

    public void deleteNotification(IRI eventId, IRI notificationId, RepositoryConnection connection) {
        connection.remove(eventId, NEONE.hasNotification, notificationId);
    }

    public List<IRI> getPendingEventsWithoutNotifications(RepositoryConnection connection) {
        Variable eventVar = SparqlBuilder.var("event");
        try (TupleQueryResult result = connection.prepareTupleQuery(
                Q.getPendingEventsWithoutNotifications(eventVar)).evaluate()) {

            return result.stream()
                    .map(bindingSet -> (IRI) bindingSet.getValue("eventVar"))
                    .collect(Collectors.toList());
        }
    }

    public NeoneEvent addEvent(IRI loId, IRI eventType, Optional<IRI> triggeredBy,
                               Set<IRI> changedProperties, Set<IRI> loTypes, RepositoryConnection connection) {

        NeoneEvent event = new NeoneEvent(idProvider.createLoEventUri(loId).getIri(), loId,
                eventType, NeoneEvent.State.NEW, triggeredBy, changedProperties, loTypes);

        persist(event, connection);

        return event;
    }

    public List<NeoneEvent> getNewEvents(RepositoryConnection connection) {
        Variable eventVar = SparqlBuilder.var("event");
        Variable stateVar = SparqlBuilder.var("state");

        TupleQuery query = connection.prepareTupleQuery(Q.getEventsInState(eventVar, stateVar));
        query.setBinding("stateVar", NeoneEvent.State.NEW.getValue());
        try (TupleQueryResult result = query.evaluate()) {
            return result.stream().map(bindingSet -> {
                        IRI eventIri = (IRI) bindingSet.getValue("eventVar");
                        return findByIri(eventIri, connection);
                    })
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
        }
    }

    public interface Q {
        static String getPendingEventsWithoutNotifications(Variable eventVar) {
            Variable notificationVar = SparqlBuilder.var("x");

            // 修复 JDK 1.8 兼容性问题：使用 GraphPatterns.and 替代 .andHas() 链式调用
            return org.eclipse.rdf4j.sparqlbuilder.core.query.Queries.SELECT()
                    .select(eventVar)
                    .where(GraphPatterns.and(
//                            eventVar.isA(NEONE.NeoneEvent),
                            eventVar.has(NEONE.hasState, Rdf.iri(NEONE.PENDING))
                    ).filterNotExists(eventVar.has(NEONE.hasNotification, notificationVar)))
                    .getQueryString();
        }

        static String getEventsInState(Variable eventVar, Variable stateVar) {
            // 修复 JDK 1.8 兼容性问题：使用 GraphPatterns.and 替代 .andHas() 链式调用
            return org.eclipse.rdf4j.sparqlbuilder.core.query.Queries.SELECT()
                    .select(eventVar)
                    .where(GraphPatterns.and(
//                            eventVar.isA(NEONE.NeoneEvent),
                            eventVar.has(NEONE.hasState, stateVar)
                    )).getQueryString();
        }
    }
}
