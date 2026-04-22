package com.efreight.base.module.one.record.neone.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.efreight.base.module.one.record.neone.model.entity.NeOneSubscriptions;
import org.apache.ibatis.annotations.Select;

/**
 * @author fu yuan hui
 * @since 2024-09-10 17:59:11 星期二
 */
public interface NeOneSubscriptionsMapper extends BaseMapper<NeOneSubscriptions> {

    @Select("SELECT * FROM ne_one_subscriptions WHERE action_request_iri = #{actionRequestIri}")
    NeOneSubscriptions getByActionRequestIri(String actionRequestIri);

}
