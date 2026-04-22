//  Copyright Open Logistics Foundation
//
//  Licensed under the Open Logistics Foundation License 1.3.
//  For details on the licensing terms, see the LICENSE file.
//  SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.config;

import com.efreight.base.module.one.record.neone.listener.ChangeRequestEventListener;
import com.efreight.base.module.one.record.neone.listener.NeoneEventsListener;
import com.google.common.eventbus.EventBus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OneRecord EventBus 配置
 * 配置 Neone 事件发布和订阅模式
 *
 * @author quxinyu
 * @since 2025-01-19
 */
@Configuration
public class OneRecordEventBus {

    /**
     * 创建 OneRecord EventBus
     * 用于 Neone 事件的发布和订阅
     *
     * @param neoneEventsListener Neone事件监听器
     * @param changeRequestEventListener ChangeRequest事件监听器
     * @return EventBus 实例
     */
    @Bean
    public EventBus onerecordEventBus(NeoneEventsListener neoneEventsListener,
                                      ChangeRequestEventListener changeRequestEventListener) {
        EventBus neOneEventBus = new EventBus("OneRecordEventBus");
        // 注册 Neone 事件监听器
        neOneEventBus.register(neoneEventsListener);
        // 注册 ChangeRequest 事件监听器
        neOneEventBus.register(changeRequestEventListener);
        return neOneEventBus;
    }
}
