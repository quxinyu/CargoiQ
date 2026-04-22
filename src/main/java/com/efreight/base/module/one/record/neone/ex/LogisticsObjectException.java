package com.efreight.base.module.one.record.neone.ex;

import lombok.Getter;

/**
 * @author fu yuan hui
 * @since 2024-09-12 13:54:56 星期四
 */
@Getter
public class LogisticsObjectException extends RuntimeException {

    private final String subject;

    private final int code;

    public LogisticsObjectException(String subject, int code) {
        super(subject);
        this.subject = subject;
        this.code = code;
    }
}
