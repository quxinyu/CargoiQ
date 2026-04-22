package com.efreight.base.module.one.record.neone.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.efreight.base.common.core.enmus.OneRecordParseVersionType;
import com.efreight.base.module.one.record.neone.enums.FromType;
import com.efreight.base.module.one.record.neone.notify.NotifyEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author fu yuan hui
 * @since 2024-08-13 16:41:54 星期二
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("ne_one_logistics_events")
@Data
public class NeOneLogisticsEvents extends NotifyEntity implements Serializable {

    @TableId
    private Long id;

    /**
     * uuid
     */
    private String eventId;

    /**
     * 物流对象的 uuid
     */
    private String loId;

    /**
     * 事件完整的url
     */
    private String eventIri;

    /**
     * LO完整的url
     */
    private String loIri;

    private String bodyText;

    /**
     * CRS, CRF, DEP......
     */
    private String eventType;

    /**
     * @see FromType
     */
    private String eventFromType;

    /**
     * @see OneRecordParseVersionType
     */
    private String version;

    private String extParams;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;


    public NeOneLogisticsEvents(String loId, String eventId, String eventIri) {
        this.loId = loId;
        this.eventId = eventId;
        this.eventIri = eventIri;
    }
}
