//  Copyright Open Logistics Foundation
//
//  Licensed under the Open Logistics Foundation License 1.3.
//  For details on the licensing terms, see the LICENSE file.
//  SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.handler;

import com.efreight.base.module.one.record.neone.iata.onerecord.API;
import com.efreight.base.module.one.record.neone.model.onerecord.Error;
import com.efreight.base.module.one.record.neone.model.onerecord.RequestStatus;
import com.efreight.base.module.one.record.neone.model.onerecord.Subscription;
import com.efreight.base.module.one.record.neone.model.onerecord.SubscriptionRequest;
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
public class SubscriptionRequestHandler extends ModelHandler<SubscriptionRequest> {

    private final ErrorHandler errorHandler;

    private final SubscriptionHandler subscriptionHandler;

    @Inject
    public SubscriptionRequestHandler(ErrorHandler errorHandler,
                                      SubscriptionHandler subscriptionHandler) {
        this.errorHandler = errorHandler;
        this.subscriptionHandler = subscriptionHandler;
    }

    @Override
    public SubscriptionRequest fromModel(IRI iri, Model model) {
        IRI subscriptionIri = getObject(iri, API.hasSubscription, model);
        RequestStatus requestStatus = RequestStatus.from(getObject(iri, API.hasRequestStatus, model));
        Instant requestedAt = Instant.parse(getObjectString(iri, API.isRequestedAt, model));
        IRI requestedBy = getObject(iri, API.isRequestedBy, model);
        Optional<Instant> revokedAt = findObjectString(iri, API.isRevokedAt, model).map(Instant::parse);
        Optional<IRI> revokedBy = findObject(iri, API.isRevokedBy, model);
        Optional<Error> error = findObject(iri, API.hasError, model).map(errorIri -> errorHandler.fromModel(errorIri, model));
        Subscription subscription = subscriptionHandler.fromModel(subscriptionIri, model);

        return new SubscriptionRequest(iri, requestStatus, requestedAt, requestedBy, revokedAt, revokedBy,
                error, subscription);
    }

    @Override
    public Model fromJava(SubscriptionRequest entity) {
        ModelBuilder builder = new ModelBuilder()
                .subject(entity.iri())
                .add(RDF.TYPE, API.SubscriptionRequest)
                .add(API.hasSubscription, entity.subscription().iri())
                .add(API.hasRequestStatus, entity.requestStatus().iri())
                .add(API.isRequestedAt, Values.literal(entity.requestedAt().toString(), XMLSchema.DATETIME))
                .add(API.isRequestedBy, entity.requestedBy());
        entity.revokedAt().ifPresent(instant ->
                builder.add(API.isRevokedAt, Values.literal(instant.toString(), XMLSchema.DATETIME)));
        entity.revokedBy().ifPresent(iri -> builder.add(API.isRevokedBy, iri));
        entity.error().ifPresent(e -> builder.add(API.hasError, e.iri()));

        Model subRqModel = builder.build();
        Model subsriptionModel = subscriptionHandler.fromJava(entity.subscription());

        return merge(subRqModel, subsriptionModel);
    }
}
