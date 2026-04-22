package com.efreight.base.module.one.record.neone.ex;

/**
 * @author fu yuan hui
 * @since 2024-11-29 14:18:52 星期五
 */
public class NeOneAuthException extends RuntimeException {

    public NeOneAuthException(String message) {
        super(message);
    }

    public NeOneAuthException(String message, Throwable cause) {
        super(message, cause);
    }
}
