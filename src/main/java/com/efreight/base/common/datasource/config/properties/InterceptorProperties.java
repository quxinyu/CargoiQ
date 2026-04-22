package com.efreight.base.common.datasource.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author alex
 * @date 2022/9/14
 */
@Data
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "mybatis-plus.interceptor")
public class InterceptorProperties {
    /**
     * 是否开启租户schema模式拦截器
     */
    private Boolean enableSchema = false;
    /**
     * 是否开启org数据权限拦截器
     */
    private Boolean enableOrg = false;
    /**
     * org拦截器忽略的表名配置规则
     */
    private List<String> ignoreOrgTable;
}
