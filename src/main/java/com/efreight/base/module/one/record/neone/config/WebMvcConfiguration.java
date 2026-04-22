package com.efreight.base.module.one.record.neone.config;

import com.efreight.base.module.one.record.neone.controller.onerecord.*;
import com.efreight.base.module.one.record.neone.handler.LogisticsEventHandler;
import com.efreight.base.module.one.record.neone.handler.LogisticsObjectHandler;
import com.efreight.base.module.one.record.neone.handler.NotificationHandler;
import com.efreight.base.module.one.record.neone.handler.SubscriptionHandler;
import com.efreight.base.module.one.record.neone.interceptor.OneRecordServerAuthenticationInterceptor;
import com.efreight.base.module.one.record.neone.model.onerecord.IdProvider;
import com.efreight.base.module.one.record.neone.resolve.HandlerMethodArgumentAliasResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
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
@RequiredArgsConstructor
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final CargoOntologyService cargoOntologyService;
    private final SubscriptionHandler subscriptionHandler;
    private final NotificationHandler notificationHandler;
    private final LogisticsObjectHandler logisticsObjectHandler;
    private final LogisticsEventHandler logisticsEventHandler;
    private final IdProvider idProvider;

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

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 将自定义转换器添加到列表前面，确保它们在默认转换器之前处理

        // 添加 LogisticsEventHttpMessageConverter 到列表前面
        converters.add(new LogisticsEventHttpMessageConverter(logisticsEventHandler, idProvider, cargoOntologyService));

        // 添加 SubscriptionHttpMessageConverter 到列表前面
        converters.add(new SubscriptionHttpMessageConverter(subscriptionHandler, idProvider));

        // 添加 NotificationHttpMessageConverter 到列表前面
        converters.add(new NotificationHttpMessageConverter(notificationHandler, logisticsObjectHandler, idProvider));

        // 添加 LogisticsObjectListHttpMessageConverter
        converters.add(new LogisticsObjectListHttpMessageConverter(cargoOntologyService, idProvider));

        // 添加 LogisticsObjectHttpMessageConverter 用于处理 GetLogisticsObjectResponse
        converters.add(new LogisticsObjectHttpMessageConverter(cargoOntologyService));

        // Add ModelBodyHandler for RDF4J Model conversion (replaces JAX-RS MessageBodyReader/Writer)
        // Note: ModelBodyHandler is now a @Component, so it will be auto-registered via Spring's
        // message converter detection if needed, or we can add it manually:
        // converters.add(new ModelBodyHandler(idProvider, new WriterConfig(), cargoOntologyService));
    }

    @Bean
    public HandlerInterceptor handlerInterceptor() {
        return new OneRecordServerAuthenticationInterceptor();
    }

}
