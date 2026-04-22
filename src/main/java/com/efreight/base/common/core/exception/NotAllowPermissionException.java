package com.efreight.base.common.core.exception;

/**
 * @author fu yuan hui
 * @since 2024-01-19 17:39:33 Friday
 */
public class NotAllowPermissionException extends RuntimeException {

    public NotAllowPermissionException() {
        super("无操作权限,请获取权限后再进行操作!");
    }

    public NotAllowPermissionException(String message) {
        super(message);
    }
}
