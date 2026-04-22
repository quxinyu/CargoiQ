//  Copyright Open Logistics Foundation
//
//  Licensed under the Open Logistics Foundation License 1.3.
//  For details on the licensing terms, see the LICENSE file.
//  SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.handler;

import com.efreight.base.module.one.record.neone.iata.onerecord.API;
import com.efreight.base.module.one.record.neone.model.onerecord.AccessDelegation;
import com.efreight.base.module.one.record.neone.model.onerecord.AccessDelegationRequest;
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

import java.time.Instant;
import java.util.Optional;

@Component
public class AccessDelegationRequestHandler extends ModelHandler<AccessDelegationRequest> {

    private final ErrorHandler errorHandler;

    private final AccessDelegationHandler accessDelegationHandler;

    public AccessDelegationRequestHandler(ErrorHandler errorHandler,
                                          AccessDelegationHandler accessDelegationHandler) {
        this.errorHandler = errorHandler;
        this.accessDelegationHandler = accessDelegationHandler;
    }

    @Override
    public AccessDelegationRequest fromModel(IRI iri, Model model) {
        IRI accessDelegationIri = getObject(iri, API.hasAccessDelegation, model);
        RequestStatus requestStatus = RequestStatus.from(getObject(iri, API.hasRequestStatus, model));
        Instant requestedAt = Instant.parse(getObjectString(iri, API.isRequestedAt, model));
        IRI requestedBy = getObject(iri, API.isRequestedBy, model);
        Optional<Instant> revokedAt = findObjectString(iri, API.isRevokedAt, model).map(Instant::parse);
        Optional<IRI> revokedBy = findObject(iri, API.isRevokedBy, model);
        Optional<Error> error = findObject(iri, API.hasError, model).map(errorIri -> errorHandler.fromModel(errorIri, model));
        AccessDelegation accessDelegation = accessDelegationHandler.fromModel(accessDelegationIri, model);
        return new AccessDelegationRequest(iri, error, requestStatus, requestedAt, requestedBy, revokedAt, revokedBy,
                accessDelegation);
    }

    @Override
    public Model fromJava(AccessDelegationRequest entity) {
        ModelBuilder builder = new ModelBuilder()
                .subject(entity.iri())
                .add(RDF.TYPE, API.AccessDelegationRequest)
                .add(API.hasAccessDelegation, entity.getAccessDelegation().iri())
                .add(API.hasRequestStatus, entity.requestStatus().iri())
                .add(API.isRequestedAt, Values.literal(entity.requestedAt().toString(), XMLSchema.DATETIME))
                .add(API.isRequestedBy, entity.requestedBy());
        entity.revokedAt().ifPresent(instant ->
                builder.add(API.isRevokedAt, Values.literal(instant.toString(), XMLSchema.DATETIME)));
        entity.revokedBy().ifPresent(iri -> builder.add(API.isRevokedBy, iri));
        entity.error().ifPresent(e -> builder.add(API.hasError, e.iri()));

        Model rqModel = builder.build();
        Model accessDelegationModel = accessDelegationHandler.fromJava(entity.getAccessDelegation());
        return merge(rqModel, accessDelegationModel);
    }
}
