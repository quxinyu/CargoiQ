// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * NeOne 事件服务配置类
 * 配置前缀: neone-event-service
 *
 * @author quxinyu
 * @since 2025-01-09
 */
@Data
@Component
@ConfigurationProperties(prefix = "neone-event-service")
public class NeoneEventServiceConfig {

    /**
     * 是否验证订阅者
     * 默认: false
     * <p>
     * 当设置为 true 时，会调用通知服务验证哪些公司应该接收通知
     * 当设置为 false 时，会通知所有订阅者
     */
    private boolean validateSubscribers = false;
}
