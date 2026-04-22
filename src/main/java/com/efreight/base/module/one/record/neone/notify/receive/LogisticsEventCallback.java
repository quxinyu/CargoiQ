package com.efreight.base.module.one.record.neone.notify.receive;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.efreight.base.common.core.exception.EftException;
import com.efreight.base.common.core.utils.CaacParseTransferTools;
import com.efreight.base.module.one.record.neone.enums.FromType;
import com.efreight.base.module.one.record.neone.enums.NotifyEventType;
import com.efreight.base.module.one.record.neone.helper.IriGenerator;
import com.efreight.base.module.one.record.neone.model.entity.NeOneLogisticsEvents;
import com.efreight.base.module.one.record.neone.model.entity.NeOneLogisticsObjects;
import com.efreight.base.module.one.record.neone.model.entity.NeOneServerCompanyHolder;
import com.efreight.base.module.one.record.neone.service.NeOneCompanyService;
import com.efreight.base.module.one.record.neone.service.NeOneLogisticsEventsService;
import com.efreight.base.module.one.record.neone.service.NeOneLogisticsObjectsService;
import com.efreight.base.module.one.record.neone.service.OneRecordSubscriptionsNotifyService;
import com.efreight.base.module.one.record.neone.utils.HttpClientUtil;
import com.efreight.base.module.one.record.neone.utils.IriUtils;
import com.efreight.base.module.one.record.neone.utils.KeyLockTokenUtils;
import com.google.common.eventbus.EventBus;
import com.jayway.jsonpath.JsonPath;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * LO事件相关回调
 *
 * @author quxinyu
 * @since 2025-11-19
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LogisticsEventCallback extends AbstractNotifyCallback<NeOneEventNotifications> {

    private final NeOneCompanyService neOneCompanyService;

    private final NeOneLogisticsEventsService neOneLogisticsEventsService;
    private final NeOneLogisticsObjectsService neOneLogisticsObjectService;

    private final OneRecordSubscriptionsNotifyService oneRecordSubscriptionsNotifyService;

    private final HttpClientUtil httpClientUtil;

    private final EventBus asyncNeOneEventBus;

    private final IriGenerator iriGenerator;

    @Override
    protected void doCallback(NeOneEventNotifications neOneEventNotifications) {
        try {
            toCallback(neOneEventNotifications);
        } catch (Exception e) {
            log.error(">>>>>>>>>>>>>>>>>>>通知回调 from:{}  出错:{}", neOneEventNotifications.getNotifyFrom(), e);
            throw new EftException("通知回调出错: " + e);
        }
    }

    public void toCallback(NeOneEventNotifications notifications) {
        // LOGISTICS_EVENT_RECEIVED
        String notifyEventType = notifications.getNotifyEventType();
        // 例:http://202.105.151.140/neone/logistics-objects/5c7419f4-ec00-42f7-b1bf-1764f00836ff/logistics-events/35954163-7617-4443-857f-1cb537e4a6e2
        String hasLogisticsEvent = notifications.getHasLogisticsEvent();
        String loId = notifications.getLoId();
        String notifyBody = notifications.getNotifyBody();
        String eventId = IriUtils.extractLastVariableFromUrl(hasLogisticsEvent);
        log.info(">>>>>>>>>>>>>>>>>>>>>> 收到物流事件创建的回调通知： {}", notifications);
        // 查询LO对象
        LambdaQueryWrapper<NeOneLogisticsObjects> loWrapper = Wrappers.lambdaQuery();
        loWrapper.like(NeOneLogisticsObjects::getLoId, loId);
        loWrapper.orderByDesc(NeOneLogisticsObjects::getCreateTime);
        loWrapper.last("LIMIT 1");
        NeOneLogisticsObjects neOneLogisticsObjects = neOneLogisticsObjectService.getOne(loWrapper);
        if (ObjectUtils.isEmpty(neOneLogisticsObjects)) {
            log.error(">>>>>>通知回调事件 :{} 获取不到事件对应对象!", eventId);
            throw new EftException(">>>>>>通知回调事件 ：" + eventId + " 获取不到事件对应对象!");
        }
        String host = IriUtils.truncateBeforeLogisticsObjects(hasLogisticsEvent);
        if (StringUtils.isBlank(host)) {
            log.error(">>>>>>通知回调事件url host为空!");
            throw new EftException(">>>>>>通知回调事件url host为空!");
        }
        LambdaQueryWrapper<NeOneServerCompanyHolder> chWrapper = Wrappers.lambdaQuery();
        chWrapper.eq(NeOneServerCompanyHolder::getHost, host);
        chWrapper.orderByDesc(NeOneServerCompanyHolder::getCreateTime);
        chWrapper.last("LIMIT 1");
        NeOneServerCompanyHolder companyHolder = neOneCompanyService.getOne(chWrapper);
        if (ObjectUtils.isEmpty(companyHolder)) {
            log.error(">>>>通知回调事件 host :{} 未匹配到服务地址无法回调获取LO对象", host);
            throw new EftException(">>>>通知回调事件 host : " + host + " 未匹配到服务地址无法回调获取LO对象");
        }
        log.info(">>>>>>>>>>>>>>>>>>>通知回调 from ：{} ,回调地址信息 :{} ", notifications.getNotifyFrom(), companyHolder);
        String loIri = JsonPath.using(CONF).parse(notifyBody).read("$.api:hasLogisticsObject.@id");
        String topicType = JsonPath.using(CONF).parse(notifyBody).read("$.api:hasLogisticsObjectType.@value");
        if (StringUtils.isBlank(topicType)) {
            topicType = JsonPath.using(CONF).parse(notifyBody).read("$.api:hasLogisticsObjectType.@id");
        }
        if (StringUtils.isBlank(topicType)) {
            log.error(">>>>>>>>>>>>>>>>>> 通知的请求体结构没有 '{}' 节点, 这是非法结构", "api:hasLogisticsObjectType.@value");
        } else {
            String[] topicTypes = topicType.split("#");
            if (ArrayUtils.isEmpty(topicTypes)) {
                log.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 从订阅的请求体中无法湖区订阅的类型：{}", notifyBody);
            }
            topicType = topicTypes.length == 1 ? topicTypes[0] : topicTypes[1];
        }
        NeOneLogisticsEvents neOneLogisticsEvents = new NeOneLogisticsEvents();
        neOneLogisticsEvents.setEventId(eventId);
        neOneLogisticsEvents.setLoId(loId);
        neOneLogisticsEvents.setEventIri(this.iriGenerator.generateLogisticsEventLoId(loId, eventId));
        neOneLogisticsEvents.setLoIri(neOneLogisticsObjects.getIri());
        neOneLogisticsEvents.setCreateTime(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
        String bodyText = getBodyTextFromIri(hasLogisticsEvent, companyHolder);
        if (StringUtils.isNotBlank(bodyText)) {
            String eventCode = CaacParseTransferTools.resolveFsuEventType(bodyText);
            neOneLogisticsEvents.setEventType(eventCode);
            JSONObject bt = JSONObject.parseObject(bodyText);
            bt.put("@id", neOneLogisticsEvents.getEventIri());
            neOneLogisticsEvents.setBodyText(bt.toJSONString());
        } else {
            neOneLogisticsEvents.setBodyText("{}");
        }
        neOneLogisticsEvents.setEventFromType(FromType.SUBSCRIPTION_NOTIFY.name());

        this.neOneLogisticsEventsService.save(neOneLogisticsEvents);
        neOneLogisticsEvents.setNotifyEventType(NotifyEventType.LOGISTICS_EVENT_RECEIVED);
        asyncNeOneEventBus.post(neOneLogisticsEvents);

    }


    /**
     * 从iri地址获取物流事件的内容
     *
     * @param iri 物流对象的获取地址
     */
    private String getBodyTextFromIri(String iri, NeOneServerCompanyHolder companyHolder) {
        String token = KeyLockTokenUtils.getCompanyToekn(companyHolder);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/ld+json; version=2.0.0-dev");
        headers.add("Content-Type", "application/ld+json; version=2.0.0-dev");
        headers.add("Authorization", "Bearer " + token);
        try {
            String url = iri;
            log.info(">>>>>>>>>>>>>>>>>>获取物流事件, url/iri :{}，headers :{}", url, headers);
            ResponseEntity<String> response = httpClientUtil.get(url, headers, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                log.info(">>>>>>>>>>>>>>>>>>获取 :{} 物流事件成功，请求结果 :{}", companyHolder.getCompanyName(), response.getBody());
                return response.getBody();
            } else {
                log.error(">>>>>>>>>>>>>>>>>>获取 :{} 物流事件失败，请求结果 :{}", companyHolder.getCompanyName(), JSON.toJSONString(response));
                throw new EftException(">>>>>>获取:" + companyHolder.getCompanyName() + " 物流事件失败，请求结果 :" + JSON.toJSONString(response));
            }
        } catch (Throwable e) {
            log.error(">>>>>>>>>>>>>>>>>>获取 :{} 物流事件失败，原因 :{}", companyHolder.getCompanyName(), e.getMessage());
            throw new EftException(">>>>>>获取 :" + companyHolder.getCompanyName() + " 物流事件失败: " + e);
        }
    }

}
