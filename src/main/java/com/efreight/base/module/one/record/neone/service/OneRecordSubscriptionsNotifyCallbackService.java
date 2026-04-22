package com.efreight.base.module.one.record.neone.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.efreight.base.module.one.record.neone.model.entity.NeOneSubscriptionCallback;
import com.efreight.base.module.one.record.neone.model.entity.NeOneSubscriptionRequest;
import com.efreight.base.module.one.record.neone.model.request.SubscriptionRequestBody;

/**
 * @author fu yuan hui
 * @since 2024-10-09 11:11:51 星期三
 */
public interface OneRecordSubscriptionsNotifyCallbackService extends IService<NeOneSubscriptionCallback> {

    void resolveSubscriptions(String body);

    NeOneSubscriptionCallback retireSubscribeByHost(String host);

    void resolveUpdateSubscriptions(String body);

    void unsubscribe(String host);
}
