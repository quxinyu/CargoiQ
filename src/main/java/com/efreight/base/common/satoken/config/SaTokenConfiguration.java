package com.efreight.base.common.satoken.config;

import cn.dev33.satoken.stp.StpInterface;
import com.efreight.base.common.satoken.core.SaPermissionImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Sa-Token 配置
 *
 * @author Lion Li
 */
@Slf4j
@Configuration
public class SaTokenConfiguration {

    /**
     * 权限接口实现(使用bean注入方便用户替换)
     */
    @Bean
    public StpInterface stpInterface() {
        return new SaPermissionImpl();
    }

}
