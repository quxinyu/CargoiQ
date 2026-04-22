package com.efreight.base.module.one.record.neone.model.request;

import lombok.Data;

@Data
public class NeOneShipmentSendRequest {

    private String id;

    private String loId;

    private String checkCode;

    private String checkDescription;
}
