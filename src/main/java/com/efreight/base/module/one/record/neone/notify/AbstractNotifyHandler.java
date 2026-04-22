package com.efreight.base.module.one.record.neone.notify;

import com.efreight.base.module.one.record.neone.constants.BaseConstants;
import com.efreight.base.module.one.record.neone.enums.NotifyEventType;
import com.efreight.base.module.one.record.neone.enums.TopicType;
import com.efreight.base.module.one.record.neone.model.entity.NeOneNotificationsSendTracker;
import com.efreight.base.module.one.record.neone.model.entity.NeOneSubscriptions;
import com.efreight.base.module.one.record.neone.service.ActionRequestService;
import com.efreight.base.module.one.record.neone.service.NotifySendTrackerService;
import com.efreight.base.module.one.record.neone.service.SubscriptionsService;
import com.efreight.base.module.one.record.neone.utils.UrlFormatUtils;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Option;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.time.LocalDateTime;
import java.util.function.Supplier;

/**
 * @author fu yuan hui
 * @since 2024-09-11 13:42:43 星期三
 */
@Slf4j
public abstract class AbstractNotifyHandler<T extends NotifyEntity> implements NotifyHandler<T>, InitializingBean {

    @Autowired
    private OkHttpClient okHttpClient;

    @Autowired
    private NotifySendTrackerService notifySendTrackerService;

    @Autowired
    protected SubscriptionsService subscriptionsService;

    @Autowired
    protected ActionRequestService actionRequestService;

    @Autowired
    protected ThreadPoolTaskExecutor notifyExecutor;

    protected static final Configuration CONF = Configuration.defaultConfiguration().addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);

    protected abstract boolean isSupport(NotifyEventType eventType);

    protected abstract void doNotify(T t);

    protected void init() {}

    @Override
    public void notify(NotifyEventType eventType, Supplier<T> supplier) {
        if (isSupport(eventType)) {
            this.notifyExecutor.execute(() -> {
                T t = supplier.get();
                t.setNotifyEventType(eventType);
                doNotify(t);
            });
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }

    protected boolean broadcast(NeOneSubscriptions subscriptions, String notifyBody, NotifyEventType notifyEventType, TopicType topicType) {
        NeOneNotificationsSendTracker notifications = new NeOneNotificationsSendTracker();
        String url = UrlFormatUtils.resolveUrl(subscriptions.getCallbackHost() + "/notifications");
        RequestBody body = RequestBody.create(notifyBody, BaseConstants.OK3_LD_JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                //.header("Authorization", "Bearer your_token_here")
                .build();
        boolean sendOk = true;
        try (Response response = this.okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
               log.error(">>>>>>>>>>>>>>>>>>>发送通知response失败： code: {}, responseBody: {}", response.code(), toString(response.body()));
                sendOk = false;
                notifications.setErrorMsg(toString(response.body()));
            }
        } catch (Exception e) {
            log.error(">>>>>>>>>>>>>>>>>>>>>发送通知发生错误, url: {}, notifyBody: {}", url, notifyBody,  e);
            sendOk = false;
            notifications.setErrorMsg(e.getMessage());
        }
        notifications.setNotifyUrl(url);
        notifications.setNotifyBody(notifyBody);
        notifications.setNotifyEventType(notifyEventType.name());
        notifications.setNotifyStatus(sendOk ? "SUCCESS" : "FAIL");
        notifications.setSubscriptionId(subscriptions.getId());
        notifications.setNotifyTopicType(topicType.name());
        notifications.setCreateTime(LocalDateTime.now());
        try {
            this.notifySendTrackerService.save(notifications);
        } catch (Exception e) {
            log.error(">>>>>>>>>>>>>>>>>>>>>>>>>.保存通知结果发生错误", e);
        }
        return sendOk;
    }

    private static String toString(ResponseBody responseBody) {
        return responseBody == null ? "ResponseBody empty" : responseBody.toString();
    }
}
