package com.efreight.base.common.core.model;

import com.efreight.base.common.core.enmus.ResultStatusCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author alex
 * @date 2022/04/15
 */
@Data
@NoArgsConstructor
public class Result<T> {
    @ApiModelProperty("状态码，200为正确，其他为错误")
    private String code;
    @ApiModelProperty("错误提示")
    private String message;
    @ApiModelProperty("成功时返回的数据")
    private T data;

    public Result(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Result(ResultStatusCode resultStatusCode, T data) {
        this(resultStatusCode.getCode(), resultStatusCode.getMsg(), data);
    }

    public Result(String code, String message) {
        this(code, message, null);
    }

    public Result(ResultStatusCode resultStatusCode) {
        this(resultStatusCode, null);
    }

    public static <T> Result<T> ok(T data) {
        return new Result(ResultStatusCode.OK, data);
    }

    public static <T> Result<T> ok() {
        return new Result<>(ResultStatusCode.OK);
    }

    public static <T> Result<T> result(ResultStatusCode code, T object) {
        return new Result(code, object);
    }

    public static <T> Result<T> result(ResultStatusCode code) {
        return new Result<>(code);
    }

    public static <T> Result<T> result(String code, String message, T object) {
        return new Result<>(code, message, object);
    }

    public static <T> Result<T> result(String code, String message) {
        return new Result(code, message);
    }

    /**
     * 请求失败提示
     */
    public static <T> Result<T> fail(String message) {
        return new Result(ResultStatusCode.BUSINESS_ERROR.getCode(), message);
    }

    public static <T> Result<T> fail(ResultStatusCode resultStatusCode) {
        return new Result(resultStatusCode.getCode(), resultStatusCode.getMsg());
    }

    public boolean isOk() {
        return ResultStatusCode.OK.getCode().equals(code);
    }
}
