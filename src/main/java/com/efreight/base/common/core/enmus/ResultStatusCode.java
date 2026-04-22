package com.efreight.base.common.core.enmus;

/**
 * @param
 * @author
 * @description 错误代码
 * 1.错误码为0表示陈宫，其他都表示错误
 * 2.错误码长度为3位的是通用错误码，常规的如400、401、404、500
 * 3.错误码长度为5位的是系统自定义错误码；建议统一格式：ABBCC
 * A:错误级别，如1代表系统级别错误，4代表参数校验错误，5代表业务级别错误
 * B:项目或者模块名称对应的编码
 * C:具体的错误编号，可从01开始自增，如果不够用可以扩展为3位
 * @date 2018/1/5
 * @return
 */
public enum ResultStatusCode {
    OK("200", "OK"),
    BAD_REQUEST("400", "参数解析失败"),
    INVALID_TOKEN("401", "未识别的身份"),
    METHOD_NOT_ALLOWED("405", "不支持当前请求方法"),
    SYSTEM_ERR("500", "服务器运行异常"),
    TOO_MUCH_DATA_ERROR("2002", "批量新增数据过多"),
    UN_SELECTED_ORG("10100", "未识别的企业身份"),
    NOT_PERMISSION_ERROR("10101", "您没有权限访问"),
    KICK_OUT_ERROR("10102", "您已被系统踢出登录"),
    BE_REPLACED_ERROR("10103", "您的账号已在其他地方登录"),
    UPLOAD_ERROR("10002", "上传文件异常"),
    CAPTCHA_ERROR("10003", "验证码错误"),
    EXCEL_EXPORT_ERROR("10005", "导出excel失败！"),
    SERVICE_MAPPER_ERROR("10006", "Mapper类转换异常"),
    BUSINESS_ERROR("50000", "业务异常"),
    NOT_EXIST_USER_OR_ERROR_PWD("50101", "该用户不存在或密码错误"),
    LOGGED_IN("50102", "该用户已登录"),
    USER_FROZEN("50103", "该用户已被冻结"),
    INVALID_CAPTCHA("50105", "无效的验证码"),
    EXIST_SAME_ACCOUNT("50106", "该账号已存在"),

    ACCOUNT_HAS_LOCK("50107", "该账号已锁定,请稍后再重新登陆"),

    TOKEN_MISS("TOKEN_MISS", "token缺失，请检查"),

    TOKEN_EXPIRED("TOKEN_EXPIRED", "token失效，请重新获取"),

    TOKEN_SIGNATURE_FAIL("TOKEN_SIGNATURE_FAIL", "token签名验证失败"),

    CLIENT_ARGS_SERVER_ILLEGAL("CLIENT_ARGS_SERVER_ILLEGAL", "客户端参数在服务端不存在,请检查client_id和client_secret的配置"),

    CLIENT_ARGS_ILLEGAL("CLIENT_ARGS_ILLEGAL", "客户端参数不合法"),

    REQUEST_TOO_FAST("REQUEST_TOO_FAST", "您的请求太快了，请稍后再试"),
    ;

    private String code;

    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    ResultStatusCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
