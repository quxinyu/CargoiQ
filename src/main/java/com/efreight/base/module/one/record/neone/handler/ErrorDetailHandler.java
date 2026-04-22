// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.handler;

import com.efreight.base.module.one.record.neone.iata.onerecord.API;
import com.efreight.base.module.one.record.neone.model.onerecord.ErrorDetail;
import com.efreight.base.module.one.record.neone.repository.ModelHandler;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.springframework.stereotype.Component;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@Component
@ApplicationScoped
public class ErrorDetailHandler extends ModelHandler<ErrorDetail> {

    @Override
    public ErrorDetail fromModel(IRI subject, Model model) {
        String code = getObjectLiteral(subject, API.hasCode, model).stringValue();
        Optional<String> message = findObjectLiteral(subject, API.hasMessage, model).map(Value::stringValue);
        Optional<IRI> property = findObject(subject, API.hasProperty, model);
        Optional<IRI> resource = findObject(subject, API.hasResource, model);

        return new ErrorDetail(subject, code, message, property, resource);
    }

    @Override
    public Model fromJava(ErrorDetail entity) {
        ModelBuilder modelBuilder = new ModelBuilder()
                .subject(entity.iri())
                .add(RDF.TYPE, API.ErrorDetail)
                .add(API.hasCode, entity.code());

        entity.message().ifPresent(message -> modelBuilder.add(API.hasMessage, message));
        entity.property().ifPresent(iri -> modelBuilder.add(API.hasProperty, iri));
        entity.resource().ifPresent(iri -> modelBuilder.add(API.hasResource, iri));

        return modelBuilder.build();
    }
}
