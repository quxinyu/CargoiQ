package com.efreight.base.module.one.record.neone.model.request;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author fu yuan hui
 * @since 2024-10-09 11:00:33 星期三
 */
@Data
public class SubscriptionRequestBody implements Serializable {

    private Long id;

    @NotNull
    //订阅的目标id
    private String targetCompanyId;

    //主单号
    private String awbNumbers = StringUtils.EMPTY;

    //LOGISTICS_OBJECT: 表示订阅Waybill, LOGISTICS_BOOKING: 表示订阅Booking, LOGISTICS_EVENT: 表示订阅FSU
    @NotBlank
    private String subscribeType;

    //是否订阅分单
    private boolean subscribeFhl = true;

    //选择的FSU
    private String fsuCodes = StringUtils.EMPTY;

    //航班号
    private String flightNumbers = StringUtils.EMPTY;

    //始发港，可以多个，用逗号分隔
    private String departureCodes = StringUtils.EMPTY;

    //目的港，可以多个，用逗号分隔
    private String arrivalCodes = StringUtils.EMPTY;

    private String companyName;

    private String host;

}
