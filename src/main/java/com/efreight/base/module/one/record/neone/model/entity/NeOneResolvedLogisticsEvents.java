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
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.time.*;

/**
 * @author fu yuan hui
 * @since 2024-08-13 16:41:54 星期二
 */
@AllArgsConstructor
@NoArgsConstructor
@TableName("ne_one_resolved_logistics_events")
@Data
public class NeOneResolvedLogisticsEvents implements Serializable {

    @TableId
    private Long id;
    
    private String awbNumber;

    private String flightNumber;

    private LocalDate flightDate;

    private String pol;

    private String pod;

    /**
     * @see LoModuleType
     * 事件类型：LOGISTICS_OBJECT: 物流对象;  LOGISTICS_EVENT: 物流事件
     */
    private String loModuleType = "LOGISTICS_EVENT";

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

    public static NeOneResolvedLogisticsEvents create(NeOneResolvedData baseRecord) {
        NeOneResolvedLogisticsObject objects = NeOneResolvedLogisticsObject.create(baseRecord);
        NeOneResolvedLogisticsEvents events = new NeOneResolvedLogisticsEvents();
        BeanUtils.copyProperties(objects, events);
        events.setLoModuleType("LOGISTICS_EVENT");
        return events;
    }
}
