package com.efreight.base.common.core.exception;

import com.efreight.base.common.core.enmus.ResultStatusCode;
import lombok.Getter;

/**
 * @author fu yuan hui
 * @since 2024-08-13 17:07:59 星期二
 */
@Getter
public class OneRecordServerException extends RuntimeException {

    private ResultStatusCode statusCode;

    private Integer code;

    public OneRecordServerException(String message) {
        super(message);
    }

    public OneRecordServerException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public OneRecordServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public OneRecordServerException(ResultStatusCode statusCode) {
        super(statusCode.getMsg());
        this.statusCode = statusCode;
    }

    public OneRecordServerException(ResultStatusCode statusCode, Throwable cause) {
        super(statusCode.getMsg(), cause);
        this.statusCode = statusCode;
    }


}
