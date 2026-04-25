package com.efreight.base.module.one.record.neone.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("ne_one_shipment_audit_log")
public class NeOneShipmentAuditLog implements Serializable {

    @TableId
    private Long id;

    private String shipmentDataIds;

    private String operateNo;

    private String operationType;

    private String operatorName;

    private String operatorIp;

    private String resultStatus;

    private String resultMessage;

    private String requestBody;

    private String traceId;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
