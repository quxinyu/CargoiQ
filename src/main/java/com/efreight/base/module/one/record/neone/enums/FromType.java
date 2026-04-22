package com.efreight.base.module.one.record.neone.enums;

/**
 * @author fu yuan hui
 * @since 2024-09-14 13:11:31 星期六
 */
public enum FromType {

    /**
     * 自己创建的
     */
    CREATE_SELF,


    /**
     * 流程发送过来的
     */
    PIPELINE_FLOW_SEND,

    /**
     * 订阅通知发送过来的
     */
    SUBSCRIPTION_NOTIFY,

    OTHER,
    ;
}
