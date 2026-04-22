// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.controller.onerecord;

import com.efreight.base.module.one.record.neone.model.onerecord.IdProvider;
import com.efreight.base.module.one.record.neone.model.onerecord.RequestStatus;
import com.efreight.base.module.one.record.neone.security.AccessMode;
import com.efreight.base.module.one.record.neone.security.AccessObject;
import com.efreight.base.module.one.record.neone.security.AclSecured;
import com.efreight.base.module.one.record.neone.service.onerecord.ActionRequestService;
import com.efreight.base.module.one.record.neone.service.onerecord.NeoneId;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.rdf4j.model.Model;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Action Request Controller
 * Migrated from JAX-RS to Spring MVC
 */
@Slf4j
@RestController
@RequestMapping("/" + ActionRequestController.ACTION_REQUEST_RESOURCE_NAME)
public class ActionRequestController {

    public static final String ACTION_REQUEST_RESOURCE_NAME = "v2/action-requests";

    private final ActionRequestService actionRequestService;
    private final IdProvider idProvider;

    public ActionRequestController(ActionRequestService actionRequestService, IdProvider idProvider) {
        this.actionRequestService = actionRequestService;
        this.idProvider = idProvider;
    }

    @GetMapping("/{id}")
    @AclSecured(@AccessMode(AccessMode.READ))
    public ResponseEntity<?> getActionRequest(
            @AccessObject(AccessObject.Type.ACTION_REQUEST) @PathVariable("id") String internalId) {
        NeoneId requestId = idProvider.getActionRequestId(internalId);
        Model actionRequest = actionRequestService.getActionRequest(requestId.getIri());
        return ResponseEntity.ok(actionRequest);
    }

    @PatchMapping("{id}")
    public ResponseEntity<?> updateActionRequestStatus(@PathVariable("id") String id,
                                                       @RequestParam("status") String newStatus) {
        log.debug("Handling update on action request for LO [{}]. Status: [{}]", id, newStatus);
        actionRequestService.updateActionRequestStatus(idProvider.getActionRequestId(id).getIri(), RequestStatus.valueOf(newStatus));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    @AclSecured(@AccessMode(AccessMode.WRITE))
    public ResponseEntity<?> revokeActionRequest(
            @AccessObject(AccessObject.Type.ACTION_REQUEST) @PathVariable("id") String id) {
        NeoneId actionRequestId = idProvider.getActionRequestId(id);
        log.debug("Revoking action request for LO [{}]", actionRequestId);
//        actionRequestService.revokeActionRequest(actionRequestId.getIri());
        return ResponseEntity.noContent().build();
    }
}
