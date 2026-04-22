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
 * 通知服务配置类
 * 配置前缀: notification
 *
 * @author quxinyu
 * @since 2025-01-09
 */
@Data
@Component
@ConfigurationProperties(prefix = "notification")
public class NotificationServiceConfig {

    /**
     * 通知客户端基础 URL
     * 默认: http://localhost:8080
     */
    private String clientUrl = "http://localhost:8080";

    /**
     * 连接超时时间（毫秒）
     * 默认: 5000ms (5秒)
     */
    private int connectTimeout = 5000;

    /**
     * 读取超时时间（毫秒）
     * 默认: 30000ms (30秒)
     */
    private int readTimeout = 30000;

    /**
     * 通知发送间隔（毫秒）
     * 默认: 60000ms (1分钟)
     */
    private long sendInterval = 60000;

    /**
     * 连接超时时间（秒）
     * 兼容旧配置，单位为秒
     */
    public int getConnectTimeoutSeconds() {
        return connectTimeout / 1000;
    }

    /**
     * 读取超时时间（秒）
     * 兼容旧配置，单位为秒
     */
    public int getReadTimeoutSeconds() {
        return readTimeout / 1000;
    }
}
