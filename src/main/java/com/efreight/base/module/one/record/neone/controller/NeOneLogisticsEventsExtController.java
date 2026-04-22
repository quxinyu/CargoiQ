package com.efreight.base.module.one.record.neone.controller;

import com.efreight.base.module.one.record.neone.annotations.Authenticated;
import com.efreight.base.module.one.record.neone.constants.BaseConstants;
import com.efreight.base.module.one.record.neone.model.entity.NeOneLogisticsEvents;
import com.efreight.base.module.one.record.neone.service.NeOneLogisticsEventsService;
import com.efreight.base.module.one.record.neone.utils.ResponseEntityBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author fu yuan hui
 * @since 2024-09-19 15:10:59 星期四
 *
 * 物流事件的拓展接口
 */
@Authenticated
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/logistics-events")
@RestController
public class NeOneLogisticsEventsExtController {

    private final NeOneLogisticsEventsService logisticsObjectsEventService;

    @PostMapping
    public ResponseEntity<?> addEvents(@RequestBody String eventBody) {

        String iri = this.logisticsObjectsEventService.create(eventBody);

        return ResponseEntityBuilder.create(201).header(HttpHeaders.LOCATION, iri).build();
    }

    @GetMapping(value = "{id}", produces = BaseConstants.CONTENT_TYPE_LD_JSON)
    public ResponseEntity<?> getLogisticsEvents(@PathVariable String id) {
        NeOneLogisticsEvents event = logisticsObjectsEventService.getByEventId(id);

        return ResponseEntityBuilder.ok()
                .header("Location", event.getEventIri())
                .header("Type", "https://onerecord.iata.org/ns/cargo#LogisticsEvent")
                .header("Revision", "1")
                .header("Latest-Revision", "1")
                .body(event.getBodyText());
    }
}
