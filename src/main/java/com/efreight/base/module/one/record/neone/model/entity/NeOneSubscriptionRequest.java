package com.efreight.base.module.one.record.neone.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.efreight.base.module.one.record.neone.model.request.SubscriptionRequestBody;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author fu yuan hui
 * @since 2024-10-09 11:00:33 星期三
 */
@AllArgsConstructor
@NoArgsConstructor
@TableName("ne_one_subscriptions_request")
@Data
public class NeOneSubscriptionRequest implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    //订阅的目标id
    private String targetCompanyId;

    private String targetHost;

    @TableField(updateStrategy = FieldStrategy.IGNORED)
    //主单号
    private String awbNumbers;

    //1: 表示订阅Waybill, 2 表示订阅Booking, 3 表示订阅FSU
    private String subscribeType;

    //是否订阅分单
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private boolean subscribeFhl;

    //选择的FSU
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String fsuCodes;

    //航班号
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String flightNumbers;

    //始发港，可以多个，用逗号分隔
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String departureCodes;

    //目的港，可以多个，用逗号分隔
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String arrivalCodes;

    private String subscribeStatus;

    private String subscribeJsonBody;

    private String actionRequestIri;

    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String subscribeFailCause;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @TableLogic(value = "0", delval = "id")
    private Long deleted;

    public NeOneSubscriptionRequest(SubscriptionRequestBody body) {
        this.targetCompanyId = body.getTargetCompanyId();
        this.awbNumbers = body.getAwbNumbers();
        this.subscribeType = body.getSubscribeType();
        this.subscribeFhl = body.isSubscribeFhl();
        this.fsuCodes = body.getFsuCodes();
        this.flightNumbers = body.getFlightNumbers();
        this.departureCodes = body.getDepartureCodes();
        this.arrivalCodes = body.getArrivalCodes();
        this.createTime = LocalDateTime.now();
    }
}
