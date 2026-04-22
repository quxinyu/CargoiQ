package com.efreight.base.module.one.record.neone.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.efreight.base.module.one.record.neone.model.entity.NeOneSubscriptionCallback;
import com.efreight.base.module.one.record.neone.model.entity.NeOneSubscriptionRequest;
import com.efreight.base.module.one.record.neone.model.entity.NeOneSubscriptions;
import org.apache.ibatis.annotations.Select;

/**
 * @author fu yuan hui
 * @since 2024-10-09 13:33:58 星期三
 */
public interface OneRecordSubscriptionsNotifyCallbackMapper extends BaseMapper<NeOneSubscriptionCallback> {

    @Select("SELECT * FROM ne_one_subscriptions_receive WHERE action_request_iri = #{actionRequestIri}")
    NeOneSubscriptionCallback getByActionRequestIri(String actionRequestIri);
}
