package com.efreight.base.module.one.record.neone.notify;


import com.efreight.base.module.one.record.neone.enums.NotifyEventType;

import java.util.function.Supplier;


/**
 * @author fu yuan hui
 * @since 2024-09-11 13:40:49 星期三
 */
@FunctionalInterface
public interface NotifyHandler<T extends NotifyEntity> {

    /**
     * 发送通知
     * @param eventType 事件类型
     * @param t 事件对象
     */
    void notify(NotifyEventType eventType, Supplier<T> t);

}
