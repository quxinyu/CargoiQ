package com.efreight.base.module.one.record.neone.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.efreight.base.common.core.enmus.LoModuleType;
import com.efreight.base.common.core.enmus.OneRecordParseVersionType;
import com.efreight.base.module.one.record.neone.enums.FromType;
import com.efreight.base.module.one.record.neone.enums.TopicType;
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
@TableName("ne_one_logistics_objects")
@Data
public class NeOneLogisticsObjects extends NotifyEntity implements Serializable {

    @TableId
    private Long id;

    /**
     * uuid
     */
    private String loId;

    /**
     * 完整的url
     */
    private String iri;

    private String bodyText;

    /**
     * LOGISTICS_OBJECT: 物流对象，LOGISTICS_EVENT：物流事件
     * @see LoModuleType
     */
    private String loModuleType;

    /**
     * @see TopicType.LoType
     */
    private String contextType;

    /**
     * @see FromType
     */
    private String loFromType;

    // 主单号
    private String mawbCode;

    /**
     * 0：表示不删除，其他表示删除
     */
    @TableLogic(value = "0", delval = "id")
    private Integer deleted;

    /**
     * @see OneRecordParseVersionType
     */
    private String version;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

}
