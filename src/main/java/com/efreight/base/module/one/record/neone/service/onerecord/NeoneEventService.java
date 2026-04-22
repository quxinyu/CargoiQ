// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.service.onerecord;

import com.efreight.base.module.one.record.neone.model.onerecord.NeoneEvent;
import com.google.common.eventbus.EventBus;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.rdf4j.model.IRI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

/**
 * Neone 事件发布服务
 * 负责发布 Neone 事件到 EventBus
 *
 * @author quxinyu
 * @since 2025-01-19
 */
@Slf4j
@Component
public class NeoneEventService {

    private final EventBus onerecordEventBus;

    /**
     * 构造函数
     *
     * @param onerecordEventBus OneRecord 事件总线
     */
    public NeoneEventService(EventBus onerecordEventBus) {
        this.onerecordEventBus = onerecordEventBus;
    }

    /**
     * 发布 Neone 事件到 EventBus（同步）
     * 使用同步 EventBus 发布事件，会立即通知所有已注册的监听器
     * NeoneEventsListener 监听
     *
     * @param event 要发布的 Neone 事件对象
     */
    public void publishEvent(NeoneEvent event) {
        log.debug("Publishing event [{}]", event);
        try {
            // 使用同步 EventBus 发布事件，会立即通知所有注册的监听器
            onerecordEventBus.post(event);
            log.info("Event published successfully: type={}, loId={}, state={}",
                    event.notificationEventType(),
                    event.loId().stringValue(),
                    event.state());
        } catch (Exception e) {
            log.error("Failed to publish event [{}]", event, e);
            throw new RuntimeException("Failed to publish Neone event", e);
        }
    }

    /**
     * 批量发布多个事件
     *
     * @param events 事件集合
     */
    public void publishEvents(Collection<NeoneEvent> events) {
        if (events == null || events.isEmpty()) {
            log.warn("No events to publish");
            return;
        }
        log.debug("Publishing [{}] events", events.size());
        try {
            for (NeoneEvent event : events) {
                onerecordEventBus.post(event);
            }
            log.info("Published [{}] events successfully", events.size());
        } catch (Exception e) {
            log.error("Failed to publish events", e);
            throw new RuntimeException("Failed to publish Neone events", e);
        }
    }

    /**
     * 创建物流对象事件
     *
     * @param iri                   事件 IRI
     * @param loId                  物流对象 IRI
     * @param notificationEventType 通知事件类型
     * @param state                 事件状态
     * @param triggeredBy           触发者（可选）
     * @param changedProperties     变更的属性
     * @param loTypes               物流对象类型
     * @return Neone 事件对象
     */
    public static NeoneEvent createEvent(IRI iri,
                                         IRI loId,
                                         IRI notificationEventType,
                                         NeoneEvent.State state,
                                         Optional<IRI> triggeredBy,
                                         Set<IRI> changedProperties,
                                         Set<IRI> loTypes) {
        return new NeoneEvent(iri, loId, notificationEventType, state, triggeredBy, changedProperties != null ? Collections.emptySet() : changedProperties,
                loTypes != null ? Collections.emptySet() : loTypes
        );
    }
}
