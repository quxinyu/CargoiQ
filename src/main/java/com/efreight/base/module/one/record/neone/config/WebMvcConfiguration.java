package com.efreight.base.module.one.record.neone.config;

import com.efreight.base.module.one.record.neone.interceptor.OneRecordServerAuthenticationInterceptor;
import com.efreight.base.module.one.record.neone.resolve.HandlerMethodArgumentAliasResolver;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * mvc相关配置
 *
 * @author fu yuan hui
 * @since 2024-08-29 14:07:35 星期四
 */
@Configuration
@EnableConfigurationProperties({NeOneConfigProperties.class})
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 认证拦截器
        registry.addInterceptor(handlerInterceptor())
                .addPathPatterns("/**")
                // 放行获取token的路径
                .excludePathPatterns("/authapi/token", "/token");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        // 参数别名处理器
        resolvers.add(new HandlerMethodArgumentAliasResolver());
    }

    @Bean
    public HandlerInterceptor handlerInterceptor() {
        return new OneRecordServerAuthenticationInterceptor();
    }
}
