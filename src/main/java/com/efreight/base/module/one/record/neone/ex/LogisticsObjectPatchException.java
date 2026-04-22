package com.efreight.base.module.one.record.neone.ex;

import lombok.Getter;

/**
 * @author fu yuan hui
 * @since 2025-04-16 13:29:53 星期三
 */
@Getter
public class LogisticsObjectPatchException extends RuntimeException {

    private final String subject;

    private final int code;

    public LogisticsObjectPatchException(String subject, int code) {
        super(subject);
        this.subject = subject;
        this.code = code;
    }
}
