package com.efreight.base.module.one.record.neone.listener;

import com.efreight.base.module.one.record.neone.enums.ActionRequestStatus;
import com.efreight.base.module.one.record.neone.enums.SubscribeStatus;
import com.efreight.base.module.one.record.neone.mapper.NeOneSubscriptionsMapper;
import com.efreight.base.module.one.record.neone.mapper.OneRecordSubscriptionsNotifyCallbackMapper;
import com.efreight.base.module.one.record.neone.mapper.OneRecordSubscriptionsNotifyMapper;
import com.efreight.base.module.one.record.neone.model.entity.NeOneActionRequests;
import com.efreight.base.module.one.record.neone.model.entity.NeOneSubscriptionCallback;
import com.efreight.base.module.one.record.neone.model.entity.NeOneSubscriptionRequest;
import com.efreight.base.module.one.record.neone.model.entity.NeOneSubscriptions;
import com.google.common.eventbus.Subscribe;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;

/**
 * @author fu yuan hui
 * @since 2024-09-13 15:55:51 星期五
 * <p>
 *
 * 当 action-request 变更了通知后，我这里采用EventBus异步通知的形式来更新订阅表的状态，其目的是为了避免在发送通知时，
 * 遍历订阅表根据action-request-iri来查询action表来获取状态，导致性能降低
 */
@SuppressWarnings("unused")
@Slf4j
public class SubscriptionsChangeListener {

    @Resource
    private NeOneSubscriptionsMapper neOneSubscriptionsMapper;

    @Resource
    private OneRecordSubscriptionsNotifyCallbackMapper oneRecordSubscriptionsNotifyCallbackMapper;

    @Resource
    private TransactionTemplate transactionTemplate;

    @Subscribe
    public final void subscriptionStatusChange(NeOneActionRequests neOneActionRequests) {
        log.info(">>>>>>>>>>>>>>>>>>>>>> eventBus 收到订阅变更通知：{}", neOneActionRequests);
        NeOneSubscriptions subscriptions = neOneSubscriptionsMapper.getByActionRequestIri(neOneActionRequests.getActionRequestIri());
        if (subscriptions == null) {
            log.error(">>>>>>>>>>>>>>>>>>>>> 根据 actionRequestIri 无法从官方订阅表中检索到订阅信息： {}", neOneActionRequests.getActionRequestIri());
            return;
        }

        NeOneSubscriptionCallback callback = this.oneRecordSubscriptionsNotifyCallbackMapper.getByActionRequestIri(neOneActionRequests.getActionRequestIri());
        if (callback == null) {
            log.error(">>>>>>>>>>>>>>>>>>>>> 根据 actionRequestIri 无法从拓展订阅表中检索到订阅信息： {}", neOneActionRequests.getActionRequestIri());
            return;
        }

        this.transactionTemplate.executeWithoutResult(status -> {
            try {
                subscriptions.setActionRequestStatus(neOneActionRequests.getActionRequestStatus());
                neOneSubscriptionsMapper.updateById(subscriptions);

                if (ActionRequestStatus.REQUEST_ACCEPTED.name().equals(neOneActionRequests.getActionRequestStatus())) {
                    callback.setSubscribeStatus(SubscribeStatus.SUBSCRIBE_OK.name());
                }

                if (ActionRequestStatus.SUBSCRIPTION_REQUEST_REMOVED.name().equals(neOneActionRequests.getActionRequestStatus())
                        || ActionRequestStatus.REQUEST_REJECTED.name().equals(neOneActionRequests.getActionRequestStatus())) {
                    callback.setSubscribeStatus(SubscribeStatus.UNSUBSCRIBE.name());
                }

                this.oneRecordSubscriptionsNotifyCallbackMapper.updateById(callback);
            } catch (Exception e) {
                log.error(">>>>>>>>>>>>>>>>>>>>>> 更新订阅状态失败：{}", e.getMessage(), e);
                status.setRollbackOnly();
                throw e;
            }
        });

    }


    @Subscribe
    public final void removeSubscriptionNotice(String actionRequestIri) {
        log.info(">>>>>>>>>>>>>>>>>>>>>> eventBus 收到订阅删除通知：{}", actionRequestIri);
        NeOneSubscriptions subscriptions = neOneSubscriptionsMapper.getByActionRequestIri(actionRequestIri);
        if (subscriptions == null) {
            log.error(">>>>>>>>>>>>>>>>>>>>>> 根据 actionRequestIri 无法检索到订阅信息： {}", actionRequestIri);
            return;
        }

        NeOneSubscriptionCallback callback = this.oneRecordSubscriptionsNotifyCallbackMapper.getByActionRequestIri(actionRequestIri);
        if (callback == null) {
            log.error(">>>>>>>>>>>>>>>>>>>>> 根据 actionRequestIri 无法从拓展订阅表中检索订阅信息： {}", actionRequestIri);
            return;
        }

        this.transactionTemplate.executeWithoutResult(status -> {
            try {
                /*
                 * 新增一个标识，不用deleted字段来表示删除
                 */
                subscriptions.setActionRequestStatus(ActionRequestStatus.SUBSCRIPTION_REQUEST_REMOVED.name());
                neOneSubscriptionsMapper.updateById(subscriptions);

                callback.setSubscribeStatus(SubscribeStatus.UNSUBSCRIBE.name());
                this.oneRecordSubscriptionsNotifyCallbackMapper.updateById(callback);
            } catch (Exception e) {
                log.error(">>>>>>>>>>>>>>>>>>>>>> 删除订阅信息失败：{}", e.getMessage(), e);
                status.setRollbackOnly();
                throw e;
            }
        });
    }
}
