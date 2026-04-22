package com.efreight.base.module.one.record.neone.ex;

/**
 * @author fu yuan hui
 * @since 2024-09-10 15:56:13 星期二
 */
public class NeoneRequestBodyException extends RuntimeException {

    public NeoneRequestBodyException(String message) {
        super(message);
    }

    public NeoneRequestBodyException(String message, Throwable cause) {
        super(message, cause);
    }
}
