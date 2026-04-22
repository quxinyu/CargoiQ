package com.efreight.base.module.one.record.neone.config;

import com.efreight.base.module.one.record.neone.listener.LogisticsEventsListener;
import com.efreight.base.module.one.record.neone.listener.LogisticsObjectsListener;
import com.efreight.base.module.one.record.neone.listener.SubscriptionsChangeListener;
import com.efreight.base.module.one.record.neone.service.OneRecordResolvedLogisticsEventsService;
import com.efreight.base.module.one.record.neone.service.OneRecordResolvedLogisticsObjectService;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * EventBus相关配置
 *
 * @author fu yuan hui
 * @since 2024-09-13 15:25:33 星期五
 */
@Configuration
public class EventBusConfiguration {

    @Bean
    public EventBus asyncNeOneEventBus(@Qualifier("notifyExecutor") ThreadPoolTaskExecutor notifyExecutor,
                                       OneRecordResolvedLogisticsObjectService oneRecordResolvedLogisticsObjectService,
                                       OneRecordResolvedLogisticsEventsService oneRecordResolvedLogisticsEventsService) {
        EventBus neOneEventBus = new AsyncEventBus("NeOneAsyncEventBus", notifyExecutor);
        neOneEventBus.register(subscriptionsChangeListener());
        neOneEventBus.register(logisticsEventsListener());
        neOneEventBus.register(logisticsObjectsListener(oneRecordResolvedLogisticsObjectService));
        return neOneEventBus;
    }

    @Bean
    public EventBus eventBus() {
        return new EventBus("NeOneEventBus");
    }

    @Bean
    public SubscriptionsChangeListener subscriptionsChangeListener() {
        return new SubscriptionsChangeListener();
    }

    @Bean
    public LogisticsEventsListener logisticsEventsListener() {
        return new LogisticsEventsListener();
    }

    @Bean
    public LogisticsObjectsListener logisticsObjectsListener(OneRecordResolvedLogisticsObjectService oneRecordResolvedLogisticsObjectService) {
        return new LogisticsObjectsListener(oneRecordResolvedLogisticsObjectService);
    }
}
