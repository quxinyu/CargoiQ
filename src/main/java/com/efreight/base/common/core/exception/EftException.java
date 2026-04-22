package com.efreight.base.common.core.exception;

import com.efreight.base.common.core.enmus.ResultStatusCode;

/**
 * @author alex
 * @date 2022/04/18
 */
public class EftException extends RuntimeException {

    private String code;

    public EftException(String message) {
        super(message);
    }

    public EftException(ResultStatusCode resultStatusCode) {
        super(resultStatusCode.getMsg());
        this.code = resultStatusCode.getCode();
    }

    public EftException(String code, String message) {
        super(message);
        this.code = code;
    }
    public EftException(String message, Throwable cause) {
        super(message, cause);
    }
    public EftException(Integer code, String message) {
        super(message);
        this.code = String.valueOf(code);
    }

    public String getCode() {
        return code;
    }

    public static EftException of(Exception e) {
        return new EftException(e.getMessage(), e);
    }


    public static EftException of(String message) {
        return new EftException(500, message);
    }
}
