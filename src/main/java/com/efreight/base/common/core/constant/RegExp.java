package com.efreight.base.common.core.constant;

/**
 * @author alex
 * @date 2023/08/02
 */
public interface RegExp {
    /**
     * 验证机场
     */
    String VALID_AIRPORT = "^(SVO1)|(SVO2)|([A-Z]{3})$";
    /**
     * 验证主单或邮单
     */
    String VALID_AWB_MAIL = "^((\\d{3}-)?\\d{8})$";
    /**
     * 验证主单
     */
    String VALID_AWB = "^(\\d{3}-\\d{8})$";
    /**
     * 验证特货代码
     */
    String VALID_SHC = "^([A-Z]{3})$";
}
