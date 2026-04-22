//  Copyright Open Logistics Foundation
//
//  Licensed under the Open Logistics Foundation License 1.3.
//  For details on the licensing terms, see the LICENSE file.
//  SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.controller.onerecord;

import com.efreight.base.module.one.record.neone.annotations.Authenticated;
import com.efreight.base.module.one.record.neone.model.onerecord.AccessDelegation;
import com.efreight.base.module.one.record.neone.model.onerecord.AccessDelegationRequest;
import com.efreight.base.module.one.record.neone.service.onerecord.AccessDelegationService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.POST;
import javax.ws.rs.core.Response;
import java.net.URI;

@RestController
@RequestMapping(value = "/" + AccessDelegationsController.RESOURCE_NAME,
        consumes = {"application/ld+json", "application/x-turtle", "text/turtle"},
        produces = {"application/ld+json", "application/x-turtle", "text/turtle"})
public class AccessDelegationsController {

    public static final String RESOURCE_NAME = "v2/access-delegations";
    private final AccessDelegationService accessDelegationService;

    public AccessDelegationsController(AccessDelegationService accessDelegationService) {
        this.accessDelegationService = accessDelegationService;
    }

    @POST
    @Authenticated
    public Response createAccessDelegationRequest(AccessDelegation accessDelegation) {
        AccessDelegationRequest request = accessDelegationService.handleAccessDelegation(accessDelegation);
        URI uri = URI.create(request.iri().stringValue());
        return Response.created(uri).entity("Request for access delegation was successful").build();
    }
}
