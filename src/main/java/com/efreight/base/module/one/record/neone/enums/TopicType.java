package com.efreight.base.module.one.record.neone.enums;

import com.efreight.base.module.one.record.neone.ex.NotSupportLogisticsObjectTypeException;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

/**
 * @author fu yuan hui
 * @since 2024-09-09 18:00:49 星期一
 */
public enum TopicType {


    /**
     * 国际行协发布的 按照类型进行订阅
     */
    LOGISTICS_OBJECT_TYPE,


    /**
     * 国际行协发布的按照物流对象的ID进行订阅
     */
    LOGISTICS_OBJECT_IDENTIFIER,

    /**
     * 拓展的：按照主单订阅, 支持前缀进行订阅
     * 比如784-12345678
     * 可以输入784或者784- 进行前缀订阅
     * 还可以输入 784-12345678 订阅一个单一的主单号
     */
    LOGISTICS_OBJECT_LO_NUMBER,

    ;


    public static Set<String> resolveTopicName() {
        Set<String> topicNameSet = new HashSet<>();
        for (TopicType value : values()) {
            topicNameSet.add(value.name());
        }
        return topicNameSet;
    }

    @Getter
    public enum LoType {

        WAYBILL("Waybill"),

        PIECE("Piece"),

        SHIPMENT("Shipment"),

        LOGISTICS_EVENT("LogisticsEvent"),

        BOOKING("Booking"),


        /**
         * 他是属于  {@link TopicType#LOGISTICS_OBJECT_LO_NUMBER } 下面的
         */
        LO_NUMBER("LoNumber"),

        //  舱单 FFM
        TRANSPORT_MOVEMENT("TransportMovement"),

        ;

        private final String type;

        LoType(String type) {
            this.type = type;
        }

        public static LoType from(String type) {
            for (LoType loType : LoType.values()) {
                if (loType.type.equalsIgnoreCase(type)) {
                    return loType;
                }
            }
            throw new NotSupportLogisticsObjectTypeException("不支持的物流对象类型: " + type);
        }
    }

    public static TopicType from(String topicType) {
        for (TopicType value : values()) {
            if (value.name().equalsIgnoreCase(topicType)) {
                return value;
            }
        }

        throw new NotSupportLogisticsObjectTypeException("不支持的订阅类型: " + topicType);
    }
}
