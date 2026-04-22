//  Copyright Open Logistics Foundation
//
//  Licensed under the Open Logistics Foundation License 1.3.
//  For details on the licensing terms, see the LICENSE file.
//  SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.handler;

import com.efreight.base.module.one.record.neone.iata.onerecord.API;
import com.efreight.base.module.one.record.neone.model.onerecord.Notification;
import com.efreight.base.module.one.record.neone.repository.ModelHandler;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.XMLSchema;
import org.springframework.stereotype.Component;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;
import java.util.Set;

@Component
@ApplicationScoped
public class NotificationHandler extends ModelHandler<Notification> {

    @Override
    public Notification fromModel(IRI iri, Model model) {
        IRI eventType = getObject(iri, API.hasEventType, model);
        Optional<IRI> loIri = findObject(iri, API.hasLogisticsObject, model);
        Optional<String> loType = findObjectUri(iri, API.hasLogisticsObjectType, model);
        Optional<IRI> triggeredBy = findObject(iri, API.isTriggeredBy, model);
        Set<IRI> changedProps = findObjects(iri, API.hasChangedProperty, model);
        return new Notification(iri, eventType, loType, loIri, triggeredBy, changedProps);
    }

    @Override
    public Model fromJava(Notification entity) {
        ModelBuilder builder = new ModelBuilder();
        builder.subject(entity.iri())
                .add(RDF.TYPE, API.Notification)
                .add(API.hasEventType, entity.eventType());
        entity.hasLogisticsObjectType().ifPresent(type ->
                builder.add(API.hasLogisticsObjectType, Values.literal(type, XMLSchema.ANYURI))
        );
        entity.hasLogisticsObject().ifPresent(p -> builder.add(API.hasLogisticsObject, p));
        entity.triggeredBy().ifPresent(t -> builder.add(API.isTriggeredBy, t));
        entity.changedProperties().forEach(p -> builder.add(API.hasChangedProperty, p));
        return builder.build();
    }
}
