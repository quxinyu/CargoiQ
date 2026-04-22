//  Copyright Open Logistics Foundation
//
//  Licensed under the Open Logistics Foundation License 1.3.
//  For details on the licensing terms, see the LICENSE file.
//  SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.client;

//import jakarta.ws.rs.Consumes;
//import jakarta.ws.rs.POST;
//import jakarta.ws.rs.Path;
//import jakarta.ws.rs.Produces;
import org.eclipse.rdf4j.model.Model;

public interface ActionRequestClient {

    /**
     * Query some external system for the status the posted actionRequest should be in, e.g.
     * ask if a pending ActionRequest is ACCEPTED or REFUSED.
     *
     * @param actionRequest object whose status is required.
     * @return action request status
     */
//    @POST
//    @Path("/actionrequest")
//    @Consumes({"application/ld+json"})
//    @Produces({"application/json"})
    ActionRequestEvaluationResponse evaluate(Model actionRequest);
}
