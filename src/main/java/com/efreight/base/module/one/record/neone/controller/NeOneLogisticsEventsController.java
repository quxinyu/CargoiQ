package com.efreight.base.module.one.record.neone.controller;

import com.efreight.base.module.one.record.neone.annotations.Authenticated;
import com.efreight.base.module.one.record.neone.constants.BaseConstants;
import com.efreight.base.module.one.record.neone.ex.LogisticsObjectException;
import com.efreight.base.module.one.record.neone.helper.IriGenerator;
import com.efreight.base.module.one.record.neone.model.entity.NeOneLogisticsEvents;
import com.efreight.base.module.one.record.neone.model.vo.LogisticsEventsRequestVO;
import com.efreight.base.module.one.record.neone.service.NeOneLogisticsEventsService;
import com.efreight.base.module.one.record.neone.utils.ResponseEntityBuilder;
import com.efreight.base.module.one.record.neone.utils.UUIDTools;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author fu yuan hui
 * @since 2024-08-14 15:45:13 星期三
 */
@Slf4j
@Authenticated
@RestController
@RequiredArgsConstructor
@RequestMapping("/logistics-objects/{loId}/" + NeOneLogisticsEventsController.LOGISTICS_EVENT_RESOURCE_NAME)
public class NeOneLogisticsEventsController {

    private final IriGenerator iriGenerator;

    public static final String LOGISTICS_EVENT_RESOURCE_NAME = "logistics-events";

    private final NeOneLogisticsEventsService logisticsObjectsEventService;


    @PostMapping(consumes = {BaseConstants.CONTENT_TYPE_LD_JSON, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> addEvent(@PathVariable String loId, @RequestBody String oneRecordBody) {
        String uuid = UUIDTools.generateSimpleUUID();
        String iri = this.iriGenerator.generateLogisticsEventLoId(loId, uuid);

        this.logisticsObjectsEventService.create(loId, uuid, iri, oneRecordBody, null);

        return ResponseEntityBuilder.create(201).header(HttpHeaders.LOCATION, iri).build();
    }

    @GetMapping(value = "/{eventId}", produces = BaseConstants.CONTENT_TYPE_LD_JSON)
    public ResponseEntity<?> getEvent(@PathVariable("loId") String loId,
                                      @PathVariable("eventId") String eventId) {

        String iri = this.iriGenerator.generateLogisticsEventLoId(loId, eventId);
        NeOneLogisticsEvents info = logisticsObjectsEventService.getByEventIdAndLoId(eventId, loId);
        if (ObjectUtils.isEmpty(info)) {
            throw new LogisticsObjectException("Logistics Object not found", 404);
        }
        return ResponseEntityBuilder.ok()
                .header("Type", "Type: https://onerecord.iata.org/ns/cargo#LogisticsEvent")
                .header("Location", iri)
                .body(info.getBodyText());

    }

    /**
     * 获取物流对象的所有物流事件信息
     *
     * @param loId           物流对象loId
     * @param eventCode      物流事件代码，多个值以逗号分隔
     * @param createdAfter   筛选在某个时间点之后创建的事件
     * @param createdBefore  筛选在某个时间点之前创建的事件
     * @param occurredAfter  筛选在某个时间点之后发生事件
     * @param occurredBefore 筛选在某个时间点之前发生事件
     * @param sort           排序规则
     * @param limit          返回的物流事件数量
     * @param skip           跳过的物流事件数量
     * @return 物流对象的所有物流事件信息
     */
    @GetMapping(produces = BaseConstants.CONTENT_TYPE_LD_JSON)
    public ResponseEntity<?> getEvents(@PathVariable("loId") String loId,
                                       @RequestParam(value = "event-code", required = false) String eventCode,
                                       @RequestParam(value = "created-after", required = false) String createdAfter,
                                       @RequestParam(value = "created-before", required = false) String createdBefore,
                                       @RequestParam(value = "occurred-after", required = false) String occurredAfter,
                                       @RequestParam(value = "occurred-before", required = false) String occurredBefore,
                                       @RequestParam(value = "sort", required = false) String sort,
                                       @RequestParam(value = "limit", required = false) Integer limit,
                                       @RequestParam(value = "skip", required = false) Integer skip) {
        // 获取lo对象的所有物流事件信息
        log.info("====获取lo对象所有的物流事件，loId：{}，event-code：{}，create-after：{}，create-before：{}，occurred-after：{}, occurred-after：{}，sort：{}，limit：{}，skip：{} ====", loId, eventCode, createdAfter, createdBefore, occurredAfter, occurredBefore, sort, limit, skip);
        LogisticsEventsRequestVO logisticsEventsRequestVO = new LogisticsEventsRequestVO();
        logisticsEventsRequestVO.setLoId(loId);
        logisticsEventsRequestVO.setEventCode(eventCode);
        logisticsEventsRequestVO.setCreatedAfter(createdAfter);
        logisticsEventsRequestVO.setCreatedBefore(createdBefore);
        logisticsEventsRequestVO.setOccurredAfter(occurredAfter);
        logisticsEventsRequestVO.setOccurredBefore(occurredBefore);
        logisticsEventsRequestVO.setSort(sort);
        logisticsEventsRequestVO.setLimit(limit);
        logisticsEventsRequestVO.setSkip(skip);
        return ResponseEntityBuilder.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/ld+json")
                .body(logisticsObjectsEventService.getEvents(logisticsEventsRequestVO));
    }
}
