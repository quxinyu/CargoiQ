package com.efreight.base.module.one.record.neone.controller;

import com.efreight.base.common.core.exception.EftException;
import com.efreight.base.common.core.model.Result;
import com.efreight.base.module.one.record.neone.annotations.Authenticated;
import com.efreight.base.module.one.record.neone.config.NeOneConfigProperties;
import com.efreight.base.module.one.record.neone.enums.TopicType;
import com.efreight.base.module.one.record.neone.helper.SubscriptionsParamValidator;
import com.efreight.base.module.one.record.neone.service.NeOneCompanyService;
import com.efreight.base.module.one.record.neone.service.OneRecordSubscriptionsNotifyCallbackService;
import com.efreight.base.module.one.record.neone.service.SubscriptionsService;
import com.efreight.base.module.one.record.neone.utils.ResponseEntityBuilder;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author fu yuan hui
 * @since 2024-09-09 14:07:58 星期一
 */
@Authenticated
@Slf4j
@RequiredArgsConstructor
@RestController
public class NeOneSubscriptionsController {

    private static final Configuration CONFIG = Configuration.defaultConfiguration().addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);
    private final SubscriptionsService subscriptionsService;

    private final NeOneConfigProperties neOneConfigProperties;
    private final NeOneCompanyService neOneCompanyService;

    private final OneRecordSubscriptionsNotifyCallbackService subscriptionsNotifyCallbackService;


    /**
     * 获取订阅信息
     * <p>
     * .../subscriptions?topicType=<a href="https://onerecord.iata.org/ns/api#LOGISTICS_OBJECT_TYPE&topic=https://onerecord.iata.org/ns/cargo#Shipment">...</a>
     * <p>
     * # 要编码成 %23, 否则接口调用时会报错
     * <p>
     * .../subscriptions?topicType=<a href="https://onerecord.iata.org/ns/api%23LOGISTICS_OBJECT_TYPE&topic=https://onerecord.iata.org/ns/cargo%23Shipment">...</a>
     */
    @GetMapping("/subscriptions")
    public ResponseEntity<?> getSubInfoAsPublisher(@RequestParam("topicType") String topicType,
                                                   @RequestParam("topic") String topic) {
        String body = subscriptionsService.handleSubscriptionProposal(TopicType.from(topicType), topic);
        return ResponseEntityBuilder.ok().body(body);
    }

    @SuppressWarnings("all")
    @PostMapping(value = "/subscriptions", consumes = {"application/ld+json", MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseEntityBuilder> subscriptions(@RequestBody String subscriptionsBody) {
        String host = neOneCompanyService.getLocalServerNoCache().getHost();
        SubscriptionsParamValidator.ResolvedSubscriptions config = SubscriptionsParamValidator.validate(subscriptionsBody, host);
        String iri = this.subscriptionsService.receiveSubscriptions(config, subscriptionsBody);
        ResponseEntity response = ResponseEntityBuilder.create()
                .header("Type", "https://onerecord.iata.org/ns/api#SubscriptionRequest")
                .header(HttpHeaders.LOCATION, iri)
                .build();
        return response;
    }

    @PostMapping(value = "/unsubscribe", consumes = {"application/ld+json", MediaType.APPLICATION_JSON_VALUE})
    public Result<?> unsubscribe(@RequestBody String jsonBody) {
        String host = JsonPath.using(CONFIG).parse(jsonBody).read("$.host", String.class);
        if (StringUtils.isBlank(host)) {
            throw new EftException("取消订阅的订阅者host为空，无法取消订阅");
        }
        subscriptionsNotifyCallbackService.unsubscribe(host);
        return Result.ok();
    }
}
