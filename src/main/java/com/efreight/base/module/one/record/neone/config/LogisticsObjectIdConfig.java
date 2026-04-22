// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Logistics Object ID 配置类
 * <p>
 * Spring Boot 配置属性，从 application.yml 读取配置
 * <p>
 * 配置前缀: lo-id-config
 *
 * @author test
 * @since 2025-01-09
 */
@Data
@Component
@ConfigurationProperties(prefix = "lo-id-config")
public class LogisticsObjectIdConfig {

    /**
     * 主机地址
     * 默认: {server.canonical-hostname}
     */
    private String host;

    /**
     * 端口号（可选）
     */
    private Optional<String> port;

    /**
     * 协议（https 或 http）
     * 默认: https
     */
    private String scheme = "https";

    /**
     * 内部 IRI 方案
     * 默认: neone
     */
    private String internalIriScheme = "neone";

    /**
     * 根路径
     * 默认: /
     */
    private String rootPath = "/";

    /**
     * 随机 ID 策略
     * 默认: UUID
     */
    private RandomIdStrategy randomIdStrategy = RandomIdStrategy.UUID;


}