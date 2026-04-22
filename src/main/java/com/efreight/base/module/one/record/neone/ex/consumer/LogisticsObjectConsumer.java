package com.efreight.base.module.one.record.neone.ex.consumer;

import com.efreight.base.common.core.constant.MqLoConstants;
import com.efreight.base.common.core.enmus.LoModuleType;
import com.efreight.base.module.one.record.neone.enums.FromType;
import com.efreight.base.module.one.record.neone.helper.IriGenerator;
import com.efreight.base.module.one.record.neone.service.NeOneLogisticsEventsService;
import com.efreight.base.module.one.record.neone.service.NeOneLogisticsObjectsService;
import com.efreight.base.module.one.record.neone.utils.UUIDTools;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * @author fu yuan hui
 * @since 2024-09-19 15:59:20 星期四
 */
@Slf4j
@Service
@ConditionalOnProperty(name = "rocketmq.enabled", havingValue = "true", matchIfMissing = false)
@RocketMQMessageListener(consumerGroup = MqLoConstants.LOGISTICS_OBJECT_GROUP,
        topic = MqLoConstants.LOGISTICS_OBJECT_TOPIC, consumeMode = ConsumeMode.CONCURRENTLY,
        maxReconsumeTimes = 5, consumeThreadNumber = 8)
@RequiredArgsConstructor
public class LogisticsObjectConsumer implements RocketMQListener<String> {

    private static final Configuration CONFIG = Configuration.defaultConfiguration().addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);

    private final NeOneLogisticsObjectsService neOneLogisticsObjectsService;

    private final NeOneLogisticsEventsService neOneLogisticsEventsService;

    private final IriGenerator iriGenerator;

    @Override
    public void onMessage(String message) {
        if (StringUtils.isBlank(message)) {
            return;
        }
        log.info("receive message: {}", message);
        String type = JsonPath.using(CONFIG).parse(message).read("$.@type");
        if ("cargo:LogisticsEvent".contains(type)) {
            this.neOneLogisticsEventsService.create(message);
        } else {
            String uuid = UUIDTools.generateSimpleUUID();
            String iri = iriGenerator.generateLogisticsObjectLoId(uuid);
            this.neOneLogisticsObjectsService.create(iri, message, LoModuleType.LOGISTICS_OBJECT,FromType.PIPELINE_FLOW_SEND);
        }
    }
}
