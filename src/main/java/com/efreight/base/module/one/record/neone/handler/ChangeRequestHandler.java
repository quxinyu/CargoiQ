// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.handler;

import com.efreight.base.module.one.record.neone.iata.onerecord.API;
import com.efreight.base.module.one.record.neone.model.onerecord.Change;
import com.efreight.base.module.one.record.neone.model.onerecord.ChangeRequest;
import com.efreight.base.module.one.record.neone.model.onerecord.Error;
import com.efreight.base.module.one.record.neone.model.onerecord.RequestStatus;
import com.efreight.base.module.one.record.neone.repository.ModelHandler;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.XMLSchema;
import org.springframework.stereotype.Component;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Instant;
import java.util.Optional;

@Component
@ApplicationScoped
public class ChangeRequestHandler extends ModelHandler<ChangeRequest> {

    private final ChangeHandler changeHandler;
    private final ErrorHandler errorHandler;

    @Inject
    public ChangeRequestHandler(ChangeHandler changeHandler, ErrorHandler errorHandler) {
        this.changeHandler = changeHandler;
        this.errorHandler = errorHandler;
    }

    @Override
    public ChangeRequest fromModel(IRI subject, Model model) {
        // ModelBodyHandler should have created new IRI and replaced bnodes.
        IRI requestedBy = getObject(subject, API.isRequestedBy, model);
        Instant requestedAt = getObjectLiteral(subject, API.isRequestedAt, model).calendarValue().toGregorianCalendar().toInstant();
        RequestStatus requestStatus = RequestStatus.from(getObject(subject, API.hasRequestStatus, model));
        Optional<IRI> revokedBy = findObject(subject, API.isRevokedBy, model);
        Optional<Instant> revokedAt = findObjectLiteral(subject, API.isRevokedAt, model).map(literal -> literal.calendarValue().toGregorianCalendar().toInstant());
        Optional<Error> error = findObject(subject, API.hasError, model).map(iri -> errorHandler.fromModel(iri, model));
        Change change = changeHandler.fromModel(getObject(subject, API.hasChange, model), model);

        return new ChangeRequest(subject, error, requestStatus, requestedAt, requestedBy,
            revokedAt, revokedBy, change);
    }

    @Override
    public Model fromJava(ChangeRequest entity) {
        ModelBuilder modelBuilder = new ModelBuilder()
            .subject(entity.iri())
            .add(RDF.TYPE, API.ChangeRequest)
            .add(API.isRequestedAt, Values.literal(entity.requestedAt().toString(), XMLSchema.DATETIME))
            .add(API.isRequestedBy, entity.requestedBy())
            .add(API.hasChange, entity.change().iri())
            .add(API.hasRequestStatus, entity.requestStatus().iri());
        entity.revokedBy().ifPresent(iri -> modelBuilder.add(API.isRevokedBy, iri));
        entity.revokedAt().ifPresent(instant -> modelBuilder.add(API.isRevokedAt, Values.literal(instant.toString(), XMLSchema.DATETIME)));

        Model model = modelBuilder.build();

        entity.error().ifPresent(error -> {
            model.add(entity.iri(), API.hasError, error.iri());
            model.addAll(errorHandler.fromJava(error));
        });

        return merge(model, changeHandler.fromJava(entity.change()));
    }
}
