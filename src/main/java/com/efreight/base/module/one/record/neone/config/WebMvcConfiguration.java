package com.efreight.base.module.one.record.neone.config;

import com.efreight.base.module.one.record.neone.interceptor.OneRecordServerAuthenticationInterceptor;
import com.efreight.base.module.one.record.neone.resolve.HandlerMethodArgumentAliasResolver;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
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
                .excludePathPatterns(
                        "/",
                        "/index.html",
                        "/favicon.ico",
                        "/authapi/token",
                        "/token",
                        "/css/**",
                        "/js/**",
                        "/img/**",
                        "/fonts/**",
                        "/print/**",
                        "/swagger-ui/**",
                        "/webjars/**",
                        "/v2/api-docs",
                        "/error"
                );
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        // 参数别名处理器
        resolvers.add(new HandlerMethodArgumentAliasResolver());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/favicon.ico")
                .addResourceLocations("classpath:/static/favicon.ico");
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/");
        registry.addResourceHandler("/img/**")
                .addResourceLocations("classpath:/static/img/");
        registry.addResourceHandler("/fonts/**")
                .addResourceLocations("classpath:/static/fonts/");
        registry.addResourceHandler("/print/**")
                .addResourceLocations("classpath:/static/print/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // SPA history 路由兜底到 index.html，支持直接刷新前端页面
        registry.addViewController("/").setViewName("forward:/index.html");
        registry.addViewController("/{path:[^\\.]*}").setViewName("forward:/index.html");
        registry.addViewController("/**/{path:[^\\.]*}").setViewName("forward:/index.html");
    }

    @Bean
    public HandlerInterceptor handlerInterceptor() {
        return new OneRecordServerAuthenticationInterceptor();
    }
}
