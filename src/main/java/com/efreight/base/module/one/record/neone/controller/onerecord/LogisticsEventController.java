// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.controller.onerecord;

import com.efreight.base.module.one.record.neone.annotations.Authenticated;
import com.efreight.base.module.one.record.neone.config.LogisticsEventHttpMessageConverter;
import com.efreight.base.module.one.record.neone.constants.BaseConstants;
import com.efreight.base.module.one.record.neone.model.onerecord.IdProvider;
import com.efreight.base.module.one.record.neone.model.onerecord.LogisticsEvent;
import com.efreight.base.module.one.record.neone.security.AccessMode;
import com.efreight.base.module.one.record.neone.security.AccessObject;
import com.efreight.base.module.one.record.neone.security.AclSecured;
import com.efreight.base.module.one.record.neone.service.onerecord.LogisticsEventService;
import com.efreight.base.module.one.record.neone.service.onerecord.NeoneId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.Instant;

/**
 * 国际航协ONERECORD逻辑
 *
 * @author quxinyu
 * @since 2026-01-12
 */
@Slf4j
@Authenticated
@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/logistics-objects/{loId}/" + LogisticsEventController.LOGISTICS_EVENT_RESOURCE_NAME)
public class LogisticsEventController {

    public static final String LOGISTICS_EVENT_RESOURCE_NAME = "logistics-events";

    private final IdProvider idProvider;

    private final LogisticsEventService logisticsEventService;

//    private final Context context;


    @PostMapping(consumes = {
            LogisticsEventHttpMessageConverter.APPLICATION_LD_JSON,
            LogisticsEventHttpMessageConverter.APPLICATION_X_TURTLE,
            LogisticsEventHttpMessageConverter.TEXT_TURTLE,
            MediaType.APPLICATION_JSON_VALUE
    })
    public ResponseEntity<?> addEvent(@PathVariable("loId") String id, @RequestBody LogisticsEvent event) {
        NeoneId loId = idProvider.getUriForLoId(id);
        LogisticsEvent createdEvent = logisticsEventService.addLogisticsEvent(loId, event);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(createdEvent.iri().stringValue()));
        return ResponseEntity.status(201).headers(headers).build();
    }

    @AclSecured(@AccessMode(AccessMode.READ))
    @GetMapping(value = "/{eventId}", produces = BaseConstants.CONTENT_TYPE_LD_JSON)
    public LogisticsEvent getEvent(@AccessObject @PathVariable("loId") String loId,
                                   @AccessObject(AccessObject.Type.LOGISTICS_EVENT_ID) @PathVariable("eventId") String eventId,
                                   @RequestParam(value = "embedded", defaultValue = "false") boolean embedded,
                                   @RequestParam(value = "at", required = false) String loAt) {

        NeoneId eventUri = idProvider.getUriForEventId(loId, eventId);
        Instant at = DateTimeConverter.convert(loAt);
        return logisticsEventService.getLogisticsEvent(eventUri, embedded, at);
    }


//    @RequestScope
//    public static class Context {
//
//        private NeoneId loId;
//
//        public NeoneId getLoId() {
//            return loId;
//        }
//
//        public void setLoId(NeoneId loId) {
//            this.loId = loId;
//        }
//    }
}
