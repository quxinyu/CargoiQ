package com.efreight.base.module.one.record.neone.controller.api;

import com.efreight.base.module.one.record.neone.constants.BaseConstants;
import com.efreight.base.module.one.record.neone.enums.FromType;
import com.efreight.base.module.one.record.neone.helper.IriGenerator;
import com.efreight.base.module.one.record.neone.service.NeOneLogisticsEventsService;
import com.efreight.base.module.one.record.neone.service.NeOneLogisticsObjectsService;
import com.efreight.base.module.one.record.neone.utils.UUIDTools;
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
@RequestMapping("/api/logistics-objects")
public class NeOneLogisticsObjectsApi {
    private final IriGenerator iriGenerator;
    private final NeOneLogisticsObjectsService logisticsObjectsService;

    @PostMapping(consumes = {BaseConstants.CONTENT_TYPE_LD_JSON, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> createLogisticsObject(@RequestBody String oneRecordBody) {
        String uuid = UUIDTools.generateSimpleUUID();
        String iri = iriGenerator.generateLogisticsObjectLoId(uuid);
        logisticsObjectsService.createResolveBody(iri, oneRecordBody, FromType.CREATE_SELF);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, iri);
        return ResponseEntity
                .status(201)
                .headers(headers)
                .build();
    }

}
