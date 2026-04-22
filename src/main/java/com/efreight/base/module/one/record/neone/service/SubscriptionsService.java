package com.efreight.base.module.one.record.neone.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.efreight.base.module.one.record.neone.enums.TopicType;
import com.efreight.base.module.one.record.neone.helper.SubscriptionsParamValidator;
import com.efreight.base.module.one.record.neone.model.entity.NeOneSubscriptionCallback;
import com.efreight.base.module.one.record.neone.model.entity.NeOneSubscriptions;
import com.efreight.base.module.one.record.neone.model.vo.SubscriptionVo;

import java.util.List;

/**
 * @author fu yuan hui
 * @since 2024-09-09 17:08:54 星期一
 */
public interface SubscriptionsService extends IService<NeOneSubscriptions> {

    String handleSubscriptionProposal(TopicType topicType, String topic);

    /**
     * 接收订阅
     */
    String receiveSubscriptions(SubscriptionsParamValidator.ResolvedSubscriptions config, String subscribeBody);

    List<NeOneSubscriptions> retrieveSubscriptionsWithStatus();

    boolean hasSubscription(NeOneSubscriptions neOneSubscriptions);


    NeOneSubscriptions getByActionRequestIri(String actionRequestIri);

    /**
     * 发起订阅
     */
    void doSubscriptions();

    void unsubscribe(String host);

    IPage<?> pageList(SubscriptionVo requestParam);

    NeOneSubscriptionCallback receiveDetails(Long id);

    boolean deleteSubscription(Long id);

    List<NeOneSubscriptions> getSubscriptions(Object loObject);

    void internalSubscription(NeOneSubscriptions neOneSubscriptions);

}
