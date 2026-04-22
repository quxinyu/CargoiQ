package com.efreight.base.common.core.enmus;

import com.efreight.base.common.core.exception.OneRecordServerException;
import lombok.Getter;

/**
 * @author fu yuan hui
 * @date 2023-11-28 10:32:30 Tuesday
 */
@Getter
public enum MessageDataType {

    CARGO_IMP("Cargo-IMP"),

    CARGO_XML("Cargo-XML"),

    CAAC_XML("CAAC-XML"),

    NOT_SURE("NOT-SURE"),

    ;


    private final String type;

    MessageDataType(String messageType) {
        this.type = messageType;
    }

    public static MessageDataType select(String type) {
        for (MessageDataType dataType : MessageDataType.values()) {
            if (dataType.getType().equals(type)) {
                return dataType;
            }
        }

        throw new OneRecordServerException("不支持的报文类型: " + type);
    }
}
