package com.efreight.base.common.satoken.constant;


/**
 * 登录类型
 *
 * @author alex
 * @date 2021/04/20
 */
public enum UserType {
    // 系统后台管理员登录
    ADMIN_LOGIN("admin"),
    // 代理登录
    AGENT_LOGIN("agent");

    private String type;

    private UserType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static UserType getUserType(String str) {
        for (UserType value : values()) {
            if (value.getType().equals(str)) {
                return value;
            }
        }
        throw new RuntimeException("'UserType' not found By " + str);
    }
}
