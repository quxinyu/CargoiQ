package com.efreight.base.module.one.record.neone.ex;

import lombok.Getter;

/**
 * @author fu yuan hui
 * @since 2024-09-10 10:03:52 星期二
 */
@Getter
public class NotSupportLogisticsObjectTypeException extends RuntimeException {


    public NotSupportLogisticsObjectTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotSupportLogisticsObjectTypeException(String message) {
        super(message);
    }
}
