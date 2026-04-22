package com.efreight.base.module.one.record.neone.service.impl;

import cn.gov.caac.issp.utility.CaacParseTransfer;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.efreight.base.common.core.exception.EftException;
import com.efreight.base.common.core.model.NeOneResolvedData;
import com.efreight.base.common.core.utils.CaacParseTransferTools;
import com.efreight.base.module.one.record.neone.config.NeOneDataModelProperties;
import com.efreight.base.module.one.record.neone.enums.FromType;
import com.efreight.base.module.one.record.neone.enums.NotifyEventType;
import com.efreight.base.module.one.record.neone.ex.LogisticsObjectException;
import com.efreight.base.module.one.record.neone.ex.NeoneRequestBodyException;
import com.efreight.base.module.one.record.neone.helper.IriGenerator;
import com.efreight.base.module.one.record.neone.mapper.NeOneLogisticsEventsMapper;
import com.efreight.base.module.one.record.neone.model.entity.NeOneLogisticsEvents;
import com.efreight.base.module.one.record.neone.model.entity.NeOneLogisticsObjects;
import com.efreight.base.module.one.record.neone.model.vo.LogisticsEventsRequestVO;
import com.efreight.base.module.one.record.neone.notify.NotifyHandler;
import com.efreight.base.module.one.record.neone.service.NeOneLogisticsEventsService;
import com.efreight.base.module.one.record.neone.service.NeOneLogisticsObjectsService;
import com.efreight.base.module.one.record.neone.utils.UUIDTools;
import com.google.common.eventbus.EventBus;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Option;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author fu yuan hui
 * @since 2024-09-18 11:29:07 星期三
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NeOneLogisticsEventsServiceImpl extends ServiceImpl<NeOneLogisticsEventsMapper, NeOneLogisticsEvents> implements NeOneLogisticsEventsService {

    private final NeOneDataModelProperties neOneDataModelProperties;

    private final NotifyHandler<NeOneLogisticsEvents> notifyHandler;

    private final NeOneLogisticsObjectsService logisticsObjectsService;

    private final EventBus asyncNeOneEventBus;

    private final IriGenerator iriGenerator;

    private static final List<String> SUPPORT_SORT_VALUES = Arrays.asList("ASC-CREATIONDATE", "DESC-CREATIONDATE", "ASC-EVENTDATE", "DESC-EVENTDATE");

    private static final Configuration CONFIG = Configuration.defaultConfiguration().addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);


    @Override
    public void create(String loId, String uuid, String iri, String logisticsEventBody) {
        if (StringUtils.isBlank(logisticsEventBody)) {
            throw new NeoneRequestBodyException("ne one request body must be not null");
        }

        //检查loId，这里暂时先不检查
//        NeOneLogisticsObjects info = this.logisticsObjectsService.getWithLoId(loId);
//        if (info == null) {
//            throw new LogisticsObjectException("Logistics Object not found", 404);
//        }

        NeOneLogisticsEvents event = new NeOneLogisticsEvents(loId, uuid, iri);
        event.setBodyText(logisticsEventBody);
        NeOneResolvedData.Meta meta = CaacParseTransferTools.parseLoEventTypeAndVersion(logisticsEventBody);
        event.setEventType(CaacParseTransferTools.resolveFsuEventType(logisticsEventBody));
        event.setCreateTime(LocalDateTime.now());
        event.setEventFromType(FromType.CREATE_SELF.name());
        event.setVersion(meta.getVersion().getName());

        save(event);
        event.setNotifyEventType(NotifyEventType.LOGISTICS_EVENT_RECEIVED);
        asyncNeOneEventBus.post(event);

//        this.notifyHandler.notify(NotifyEventType.LOGISTICS_EVENT_CREATED, () -> event);
    }

    @Override
    public String create(String eventBody) {
        String uuid = UUIDTools.generateSimpleUUID();
        // 查找对应的lo物流对象
        NeOneLogisticsObjects logisticsObjects = getLoByEventBody(eventBody);
        if (ObjectUtils.isEmpty(logisticsObjects)) {
            log.error(">>>>>>>oneRecordBody:{} 未匹配到主单物流对象,无法创建事件!", eventBody);
            throw new EftException(String.format(">>>>>>>oneRecordBody:  %s 未匹配到主单物流对象,无法创建事件!", eventBody));
        }
        String eventIri = this.iriGenerator.generateLogisticsEventLoId(logisticsObjects.getLoId(), uuid);
        NeOneLogisticsEvents event = new NeOneLogisticsEvents(null, uuid, eventIri);
        NeOneResolvedData.Meta meta = CaacParseTransferTools.parseLoEventTypeAndVersion(eventBody);
        event.setEventType(CaacParseTransferTools.resolveFsuEventType(eventBody));
        event.setCreateTime(LocalDateTime.now());
        event.setEventFromType(FromType.CREATE_SELF.name());
        event.setVersion(meta.getVersion().getName());
        event.setBodyText(eventBody);
        // 查找对应的lo对象
        event.setLoId(logisticsObjects.getLoId());
        // 将@id字段加入到eventBody里面
        JSONObject bodyText = JSONObject.parseObject(eventBody);
        bodyText.put("@id", eventIri);
        event.setBodyText(bodyText.toJSONString());
        save(event);
        event.setNotifyEventType(NotifyEventType.LOGISTICS_EVENT_RECEIVED);
        // 重复逻辑  在通知里实现
        asyncNeOneEventBus.post(event);
        event.setLoIri(logisticsObjects.getIri());
//        this.notifyHandler.notify(NotifyEventType.LOGISTICS_EVENT_CREATED, () -> event);
        return eventIri;
    }

    @Override
    public NeOneLogisticsObjects getLoByLoId(String loId) {
        return logisticsObjectsService.getWithLoId(loId);
    }

    @Override
    public NeOneLogisticsEvents getByEventId(String eventId) {
        LambdaQueryWrapper<NeOneLogisticsEvents> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(NeOneLogisticsEvents::getEventId, eventId);
        //wrapper.select(NeOneLogisticsEvents.class, t -> !"body_text".equalsIgnoreCase(t.getColumn()));

        return getOne(wrapper);
    }

    @Override
    public NeOneLogisticsEvents getByEventIdAndLoId(String eventId, String loId) {
        LambdaQueryWrapper<NeOneLogisticsEvents> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(NeOneLogisticsEvents::getEventId, eventId);
        wrapper.eq(NeOneLogisticsEvents::getLoId, loId);
        wrapper.orderByDesc(NeOneLogisticsEvents::getCreateTime);
        wrapper.last("LIMIT 1");
        //wrapper.select(NeOneLogisticsEvents.class, t -> !"body_text".equalsIgnoreCase(t.getColumn()));
        return getOne(wrapper);
    }

    @Override
    public String getEvents(LogisticsEventsRequestVO logisticsEventsRequestVO) {
        String loId = logisticsEventsRequestVO.getLoId();
        String eventCode = logisticsEventsRequestVO.getEventCode();
        String createdAfter = logisticsEventsRequestVO.getCreatedAfter();
        String createdBefore = logisticsEventsRequestVO.getCreatedBefore();
//        String occurredAfter = logisticsEventsRequestVO.getOccurredAfter();
//        String occurredBefore = logisticsEventsRequestVO.getOccurredBefore();
        String sort = logisticsEventsRequestVO.getSort();
        Integer limit = logisticsEventsRequestVO.getLimit();
        Integer skip = logisticsEventsRequestVO.getSkip();
        // 检查loId，这里暂时先不检查
        NeOneLogisticsObjects lo = logisticsObjectsService.getWithLoId(loId);
        if (lo == null) {
            throw new LogisticsObjectException("Logistics Object not found", 404);
        }
        if (!StringUtils.isBlank(eventCode)) {
            // TODO event-code 校验规则
        }
        // 校验时间筛选字段
        verifyLogisticsEventsRequestVOTimeField("created-after", createdAfter);
        verifyLogisticsEventsRequestVOTimeField("created-before", createdBefore);
        // TODO 目前不支持事件发生时间查询
//        verifyLogisticsEventsRequestVOTimeField("occurred-after", occurredAfter);
//        verifyLogisticsEventsRequestVOTimeField("occurred-after", occurredBefore);
        verifyLogisticsEventsRequestVOSort(sort);
        if (limit != null && limit < 0) {
            throw new LogisticsObjectException("limit must be greater than or equal to 0", 400);
        }
        if (skip != null && skip < 0) {
            throw new LogisticsObjectException("skip must be greater than or equal to 0", 400);
        }
        if (limit != null && skip == null) {
            skip = 0;
        }
        LambdaQueryWrapper<NeOneLogisticsEvents> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(NeOneLogisticsEvents::getLoId, loId);
        if (!StringUtils.isEmpty(eventCode)) {
            lambdaQueryWrapper.eq(NeOneLogisticsEvents::getEventType, eventCode.toUpperCase());
        }
        lambdaQueryWrapper.ge(!StringUtils.isBlank(createdAfter), NeOneLogisticsEvents::getCreateTime, createdAfter)
                .le(!StringUtils.isBlank(createdBefore), NeOneLogisticsEvents::getCreateTime, createdBefore)
                .orderByDesc(NeOneLogisticsEvents::getCreateTime)
                .orderByDesc(NeOneLogisticsEvents::getId)
                .last(limit != null, "limit " + skip + ", " + limit);
        List<NeOneLogisticsEvents> events = list(lambdaQueryWrapper);
        JSONObject result = new JSONObject();
        JSONObject context = new JSONObject();
        context.put("cargo", "https://onerecord.iata.org/ns/cargo#");
        context.put("api", "https://onerecord.iata.org/ns/api#");
        result.put("@context", context);
        result.put("@id", lo.getIri() + "/logistics-events");
        result.put("@type", "api:Collection");
        result.put("api:hasTotalItems", events.size());
        List<JSONObject> items = events.stream()
                .map(event -> JSONObject.parse(event.getBodyText()))
                .collect(Collectors.toList());
        result.put("api:hasItem", items);
        return result.toString();
    }

    /**
     * 校验批量获取物流事件请求参数VO对象中的时间字段
     *
     * @param field 时间字段名称
     * @param value 时间字段值
     */
    private void verifyLogisticsEventsRequestVOTimeField(String field, String value) {
        if (!StringUtils.isEmpty(value)) {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            try {
                // 尝试解析字符串
                ZonedDateTime.parse(value, formatter);
            } catch (Throwable e) {
                log.error(e.toString(), e);
                throw new LogisticsObjectException(field + " is invalid", 400);
            }
        }
    }

    /**
     * 校验批量获取物流事件请求参数VO对象中的排序字段
     *
     * @param sort 排序字段
     */
    private void verifyLogisticsEventsRequestVOSort(String sort) {
        if (!StringUtils.isEmpty(sort)) {
            if (!SUPPORT_SORT_VALUES.contains(sort.toUpperCase())) {
                throw new LogisticsObjectException("sort is invalid", 400);
            }
        }
    }

    /**
     * 根据事件体中的
     *
     * @param eventBody 事件体
     * @return 物流对象的loId
     */
    private NeOneLogisticsObjects getLoByEventBody(String eventBody) {
        // defaultModel
        Map<String, Object> record = CaacParseTransfer.parse(eventBody, neOneDataModelProperties.getDefaultModel(), "2", "JSON");
        String mawbCode = (String) record.get("MawbCode");
        try {
            log.info("-------根据事件Body查看物流对象 :{}  mawbCode：{}", eventBody, mawbCode);
            if (StringUtils.isNotBlank(mawbCode)) {
                NeOneLogisticsObjects one = logisticsObjectsService
                        .getOne(new LambdaQueryWrapper<NeOneLogisticsObjects>()
                                .eq(NeOneLogisticsObjects::getMawbCode, mawbCode)
                                .last("ORDER BY create_time DESC LIMIT 1"));
                log.info("----根据eventBody匹配到lo对象：{}", JSON.toJSONString(one));
                return ObjectUtils.isEmpty(one) ? null : one;
            }
        } catch (Throwable e) {
            log.error("----根据eventBody匹配lo对象失败");
            log.error(e.toString(), e);
        }
        return null;
    }

    private String extractValue(String text, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    /**
     * 批量重命名 JSON 中所有层级的字段名
     *
     * @param jsonObject 需要处理的 JSON 对象
     * @param renameMap  字段重命名映射，key 是旧字段名，value 是新字段名
     */
    public static void renameFields(JSONObject jsonObject, Map<String, String> renameMap) {
        Set<String> keys = jsonObject.keySet();
        for (String key : keys.toArray(new String[0])) {
            Object value = jsonObject.get(key);

            // 如果值是 JSONObject，递归处理
            if (value instanceof JSONObject) {
                renameFields((JSONObject) value, renameMap);
            }

            // 如果值是 JSONArray，遍历元素递归处理
            else if (value instanceof JSONArray) {
                JSONArray array = (JSONArray) value;
                for (Object item : array) {
                    if (item instanceof JSONObject) {
                        renameFields((JSONObject) item, renameMap);
                    }
                }
            }

            // 如果字段需要重命名
            if (renameMap.containsKey(key)) {
                String newKey = renameMap.get(key);
                jsonObject.put(newKey, value);
                jsonObject.remove(key);
            }
        }
    }

//    public static void main(String[] args) {
//        String jsonStr = "{\n" +
//                "    \"@id\": \"http://183.6.158.131:8080/api/neone/logistics-events/1689b839-0bf5-40cb-95d5-fd5536cf0c6e\",\n" +
//                "    \"@type\": \"cargo:LogisticsEvent\",\n" +
//                "    \"@context\": {\n" +
//                "        \"cargo\": \"https://onerecord.iata.org/ns/cargo#\"\n" +
//                "    },\n" +
//                "    \"eventFor\": {\n" +
//                "        \"@type\": \"cargo:Waybill\",\n" +
//                "        \"shipment\": {\n" +
//                "            \"@type\": \"cargo:Shipment\",\n" +
//                "            \"containedPieces\": [\n" +
//                "                {\n" +
//                "                    \"slac\": \"1\",\n" +
//                "                    \"@type\": \"cargo:Piece\",\n" +
//                "                    \"grossWeight\": \"30.0\",\n" +
//                "                    \"involvedInActions\": [\n" +
//                "                        {\n" +
//                "                            \"@type\": \"cargo:Loading\",\n" +
//                "                            \"servedActivity\": {\n" +
//                "                                \"@type\": \"cargo:TransportMovement\",\n" +
//                "                                \"movementTimes\": [\n" +
//                "                                    {\n" +
//                "                                        \"@type\": \"cargo:MovementTime\",\n" +
//                "                                        \"movementTimestamp\": \"\"\n" +
//                "                                    }\n" +
//                "                                ],\n" +
//                "                                \"arrivalLocation\": {\n" +
//                "                                    \"@type\": \"cargo:Location\",\n" +
//                "                                    \"locationCode\": \"CAN\"\n" +
//                "                                },\n" +
//                "                                \"departureLocation\": {\n" +
//                "                                    \"@type\": \"cargo:Location\",\n" +
//                "                                    \"locationCode\": \"CPH\"\n" +
//                "                                },\n" +
//                "                                \"transportIdentifier\": \"\"\n" +
//                "                            }\n" +
//                "                        }\n" +
//                "                    ]\n" +
//                "                },\n" +
//                "                {\n" +
//                "                    \"slac\": \"1\",\n" +
//                "                    \"@type\": \"cargo:Piece\",\n" +
//                "                    \"grossWeight\": \"30.0\"\n" +
//                "                }\n" +
//                "            ],\n" +
//                "            \"textualHandlingInstructions\": \"\"\n" +
//                "        },\n" +
//                "        \"shippingInfo\": \"YITONGZHI\",\n" +
//                "        \"waybillNumber\": \"56880456\",\n" +
//                "        \"waybillPrefix\": \"071\",\n" +
//                "        \"arrivalLocation\": {\n" +
//                "            \"code\": \"CAN\",\n" +
//                "            \"@type\": \"cargo:Location\"\n" +
//                "        },\n" +
//                "        \"departureLocation\": {\n" +
//                "            \"code\": \"CPH\",\n" +
//                "            \"@type\": \"cargo:Location\"\n" +
//                "        }\n" +
//                "    },\n" +
//                "    \"eventLocation\": {\n" +
//                "        \"code\": \"CAN\",\n" +
//                "        \"@type\": \"cargo:Location\"\n" +
//                "    },\n" +
//                "    \"cargo:eventCode\": \"NFD\",\n" +
//                "    \"cargo:eventDate\": {\n" +
//                "        \"@type\": \"http://www.w3.org/2001/XMLSchema#dateTime\",\n" +
//                "        \"@value\": \"2025-06-09 10:20:00\"\n" +
//                "    },\n" +
//                "    \"cargo:creationDate\": {\n" +
//                "        \"@type\": \"http://www.w3.org/2001/XMLSchema#dateTime\",\n" +
//                "        \"@value\": \"2025-06-09 10:20:56\"\n" +
//                "    },\n" +
//                "    \"cargo:eventTimeType\": \"Actual\"\n" +
//                "}";
//        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
//        Map<String, String> renameMap = Maps.newHashMap();
//        renameMap.put("eventFor", "cargo:eventFor");
//        renameMap.put("eventLocation", "cargo:eventLocation");
//        renameFields(jsonObject, renameMap);
//        System.out.println(jsonObject.toJSONString());
//    }


}
