package com.efreight.base.common.core.exception;

import lombok.Getter;

/**
 * @author fu yuan hui
 * @since 2024-05-29 15:14:02 Wednesday
 */
@Getter
public enum EftErrorEnum implements ErrorCode {


    SYS_EXCEPTION("SYS_EXCEPTION", "系统异常"),


    ILLEGAL_PARAM("ILLEGAL_PARAM", "非法参数"),

    MISS_PARAM("MISS_PARAM", "缺少非空参数"),

    ILLEGAL_SIGN("ILLEGAL_SIGN", "签名不通过"),

    REQUEST_BODY_PARSE_ERROR("REQUEST_BODY_PARSE_ERROR", "请求的JSON结构体错误"),

    IMP_PARSE_FAIL("IMP_REPORT_PARSE_FAIL", "IMP报文解析失败"),

    SIGN_TIMESTAMP_INVALID("SIGN_TIMESTAMP_INVALID", "timestamp失效"),


    INPUT_NODE_EXCEPTION("INPUT_NODE_EXCEPTION", "输入节点异常"),


    CONVERT_NODE_EXCEPTION("CONVERT_NODE_EXCEPTION", "转换节点异常"),


    GENERAL_NODE_EXCEPTION("GENERAL_NODE_EXCEPTION", "通用节点异常"),


    OUTPUT_NODE_EXCEPTION("OUTPUT_NODE_EXCEPTION", "输出节点异常"),


    GROOVY_NODE_EXCEPTION("GROOVY_NODE_EXCEPTION", "动态节点异常"),


    NO_PERMISSION("NO_PERMISSION", "无调用权限", true),
    ;

    private final String code;

    private final String msg;

    private boolean exposeToBiz = false;

    EftErrorEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    EftErrorEnum(String code, String msg, boolean exposeToBiz) {
        this.code = code;
        this.msg = msg;
        this.exposeToBiz = exposeToBiz;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
