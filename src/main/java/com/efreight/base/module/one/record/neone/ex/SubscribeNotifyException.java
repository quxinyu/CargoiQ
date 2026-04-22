package com.efreight.base.module.one.record.neone.ex;

/**
 * @author fu yuan hui
 * @since 2024-10-14 11:39:17 星期一
 */
public class SubscribeNotifyException extends RuntimeException {

    public SubscribeNotifyException(String message) {
        super(message);
    }

    public SubscribeNotifyException(String message, Throwable cause) {
        super(message, cause);
    }
}
