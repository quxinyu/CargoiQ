package com.efreight.base.module.one.record.neone.notify.receive;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author quxinyu
 * @since 2025-11-20
 */
@Data
public class NeOneObjectNotifications implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String loId;

    private String notifyFrom;

    /**
     * @see com.efreight.base.module.one.record.neone.enums.NotifyEventType
     */
    private String notifyEventType;

    private String notifyBody;

    private LocalDateTime createTime;


}
