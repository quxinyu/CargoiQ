package com.efreight.base.common.core.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author fu yuan hui
 */
@Configuration
public class RestTemplateConfig {

    /**
     * 这个restTemplate 只用通过服务名来调用，不能通过ip:port的形式，如果想用ip:port的形式调用，可直接new一个
     * @param factory
     * @return
     */
    @LoadBalanced
    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        return new RestTemplate(factory);
    }

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(3000);
        factory.setConnectTimeout(3000);
        return factory;
    }
}
