package com.efreight.base.module.one.record.neone.enums;

import com.efreight.base.module.one.record.neone.ex.NotSupportActionRequestStatusException;

/**
 * @author fu yuan hui
 * @since 2024-09-11 10:09:13 星期三
 */
public enum ActionRequestStatus {

    /**
     * 订阅初始状态
     */
    REQUEST_PENDING,

    /**
     * 接收
     */
    REQUEST_ACCEPTED,

    /**
     * 拒绝
     */
    REQUEST_REJECTED,

    /**
     * 撤销
     */
    REQUEST_REVOKED,


    REQUEST_ACKNOWLEDGED,

    /**
     * 拓展
     */
    SUBSCRIPTION_REQUEST_REMOVED,

    ;

    public static ActionRequestStatus match(String value) {
        for (ActionRequestStatus status : values()) {
            if (status.name().equals(value)) {
                return status;
            }
        }

        throw new NotSupportActionRequestStatusException("不支持的action request patch status： " + value);
    }
}
