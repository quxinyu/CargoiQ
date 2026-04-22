package com.efreight.base.module.one.record.neone.controller.apis;

import com.efreight.base.common.core.exception.EftException;
import com.efreight.base.common.core.model.Result;
import com.efreight.base.module.one.record.neone.service.OneRecordSubscriptionsNotifyCallbackService;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author fu yuan hui
 * @since 2024-10-14 10:30:39 星期一
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/apis/callback")
public class OneRecordSubscribeNotifyApi {

    private final OneRecordSubscriptionsNotifyCallbackService subscriptionsNotifyCallbackService;

    private static final Configuration CONFIG = Configuration.defaultConfiguration().addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);


    @PostMapping("/subscribe")
    public Result<?> subscribeCallback(@RequestBody String body) {

        subscriptionsNotifyCallbackService.resolveSubscriptions(body);

        return Result.ok();
    }

    @PostMapping("/updateSubscribe")
    public Result<?> updateSubscribeCallback(@RequestBody String body) {

        subscriptionsNotifyCallbackService.resolveUpdateSubscriptions(body);

        return Result.ok();
    }

    @PostMapping("/unsubscribe")
    public Result<?> unsubscribe(@RequestBody  String jsonBody) {
        String host = JsonPath.using(CONFIG).parse(jsonBody).read("$.host", String.class);
        if (StringUtils.isBlank(host)) {
            throw new EftException("取消订阅的订阅者host为空，无法取消订阅");
        }
        subscriptionsNotifyCallbackService.unsubscribe(host);
        return Result.ok();
    }
}
