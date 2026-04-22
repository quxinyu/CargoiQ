package com.efreight.base.module.one.record.neone.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class NeOneShipmentDataRequest extends BaseParam implements Serializable {

    @JsonProperty("MawbCode")
    private String mawbCode;

    @JsonProperty("LoId")
    private String loId;

}
