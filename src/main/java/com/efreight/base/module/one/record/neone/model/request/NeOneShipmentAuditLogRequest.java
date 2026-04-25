package com.efreight.base.module.one.record.neone.model.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class NeOneShipmentAuditLogRequest extends BaseParam implements Serializable {

    /**
     * 操作单号
     */
    private String operateNo;

    /**
     * 操作人
     */
    private String operatorName;
}
