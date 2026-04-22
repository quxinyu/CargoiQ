//  Copyright Open Logistics Foundation
//
//  Licensed under the Open Logistics Foundation License 1.3.
//  For details on the licensing terms, see the LICENSE file.
//  SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.repository;

import com.efreight.base.module.one.record.neone.handler.SubscriptionRequestHandler;
import com.efreight.base.module.one.record.neone.iata.onerecord.API;
import com.efreight.base.module.one.record.neone.model.onerecord.IdProvider;
import com.efreight.base.module.one.record.neone.model.onerecord.SubscriptionRequest;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.repository.Repository;
import org.springframework.stereotype.Component;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Component
@ApplicationScoped
public class SubscriptionRequestRepository extends ActionRequestRepository<SubscriptionRequest> {

    @Inject
    public SubscriptionRequestRepository(Repository repository,
                                         SubscriptionRequestHandler modelHandler,
                                         IdProvider idProvider) {
        super(repository, modelHandler, idProvider);
    }

    @Override
    public IRI getRepositoryType() {
        return API.SubscriptionRequest;
    }
}
