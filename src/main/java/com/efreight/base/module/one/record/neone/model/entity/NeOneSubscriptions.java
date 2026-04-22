package com.efreight.base.module.one.record.neone.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.efreight.base.module.one.record.neone.enums.TopicType;
import com.efreight.base.module.one.record.neone.helper.SubscriptionsParamValidator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author fu yuan hui
 * @since 2024-09-10 15:30:56 星期二
 */
@TableName("ne_one_subscriptions")
@Data
public class NeOneSubscriptions implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订阅类型
     *
     * @see com.efreight.base.module.one.record.neone.enums.TopicType
     */
    private String subType;

    /**
     * 按类型订阅时的具体订阅Lo类型
     *
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

    /**
     * 0：表示不删除，其他表示删除
     */
    @TableLogic(value = "0", delval = "id")
    private Integer deleted;

    /**
     * 冗余一个action-request表中的状态
     */
    private String actionRequestStatus;

    private String subVersion;

    /**
     * 流程id
     */
    private String ebaseFlowId;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    public TopicType resolveTopicType() {
        return TopicType.from(this.subType);
    }

    public TopicType.LoType resolveSubLoType() {
        return TopicType.LoType.from(this.subTypeLoType);
    }


    public static NeOneSubscriptions create(SubscriptionsParamValidator.ResolvedSubscriptions config) {
        NeOneSubscriptions ns = new NeOneSubscriptions();
        ns.setCreateTime(LocalDateTime.now());
        ns.setTopic(config.getTopic());
        ns.setSubType(config.getSubType());
        ns.setSubTypeLoType(config.getSubTypeLoType());
        ns.setSubscriptionJsonBody(config.getSubscriptionJsonBody());
//        ns.setCallbackHost(config.getCallbackHost());
        ns.setSubscriptionCompanyUrl(config.getSubscriptionCompanyUrl());
        ns.setIncludeSubscriptionType(config.getIncludeSubscriptionType());
        return ns;
    }

}
