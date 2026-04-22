package com.efreight.base.common.core.enmus;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author fu yuan hui
 * @since 2023-12-06 14:23:36 Wednesday
 */
public enum FsuTypesEnum {

    RCS, DEP, RCF, NFD, DLV;


    @AllArgsConstructor
    public enum Matcher {

        NOT_FULL(0),
        FULL(1);

        @Getter
        private final int code;
    }
}
