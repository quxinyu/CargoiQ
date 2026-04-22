//  Copyright Open Logistics Foundation
//
//  Licensed under the Open Logistics Foundation License 1.3.
//  For details on the licensing terms, see the LICENSE file.
//  SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.handler;

import com.efreight.base.module.one.record.neone.iata.onerecord.API;
import com.efreight.base.module.one.record.neone.iata.onerecord.NEONE;
import com.efreight.base.module.one.record.neone.model.onerecord.NeoneEvent;
import com.efreight.base.module.one.record.neone.repository.ModelHandler;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.springframework.stereotype.Component;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;
import java.util.Set;

@Component
@ApplicationScoped
public class NeoneEventHandler extends ModelHandler<NeoneEvent> {

    @Override
    public NeoneEvent fromModel(IRI iri, Model model) {
        IRI loId = getObject(iri, NEONE.referencesLogisticsObject, model);
        IRI type = getObject(iri, API.hasEventType, model);
        IRI state = getObject(iri, NEONE.hasState, model);
        Optional<IRI> triggerdBy = findObject(iri, API.isTriggeredBy, model);
        Set<IRI> changedProperties = findObjects(iri, API.hasChangedProperty, model);
        Set<IRI> loTypes = getObjects(iri, NEONE.loType, model);

        return new NeoneEvent(
                iri,
                loId,
                type,
                NeoneEvent.State.from(state),
                triggerdBy,
                changedProperties,
                loTypes);
    }

    @Override
    public Model fromJava(NeoneEvent entity) {
        ModelBuilder builder = new ModelBuilder();
        builder.subject(entity.iri())
                .add(RDF.TYPE, NEONE.NeoneEvent)
                .add(API.hasEventType, entity.notificationEventType())
                .add(NEONE.hasState, entity.state().getValue())
                .add(NEONE.referencesLogisticsObject, entity.loId());

        entity.loTypes().forEach(t -> builder.add(NEONE.loType, t));
        entity.triggeredBy().map(iri -> builder.add(API.isTriggeredBy, iri));
        entity.changedProperties().forEach(p -> builder.add(API.hasChangedProperty, p));

        return builder.build();
    }
}
