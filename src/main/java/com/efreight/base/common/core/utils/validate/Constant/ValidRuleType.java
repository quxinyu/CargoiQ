package com.efreight.base.common.core.utils.validate.Constant;

/**
 * 自定义验证规则的验证类型
 *
 * @author alex
 * @date 2020/11/25
 */
public enum ValidRuleType {
    /**
     * 验证手机号
     */
    VALID_MOBILE,
    /**
     * 验证邮编
     */
    VALID_ZIP_CODE,
    /**
     * 验证身份证号
     */
    VALID_CITIZEN_ID,
    /**
     * 验证是否为英文字母 、数字和下划线
     */
    VALID_GENERAL,
    /**
     * 验证ip地址
     */
    VALID_IP,
    /**
     * 验证车牌号
     */
    VALID_PLATE_NUMBER,
    /**
     * 验证url网址
     */
    VALID_URL,
    /**
     * 验证是否中文
     */
    VALID_CHINESE
}
