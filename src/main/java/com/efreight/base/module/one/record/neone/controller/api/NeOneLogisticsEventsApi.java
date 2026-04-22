package com.efreight.base.module.one.record.neone.controller.api;

import com.efreight.base.module.one.record.neone.constants.BaseConstants;
import com.efreight.base.module.one.record.neone.service.NeOneLogisticsEventsService;
import com.efreight.base.module.one.record.neone.utils.ResponseEntityBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author quxinyu
 * @since 2025-11-7
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/logistics-events")
public class NeOneLogisticsEventsApi {
    private final NeOneLogisticsEventsService logisticsObjectsEventService;

    @PostMapping(consumes = {BaseConstants.CONTENT_TYPE_LD_JSON, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> addEvent(@RequestBody String oneRecordBody) {
        String iri = this.logisticsObjectsEventService.create(oneRecordBody);
        return ResponseEntityBuilder.create(201).header(HttpHeaders.LOCATION, iri).build();
    }

}
