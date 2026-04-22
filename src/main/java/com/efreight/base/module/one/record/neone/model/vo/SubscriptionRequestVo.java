package com.efreight.base.module.one.record.neone.model.vo;

import com.efreight.base.module.one.record.neone.model.request.BaseParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author fu yuan hui
 * @since 2024-10-14 15:31:54 星期一
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SubscriptionRequestVo extends BaseParam implements Serializable {

    private Long id;

    //主单号
    private String awbNumbers;

    //LOGISTICS_OBJECT: 表示订阅Waybill, LOGISTICS_BOOKING: 表示订阅Booking, LOGISTICS_EVENT: 表示订阅FSU
    private String subscribeType;

    //是否订阅分单
    private boolean subscribeFhl = true;

    //选择的FSU
    private String fsuCodes;

    //航班号
    private String flightNumbers;

    //始发港，可以多个，用逗号分隔
    private String departureCodes;

    //目的港，可以多个，用逗号分隔
    private String arrivalCodes;

    private String subscribeStatus;

    private String companyName;

    private String host;

    private String actionRequestIri;

    private LocalDateTime createTime;

}
