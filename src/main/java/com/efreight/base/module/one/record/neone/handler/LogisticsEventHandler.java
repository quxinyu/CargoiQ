//  Copyright Open Logistics Foundation
//
//  Licensed under the Open Logistics Foundation License 1.3.
//  For details on the licensing terms, see the LICENSE file.
//  SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.handler;

import com.efreight.base.module.one.record.neone.iata.onerecord.CARGO;
import com.efreight.base.module.one.record.neone.model.onerecord.LogisticsEvent;
import com.efreight.base.module.one.record.neone.repository.ModelHandler;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.DynamicModelFactory;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.XMLSchema;
import org.springframework.stereotype.Component;

import javax.enterprise.context.ApplicationScoped;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;

@Component
@ApplicationScoped
public class LogisticsEventHandler extends ModelHandler<LogisticsEvent> {

    @Override
    public LogisticsEvent fromModel(IRI iri, Model model) {

        Optional<Instant> creationDate = findObjectString(iri, CARGO.creationDate, model).map(Instant::parse);
        Optional<Instant> eventDate = findObjectString(iri, CARGO.eventDate, model).map(Instant::parse);
        Optional<IRI> eventCode = findObject(iri, CARGO.eventCode, model);
        Optional<String> eventName = findObjectString(iri, CARGO.eventName, model);
        Optional<IRI> eventTimeType = findObject(iri, CARGO.eventTimeType, model);
        Set<IRI> externalReferences = findObjects(iri, CARGO.externalReferences, model);
        Optional<IRI> eventFor = findObject(iri, CARGO.eventFor, model);
        Optional<IRI> eventLocation = findObject(iri, CARGO.eventLocation, model);
        Optional<IRI> recordingOrganization = findObject(iri, CARGO.recordingOrganization, model);
        Optional<IRI> recordingActor = findObject(iri, CARGO.recordingActor, model);
        Optional<Boolean> partialEventIndicator = findObjectLiteral(iri, CARGO.partialEventIndicator, model)
                .map(Literal::booleanValue);


        // check for embedded event code list element
        Optional<Model> embeddedEventCodeModel;
        if (eventCode.isPresent() && model.contains(eventCode.get(), null, null)) {
            embeddedEventCodeModel = Optional.of(model.filter(eventCode.get(), null, null));
        } else {
            embeddedEventCodeModel = Optional.empty();
        }

        // embedded lo model never gets sent to the API so for adding an embedded LO model use
        // the corresponding LogisticsEvent wither method to add an embedded LO model
        Optional<Model> embeddedLo = Optional.empty();
        return new LogisticsEvent(iri, creationDate, eventDate, eventCode, eventName,
                eventTimeType, externalReferences, eventFor, eventLocation,
                recordingOrganization, recordingActor, embeddedLo, embeddedEventCodeModel, partialEventIndicator);
    }

    @Override
    public Model fromJava(LogisticsEvent entity) {
        ModelBuilder builder = new ModelBuilder()
                .subject(entity.iri())
                .add(RDF.TYPE, CARGO.LogisticsEvent);

        entity.creationDate().ifPresent(ts ->
                builder.add(CARGO.creationDate, Values.literal(ts.toString(), XMLSchema.DATETIME))
        );
        entity.eventDate().ifPresent(ts ->
                builder.add(CARGO.eventDate, Values.literal(ts.toString(), XMLSchema.DATETIME))
        );
        entity.eventCode().ifPresent(code -> builder.add(CARGO.eventCode, code));
        entity.eventName().ifPresent(name -> builder.add(CARGO.eventName, name));
        entity.eventTimeType().ifPresent(type -> builder.add(CARGO.eventTimeType, type));
        entity.externalReferences().forEach(ref -> builder.add(CARGO.externalReferences, ref));
        entity.eventFor().ifPresent(lo -> builder.add(CARGO.eventFor, lo));
        entity.eventLocation().ifPresent(l -> builder.add(CARGO.eventLocation, l));
        entity.recordingOrganization().ifPresent(p -> builder.add(CARGO.recordingOrganization, p));
        entity.recordingActor().ifPresent(p -> builder.add(CARGO.recordingActor, p));
        entity.partialEventIndicator().ifPresent(partialEventIndicator ->
                builder.add(CARGO.partialEventIndicator, Values.literal(partialEventIndicator))
        );
        Model model = builder.build();

        Model loModel = entity.embeddedLo().orElse(new DynamicModelFactory().createEmptyModel());
        Model embeddedEventCodeModel = entity.embeddedEventCodeModel()
                .orElse(new DynamicModelFactory().createEmptyModel());

        // if a LO is embedded, merge the LOs model into the event model
        return merge(model, loModel, embeddedEventCodeModel);
    }
}
