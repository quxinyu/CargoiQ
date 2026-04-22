package com.efreight.base.module.one.record.neone.model.vo;

import com.efreight.base.module.one.record.neone.enums.TopicType;
import com.efreight.base.module.one.record.neone.model.request.BaseParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author fu yuan hui
 * @since 2024-10-14 15:31:54 星期一
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SubscriptionVo extends BaseParam implements Serializable {

    private Long id;

    /**
     * 订阅类型
     * @see com.efreight.base.module.one.record.neone.enums.TopicType
     */
    private String subType;

    /**
     * 按类型订阅时的具体订阅Lo类型
     * @see com.efreight.base.module.one.record.neone.enums.TopicType.LoType
     */
    private String subTypeLoType;

    /**
     * 订阅者的回调host
     */
    private String callbackHost;

    /**
     * 订阅者的company Url
     */
    private String subscriptionCompanyUrl;

    /**
     * 订阅的topic:
     * 如果 subType == {@link TopicType#LOGISTICS_OBJECT_IDENTIFIER}, 那么该值是一个物流对象
     * 如果 subType == {@link TopicType#LOGISTICS_OBJECT_TYPE}, 那么 ‘subTypeLoType’ 是 {@link TopicType.LoType } 中的某一个值
     */
    private String topic;

    /**
     * 订阅的请求体
     */
    private String subscriptionJsonBody;

    /**
     * action-requests iri
     */
    private String actionRequestIri;

    /**
     * 包含订阅类型
     */
    private String includeSubscriptionType;

}
