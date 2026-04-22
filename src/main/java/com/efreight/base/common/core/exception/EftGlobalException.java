package com.efreight.base.common.core.exception;

/**
 * @author fu yuan hui
 * @since 2024-05-29 16:09:58 Wednesday
 */
public class EftGlobalException extends RuntimeException {

    private ErrorCode errorCode;

    EftGlobalException(Throwable throwable, ErrorCode errorCode){
        super(errorCode.getMsg(), throwable);
        this.errorCode = errorCode;
    }

    EftGlobalException(String errMsg, Throwable throwable, ErrorCode errorCode){
        super(errMsg, throwable);
        this.errorCode = errorCode;
    }

    EftGlobalException(ErrorCode errorCode){
        super(errorCode.getMsg(), null);
        this.errorCode = errorCode;
    }

    EftGlobalException(String errMsg, ErrorCode errorCode){
        super(errMsg, null);
        this.errorCode = errorCode;
    }

    EftGlobalException(String errMsg){
        super(errMsg);
    }

    EftGlobalException(Throwable throwable){
        super(throwable);
        this.errorCode = EftErrorEnum.SYS_EXCEPTION;
    }

    public static EftGlobalException of(Throwable throwable, ErrorCode errorCode) {
        return new EftGlobalException(throwable, errorCode);
    }

    public static EftGlobalException of(String errMsg, Throwable throwable, ErrorCode errorCode) {
        return new EftGlobalException(errMsg, throwable, errorCode);
    }

    public static EftGlobalException of(String errMsg, ErrorCode errorCode) {
        return new EftGlobalException(errMsg, errorCode);
    }


    public static EftGlobalException of(ErrorCode errorCode) {
        return new EftGlobalException(errorCode);
    }

    public static EftGlobalException of(String errMsg) {
        return new EftGlobalException(errMsg);
    }

    public static EftGlobalException of(Throwable throwable) {
        return new EftGlobalException(throwable);
    }

    public ErrorCode getErrorCode(){
        return errorCode;
    }

    /**
     *  调用 ErrorCode 的方法用于返回
     * @return
     */
    public String getCode(){
        return errorCode.getCode();
    }

    public String getMsg(){
        return errorCode.getMsg();
    }
}
