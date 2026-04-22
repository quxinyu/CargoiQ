package com.efreight.base.module.one.record.neone.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author fu yuan hui
 * @since 2024-09-11 17:26:37 星期三
 */
@TableName("ne_one_notifications")
@Data
public class NeOneNotifications implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String loId;

    private String eventId;

    private String notifyFrom;

    /**
     * @see com.efreight.base.module.one.record.neone.enums.NotifyEventType
     */
    private String notifyEventType;

    private String notifyBody;

    private LocalDateTime createTime;
}
