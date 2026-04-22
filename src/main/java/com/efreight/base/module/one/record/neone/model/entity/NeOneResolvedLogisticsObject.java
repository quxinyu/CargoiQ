package com.efreight.base.module.one.record.neone.model.entity;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.annotation.*;
import com.efreight.base.common.core.enmus.LoModuleType;
import com.efreight.base.common.core.enmus.MessageDataType;
import com.efreight.base.common.core.model.NeOneResolvedData;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.time.*;

/**
 * @author fu yuan hui
 * @since 2024-08-13 16:41:54 星期二
 */
@AllArgsConstructor
@NoArgsConstructor
@TableName("ne_one_resolved_logistics_object")
@Data
public class NeOneResolvedLogisticsObject implements Serializable {

    @TableId
    private Long id;

    private Long loMainId;
    
    private String awbNumber;

    private String subNumber;

    private String flightNumber;

    private LocalDate flightDate;

    private String pol;

    private String pod;

    /**
     * @see LoModuleType
     * 事件类型：LOGISTICS_OBJECT: 物流对象;  LOGISTICS_EVENT: 物流事件
     */
    private String loModuleType = "LOGISTICS_OBJECT";

    private String loId;

    private String loIri;

    private String eventId;

    private String eventIri;

    /**
     * 对象类型：FWB, FHL....如果是FSU的，会以FSU:开头，比如FSU:DEP, FSU:DLV
     */
    private String contextType;

    /**
     * @see MessageDataType
     */
    private String firstMessageType;

    private String inputSourceReport;

    private String parseVersion;

    /**
     * 0：表示不删除，其他表示删除
     */
    @TableLogic(value = "0", delval = "id")
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    public static NeOneResolvedLogisticsObject create(NeOneResolvedData baseRecord) {
        NeOneResolvedLogisticsObject base = new NeOneResolvedLogisticsObject();
        base.setContextType(baseRecord.getContextType());
        base.setAwbNumber(baseRecord.getMawbNumber());
        if (StringUtils.isNotBlank(baseRecord.getFlightDate())) {
            if (baseRecord.getFlightDate().contains("T") && baseRecord.getFlightDate().endsWith("Z")) {
                Instant instant = Instant.parse(baseRecord.getFlightDate());
                ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
                LocalDate localDate = zonedDateTime.toLocalDate();
                base.setFlightDate(localDate);
            } else {
                base.setFlightDate(LocalDateTimeUtil.parseDate(baseRecord.getFlightDate(), "yyyy-MM-dd"));
            }
        }
        base.setLoModuleType(LoModuleType.LOGISTICS_OBJECT.name());
        base.setFlightNumber(baseRecord.getFlightNo());
        base.setPol(baseRecord.getDepPort());
        base.setPod(baseRecord.getArrPort());
        base.setCreateTime(LocalDateTime.now());
        base.setInputSourceReport(baseRecord.getInputSourceReport());
        base.setSubNumber(baseRecord.getHawbNumber());
        base.setFirstMessageType(baseRecord.getFirstMessageType());

        NeOneResolvedData.Meta meta = baseRecord.getMeta();
        if (meta != null) {
            base.setParseVersion(meta.getVersion() == null ? null : meta.getVersion().getName());
        }
        base.setLoMainId(baseRecord.getNeOneLogisticsObjectsId());
        return base;
    }
}
