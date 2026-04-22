package com.efreight.base.common.core.enmus;

import lombok.Getter;

/**
 * @author fu yuan hui
 * @date 2023-11-28 10:28:23 Tuesday
 */
@Getter
public enum OneRecordParseVersionType {

    @Deprecated
    ONE_R_V3_OLD("v3", "OneRecord2023"),

    /**
     * 3.0 截止到2024.08.19 只支持4种： FWB, FHL, FSU, FFR
     *
     * 在给 {@link cn.gov.caac.issp.utility.CaacParseTransfer} 包的时候，类型要选择type值，不可以用name
     */
    V3("OneRecord3", "OneRecord3"),

    /**
     * 带namespace前缀的，符合iata官方文档的
     */
    V3_NS("OneRecord3NS", "OneRecord3NS"),

    V2("OneRecord2", "OneRecord2020"),

    HU("OneRecord_HU", "OneRecord_HU");

    private final String name;

    private final String type;


    OneRecordParseVersionType(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public static OneRecordParseVersionType selectByType(String type) {
        if (V2.getType().equals(type)) {
            return V2;
        }
        return V3;
    }

    public static OneRecordParseVersionType selectByName(String name) {
        for (OneRecordParseVersionType value : values()) {
            if (value.getName().equals(name)) {
                return value;
            }
        }

        return V3;
    }

    public static OneRecordParseVersionType selectByNameNotDefault(String name) {
        for (OneRecordParseVersionType value : values()) {
            if (value.getName().equals(name)) {
                return value;
            }
        }

        return null;
    }
}
