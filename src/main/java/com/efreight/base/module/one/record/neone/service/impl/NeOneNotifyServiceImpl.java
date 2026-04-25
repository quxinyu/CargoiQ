package com.efreight.base.module.one.record.neone.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.efreight.base.module.one.record.neone.enums.NotifyEventType;
import com.efreight.base.module.one.record.neone.mapper.NeOneNotificationsMapper;
import com.efreight.base.module.one.record.neone.model.entity.NeOneLogisticsEvents;
import com.efreight.base.module.one.record.neone.model.entity.NeOneLogisticsObjects;
import com.efreight.base.module.one.record.neone.model.entity.NeOneNotifications;
import com.efreight.base.module.one.record.neone.model.onerecord.LogisticsEventFSU;
import com.efreight.base.module.one.record.neone.model.vo.NeOneNotificationsVO;
import com.efreight.base.module.one.record.neone.notify.receive.NeOneEventNotifications;
import com.efreight.base.module.one.record.neone.notify.receive.NeOneObjectNotifications;
import com.efreight.base.module.one.record.neone.notify.receive.NotifyCallback;
import com.efreight.base.module.one.record.neone.service.NeOneLogisticsEventsService;
import com.efreight.base.module.one.record.neone.service.NeOneLogisticsObjectsService;
import com.efreight.base.module.one.record.neone.service.NeOneNotifyService;
import com.efreight.base.module.one.record.neone.utils.IriUtils;
import com.efreight.base.module.one.record.neone.utils.LogisticsEventUtils;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;

/**
 * 接收通知 service实现类
 *
 * @author fu yuan hui
 * @since 2024-09-11 17:29:39 星期三
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NeOneNotifyServiceImpl extends ServiceImpl<NeOneNotificationsMapper, NeOneNotifications> implements NeOneNotifyService {

    private final NotifyCallback<NeOneObjectNotifications> objectCallback;
    private final NotifyCallback<NeOneEventNotifications> eventCallback;

    private final NeOneLogisticsEventsService neOneLogisticsEventsService;
    private final NeOneLogisticsObjectsService neOneLogisticsObjectsService;

    private static final Configuration CONF = Configuration.defaultConfiguration().addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);

    @Override
    public void receiveNotify(String notifyJsonBody) {
        String hasEventType = StringUtils.EMPTY;
        String hasLogisticsEvent = StringUtils.EMPTY;
        String location = StringUtils.EMPTY;
        String eventId = StringUtils.EMPTY;
        // 物流对象 iri
        String loIri = JsonPath.using(CONF).parse(notifyJsonBody).read("$.api:hasLogisticsObject.@id");
        // location
        if (notifyJsonBody.contains("api:isTriggeredBy")) {
            location = JsonPath.using(CONF).parse(notifyJsonBody).read("$.api:isTriggeredBy.@id");
        }
        if (notifyJsonBody.contains("api:hasLogisticsEvent")) {
            hasLogisticsEvent = JsonPath.using(CONF).parse(notifyJsonBody).read("$.api:hasLogisticsEvent.@id");
        }
        if (notifyJsonBody.contains("api:hasEventType")) {
            hasEventType = JsonPath.using(CONF).parse(notifyJsonBody).read("$.api:hasEventType.@id");
        }
        // 物流对象id
        String loId = IriUtils.extractLastVariableFromUrl(loIri);
        NeOneNotifications notifications = new NeOneNotifications();
        notifications.setLoId(loId);
        notifications.setNotifyEventType(hasEventType);
        notifications.setNotifyBody(notifyJsonBody);
        notifications.setNotifyFrom(location);
        notifications.setCreateTime(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
        if (StringUtils.isNotBlank(hasLogisticsEvent)) {
            eventId = IriUtils.extractLastVariableFromUrl(hasLogisticsEvent);
            notifications.setEventId(eventId);
        }
        // 保存通知到数据库
        save(notifications);
        if (hasEventType.contains(NotifyEventType.LOGISTICS_EVENT_RECEIVED.name())) {
            NeOneEventNotifications eventNotifications = new NeOneEventNotifications();
            BeanUtils.copyProperties(notifications, eventNotifications);
            eventNotifications.setHasLogisticsEvent(hasLogisticsEvent);
            eventCallback.callback(() -> eventNotifications);
        } else {
            NeOneObjectNotifications objectNotifications = new NeOneObjectNotifications();
            BeanUtils.copyProperties(notifications, objectNotifications);
            objectCallback.callback(() -> objectNotifications);
        }
    }

    @Override
    public IPage<?> notifyPage(NeOneNotificationsVO requestParam) {
        LambdaQueryWrapper<NeOneNotifications> wrapper = Wrappers.lambdaQuery();
        wrapper.orderByDesc(NeOneNotifications::getCreateTime);
        Page<NeOneNotifications> page = page(new Page<>(requestParam.getCurrent(), requestParam.getSize()), wrapper);
        List<NeOneNotifications> records = page.getRecords();
        if(CollectionUtils.isNotEmpty( records)){
            records.forEach(record -> {
                String s = notifyDetails(record.getId());
                if(s.contains("SAC") || s.contains("RCS")){
                    LogisticsEventFSU parsedEvent = LogisticsEventUtils.fromJson(s);
                    String ehcContent = JSON.toJSONString(parsedEvent.getExceptionHandlingCodes());
                    String code = parsedEvent.getEventCode().getCode();
                    record.setFsuStatus(code);
                    record.setEhcContent(ehcContent);
                }
            });
//            updateBatchById( records);
        }
        page.setRecords(records);
        return page;
    }

    @Override
    public String notifyDetails(Long id) {
        NeOneNotifications oneNotifications = this.getById(id);
        String bodyText = StringUtils.EMPTY;
        if (oneNotifications.getNotifyEventType().contains(NotifyEventType.LOGISTICS_EVENT_RECEIVED.name())) {
            LambdaQueryWrapper<NeOneLogisticsEvents> eventWrapper = new LambdaQueryWrapper<>();
            eventWrapper.eq(StringUtils.isNotBlank(oneNotifications.getEventId()), NeOneLogisticsEvents::getEventId, oneNotifications.getEventId());
            eventWrapper.orderByDesc(NeOneLogisticsEvents::getCreateTime);
            eventWrapper.last("LIMIT 1");
            NeOneLogisticsEvents events = neOneLogisticsEventsService.getOne(eventWrapper);
            if (ObjectUtils.isNotEmpty(events)) {
                bodyText = events.getBodyText();
            }
        } else {
            LambdaQueryWrapper<NeOneLogisticsObjects> loWrapper = new LambdaQueryWrapper<>();
            loWrapper.eq(StringUtils.isNotBlank(oneNotifications.getLoId()), NeOneLogisticsObjects::getLoId, oneNotifications.getLoId());
            List<NeOneLogisticsObjects> loList = neOneLogisticsObjectsService.list(loWrapper);
            NeOneLogisticsObjects one = loList.stream()
                    .sorted(Comparator.comparing(NeOneLogisticsObjects::getCreateTime, Comparator.nullsFirst(LocalDateTime::compareTo)).reversed())
                    .findFirst().orElse(null);
            if (ObjectUtils.isNotEmpty(one)) {
                bodyText = one.getBodyText();
            }
        }
        return bodyText;
    }
}
