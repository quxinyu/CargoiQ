package com.efreight.base.common.core.exception;

/**
 * @author fu yuan hui
 * @since 2024-01-21 15:13:37 Sunday
 */
public class ForbiddenRequestException  extends RuntimeException {

    public ForbiddenRequestException() {
        super("非法请求");
    }

    public ForbiddenRequestException(String message) {
        super(message);
    }
}
