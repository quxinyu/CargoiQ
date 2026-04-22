package com.efreight.base.module.one.record.neone.ex;

import lombok.Getter;

/**
 * @author fu yuan hui
 * @since 2024-09-10 18:16:13 星期二
 */
@Getter
public class ActionRequestException extends RuntimeException {

    private final String body;

    private final Integer code;

    public ActionRequestException(String body, Integer code) {
        super(body);
        this.body = body;
        this.code = code;
    }
}
