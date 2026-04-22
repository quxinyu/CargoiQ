package com.efreight.base.module.one.record.neone.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.efreight.base.module.one.record.neone.model.request.SubscriptionRequestBody;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author fu yuan hui
 * @since 2024-10-09 11:00:33 星期三
 */
@TableName("ne_one_subscriptions_receive")
@Data
public class NeOneSubscriptionCallback implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    //订阅者的host
    private String subscriberHost;

    //订阅者的企业名称
    private String subscriberCompanyName;

    //主单号
    @TableField(updateStrategy = FieldStrategy.IGNORED)
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

    private Long subscriptionsId;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

}
