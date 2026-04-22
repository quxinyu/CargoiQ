package com.efreight.base.module.one.record.neone.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.efreight.base.common.core.model.Result;
import com.efreight.base.module.one.record.neone.model.entity.NeOneSubscriptionRequest;
import com.efreight.base.module.one.record.neone.model.request.SubscriptionRequestBody;
import com.efreight.base.module.one.record.neone.model.vo.SubscriptionRequestVo;

/**
 * @author fu yuan hui
 * @since 2024-10-09 11:11:51 星期三
 */
public interface OneRecordSubscriptionsNotifyService extends IService<NeOneSubscriptionRequest> {

    boolean subscribe(SubscriptionRequestBody request);

    boolean updateSubscribe(SubscriptionRequestBody request);

    Result<?> unSubscribe(Long id);

    IPage<?> pageList(SubscriptionRequestVo requestParam);

    String getFailCause(Long id);

    Object neOneDetails(Long id);

    Object getDetails(Long id);

    Result<?> removeSubscribe(Long id);

}
