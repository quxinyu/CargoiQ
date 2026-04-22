package com.efreight.base.module.one.record.neone.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author fu yuan hui
 * @since 2024-09-11 14:27:56 星期三
 */
@TableName("ne_one_notifications_send_tracker")
@Data
public class NeOneNotificationsSendTracker implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订阅者id
     */
    private Long subscriptionId;

    private String notifyUrl;

    private String notifyBody;

    private String notifyEventType;

    private String notifyTopicType;

    private String notifyStatus;

    private LocalDateTime createTime;

    private String errorMsg;

}
