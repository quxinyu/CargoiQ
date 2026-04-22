package com.efreight.base.module.one.record.neone.enums;

import lombok.Getter;

/**
 * @author fu yuan hui
 * @since 2024-10-14 11:02:23 星期一
 */
@Getter
public enum SubscribeStatus {

    SUBSCRIBE_OK("订阅成功"),

    SUBSCRIBE_FAIL("订阅失败"),

    SUBSCRIBE_UPDATE_FAIL("订阅更新失败"),

    UNSUBSCRIBE("取消订阅"),

    UNSUBSCRIBE_FAIL("取消订阅失败"),

    PENDING("等待更新订阅状态"),
    ;
    
    private final String desc;
    
    SubscribeStatus(String desc) {
        this.desc = desc;
    }

}
