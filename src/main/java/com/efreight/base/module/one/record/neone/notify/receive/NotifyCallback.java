package com.efreight.base.module.one.record.neone.notify.receive;

import java.util.function.Supplier;

/**
 * 通知回调
 *
 * @author fu yuan hui
 * @since 2024-09-14 13:25:40 星期六
 */
@FunctionalInterface
public interface NotifyCallback<T> {

    /**
     * 接收到通知之后进行处理
     *
     * @param t 通知实体
     */
//    void callback(NeOneNotifications notifications);
    void callback(Supplier<T> t);
}
