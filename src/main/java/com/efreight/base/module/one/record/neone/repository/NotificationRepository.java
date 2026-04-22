// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.repository;

import com.efreight.base.module.one.record.neone.exception.NeoneException;
import com.efreight.base.module.one.record.neone.handler.NotificationHandler;
import com.efreight.base.module.one.record.neone.iata.onerecord.NEONE;
import com.efreight.base.module.one.record.neone.model.onerecord.IdProvider;
import com.efreight.base.module.one.record.neone.model.onerecord.Notification;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.sparqlbuilder.core.SparqlBuilder;
import org.eclipse.rdf4j.sparqlbuilder.core.Variable;
import org.eclipse.rdf4j.sparqlbuilder.graphpattern.GraphPatterns;
import org.eclipse.rdf4j.sparqlbuilder.rdf.Rdf;
import org.eclipse.rdf4j.sparqlbuilder.rdf.RdfObject;
import org.springframework.stereotype.Component;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Component
@ApplicationScoped
public class NotificationRepository extends ModelRepository<Notification> {

    @Inject
    public NotificationRepository(Repository repository, NotificationHandler handler, IdProvider idProvider) {
        super(repository, handler, idProvider);
    }

    public List<Notification> getPendingNotifications(RepositoryConnection connection) {
        Variable eventVar = SparqlBuilder.var("event");
        Variable notificationVar = SparqlBuilder.var("notification");
        Variable stateVar = SparqlBuilder.var("state");
        TupleQuery notificationsQuery = connection.prepareTupleQuery(
            Q.getNotificationsForEventsInState(eventVar, notificationVar, stateVar)
        );
        notificationsQuery.setBinding("state", NEONE.PENDING);
        final List<Notification> notifications = new ArrayList<>();
        try (TupleQueryResult result = notificationsQuery.evaluate()) {
            result.forEach(bindingSet -> {
                IRI notificationId = (IRI) bindingSet.getValue("notification");
                Notification notification = findByIri(notificationId, connection)
                    .orElseThrow(() -> new NeoneException("Missing notification " + notificationId));
                notifications.add(notification);
            });
        }
        return notifications;
    }

    public interface Q {
        static String getNotificationsForEventsInState(Variable event, Variable notification, Variable state) {
            return org.eclipse.rdf4j.sparqlbuilder.core.query.Queries.SELECT()
                .select(notification)
                .where(GraphPatterns.and(
                    event.isA(Rdf.iri(NEONE.NeoneEvent)),
                    event.has(NEONE.hasState, state),
                    event.has(NEONE.hasNotification, notification)
                )).getQueryString();
        }
    }
}
