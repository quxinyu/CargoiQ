// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.handler;

import com.efreight.base.module.one.record.neone.iata.onerecord.API;
import com.efreight.base.module.one.record.neone.model.onerecord.Error;
import com.efreight.base.module.one.record.neone.model.onerecord.ErrorDetail;
import com.efreight.base.module.one.record.neone.repository.ModelHandler;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.springframework.stereotype.Component;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@ApplicationScoped
public class ErrorHandler extends ModelHandler<Error> {

    private final ErrorDetailHandler errorDetailHandler;

    @Inject
    public ErrorHandler(ErrorDetailHandler errorDetailHandler) {
        this.errorDetailHandler = errorDetailHandler;
    }

    @Override
    public Error fromModel(IRI subject, Model model) {
        String title = getObjectLiteral(subject, API.hasTitle, model).stringValue();
        Set<ErrorDetail> errorDetails = findObjects(subject, API.hasErrorDetail, model).stream()
                .map(iri -> errorDetailHandler.fromModel(iri, model))
                .collect(Collectors.toSet());

        return new Error(subject, title, errorDetails);
    }

    @Override
    public Model fromJava(Error entity) {
        ModelBuilder modelBuilder = new ModelBuilder()
                .subject(entity.iri())
                .add(RDF.TYPE, API.Error)
                .add(API.hasTitle, entity.title());

        entity.errorDetail().forEach(errorDetail -> modelBuilder.add(API.hasErrorDetail, errorDetail.iri()));

        Model[] errorDetailModels = entity.errorDetail().stream()
                .map(errorDetailHandler::fromJava)
                .toArray(Model[]::new);

        return merge(modelBuilder.build(), errorDetailModels);
    }
}
