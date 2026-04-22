package com.efreight.base.module.one.record.neone.model.vo;

import com.efreight.base.common.core.enmus.LoModuleType;
import com.efreight.base.common.core.enmus.MessageDataType;
import com.efreight.base.module.one.record.neone.model.entity.NeOneResolvedLogisticsObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author fu yuan hui
 * @since 2024-08-13 16:41:54 星期二
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OneRecordLogisticsInfoVo implements Serializable {

    private Long id;

    @JsonProperty("awbNumber")
    private String awbNumber;

    private String subNumber;

    private String flightNumber;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate flightDate;

    private String pol;

    private String pod;

    /**
     * @see LoModuleType
     * 事件类型：LOGISTICS_OBJECT: 物流对象;  LOGISTICS_EVENT: 物流事件
     */
    private String eventType;


    private String logisticsObjectId;

    /**
     * 对象类型：FWB, FHL....如果是FSU的，会以FSU:开头，比如FSU:DEP, FSU:DLV
     */
    private String objectType;

    /**
     * @see MessageDataType
     */
    private String firstMessageType;

    private String inputSourceReport;

    private String parseVersion;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;


    public static OneRecordLogisticsInfoVo convert(NeOneResolvedLogisticsObject info) {
        OneRecordLogisticsInfoVo vo = new OneRecordLogisticsInfoVo();
        BeanUtils.copyProperties(info, vo);
        return vo;
    }
}
