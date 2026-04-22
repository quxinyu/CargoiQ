package com.efreight.base.module.one.record.neone.enums;

/**
 * @author quxinyu
 * @since 2025-11-25
 */
public enum SubscribeTypes {

    LOGISTICS_OBJECT("LOGISTICS_OBJECT", "Waybill"),

    LOGISTICS_BOOKING("LOGISTICS_BOOKING", "Booking"),

    LOGISTICS_EVENT("LOGISTICS_EVENT", "Waybill"),

    LOGISTICS_SHIPMENT("LOGISTICS_SHIPMENT", "Shipment"),

    LOGISTICS_FFM("LOGISTICS_FFM", "TransportMovement"),

    ;

    private final String mapping;

    private final String type;

    SubscribeTypes(String mapping, String type) {
        this.mapping = mapping;
        this.type = type;
    }

    public static String match(String mapping) {
        for (SubscribeTypes subscribeTypes : SubscribeTypes.values()) {
            if (subscribeTypes.mapping.equals(mapping)) {
                return subscribeTypes.type;
            }
        }
        return null;
    }
}
