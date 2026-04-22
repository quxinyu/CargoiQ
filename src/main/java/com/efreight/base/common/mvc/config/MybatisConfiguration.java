package com.efreight.base.common.mvc.config;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author alex
 * @date 2022/09/19
 */
@Configuration
public class MybatisConfiguration {

//    @Bean
//    @ConditionalOnMissingBean
//    public LampSqlInjector getMySqlInjector() {
//        return new LampSqlInjector();
//    }

    @Bean
    @ConditionalOnMissingBean
    public ISqlInjector sqlInjector() {
        return new com.efreight.base.common.mvc.injector.ISqlInjector();
    }

}
