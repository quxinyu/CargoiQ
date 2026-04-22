// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.controller.onerecord;

import com.efreight.base.module.one.record.neone.exception.InvalidApiRequestException;
import com.efreight.base.module.one.record.neone.model.onerecord.AuditTrail;
import com.efreight.base.module.one.record.neone.security.AccessMode;
import com.efreight.base.module.one.record.neone.security.AccessObject;
import com.efreight.base.module.one.record.neone.security.AclSecured;
import com.efreight.base.module.one.record.neone.service.onerecord.AuditTrailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

/**
 * Audit Trail Controller
 * Migrated from JAX-RS to Spring MVC
 */
@RestController
@RequestMapping(value = "/logistics-objects/{id}/" + AuditTrailController.AUDIT_TRAIL_RESOURCE_NAME,
        produces = {"application/ld+json", "application/x-turtle", "text/turtle"})
public class AuditTrailController {

    public static final String AUDIT_TRAIL_RESOURCE_NAME = "audit-trail";

    private static final Logger log = LoggerFactory.getLogger(AuditTrailController.class);

    private final AuditTrailService auditTrailService;

    public AuditTrailController(AuditTrailService auditTrailService) {
        this.auditTrailService = auditTrailService;
    }

    @GetMapping
    @AclSecured(@AccessMode(AccessMode.READ))
    public AuditTrail getAuditTrail(@AccessObject @PathVariable("id") String loId,
                                    @RequestParam(name = "updated-from", required = false) String updatedFrom,
                                    @RequestParam(name = "updated-to", required = false) String updatedTo) {

        log.info("Audit trail requested for LO [{}] and time range from [{}] until [{}]", loId, updatedFrom, updatedTo);

        Instant from = DateTimeConverter.convert(updatedFrom);
        Instant until = DateTimeConverter.convert(updatedTo);
        if (from != null && until != null && from.isAfter(until)) {
            throw new InvalidApiRequestException("invalid time range");
        }

        return auditTrailService.getAuditTrailByLoId(loId, from, until);
    }
}
