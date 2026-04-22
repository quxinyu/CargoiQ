package com.efreight.base.module.one.record.neone.ex;

/**
 * @author fu yuan hui
 * @since 2024-09-10 10:15:24 星期二
 */
public class TopicMissingException extends RuntimeException {

    public TopicMissingException(String message) {
        super(message);
    }

    public TopicMissingException(String message, Throwable cause) {
        super(message, cause);
    }
}
