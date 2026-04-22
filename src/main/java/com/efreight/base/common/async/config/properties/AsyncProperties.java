package com.efreight.base.common.async.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 异步线程配置
 *
 * @author zuihou
 * Date: 2021/6/23 8:06 下午
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "eft.async")
public class AsyncProperties {

    /**
     * 是否启动线程池
     */
    private boolean enabled = true;

    /**
     * 异步核心线程数，默认：10
     */
    private int corePoolSize = 10;

    /**
     * 异步最大线程数，默认：50
     */
    private int maxPoolSize = 50;

    /**
     * 队列容量，默认：1000
     */
    private int queueCapacity = 1000;

    /**
     * 线程存活时间，默认：300s
     */
    private int keepAliveSeconds = 300;

    /**
     * 线程名前缀
     */
    private String threadNamePrefix = "eft-async-executor-";
}
