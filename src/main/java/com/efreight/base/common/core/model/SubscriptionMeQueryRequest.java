package com.efreight.base.common.core.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * @author efreight
 * @date 2023-11-30
 */
@Data
public class SubscriptionMeQueryRequest implements Serializable {

    @Min(1)
    @ApiModelProperty("当前所在页")
    private Integer current = 1;
    @Min(1)
    @ApiModelProperty("每页显示数量")
    private Integer size = 10;

    @ApiModelProperty("订阅者名称")
    private String subscriberName;

    @ApiModelProperty("订阅类型 ")
    private String subscribedType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("订阅时间-开始")
    private String createTimeStart;

    @ApiModelProperty("订阅时间-截止")
    private String createTimeEnd;

}
