package com.efreight.base.common.datasource.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据权限类型
 * <p>
 * 语法支持 Slf4j的字符串格式化
 * <p>
 *
 * @author Alex
 */
@Getter
@AllArgsConstructor
public enum DataScopeType {
    SELF(0, " {}{} = {} "),
    ALL(1, ""),
    CUSTOM(2, " {}{} IN ({}) ");


    private final int code;

    /**
     * mysql数据权限模板
     */
    private final String template;

    public static DataScopeType findCode(int code) {
        for (DataScopeType type : values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        return null;
    }
}
