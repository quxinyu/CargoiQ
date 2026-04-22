package com.efreight.base.module.one.record.neone.ex;

/**
 * @author fu yuan hui
 * @since 2024-09-19 17:16:18 星期四
 */
public class NeOneRequestWrapperException extends RuntimeException {

    public NeOneRequestWrapperException(String message) {
        super(message);
    }

    public NeOneRequestWrapperException(String message, Throwable cause) {
        super(message, cause);
    }
}
