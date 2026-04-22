package com.efreight.base.module.one.record.neone.exception;

import com.efreight.base.common.core.enmus.ResultStatusCode;
import lombok.Getter;

/**
 * @author quxinyu
 * @since 2024-08-13 17:07:59 星期二
 */
@Getter
public class SubjectNotFoundException extends RuntimeException {

    private ResultStatusCode statusCode;

    private Integer code;

    public SubjectNotFoundException(String message) {
        super(message);
    }

    public SubjectNotFoundException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public SubjectNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public SubjectNotFoundException(ResultStatusCode statusCode) {
        super(statusCode.getMsg());
        this.statusCode = statusCode;
    }

    public SubjectNotFoundException(ResultStatusCode statusCode, Throwable cause) {
        super(statusCode.getMsg(), cause);
        this.statusCode = statusCode;
    }

}
