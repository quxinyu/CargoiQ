package com.efreight.base.module.one.record.neone.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.efreight.base.module.one.record.neone.enums.ActionRequestStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author fu yuan hui
 * @since 2024-09-10 17:48:22 星期二
 */
@TableName("ne_one_action_requests")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class NeOneActionRequests implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * uuid值
     */
    private String actionRequestId;

    /**
     * action request 完整的url
     */
    private String actionRequestIri;

    /**
     * @see ActionRequestStatus
     */
    private String actionRequestStatus;

    /**
     * 0：表示不删除，其他表示删除
     */
    @TableLogic(value = "0", delval = "id")
    private Integer deleted;

    private String actionResponseBody;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;


    public NeOneActionRequests(String uuid, String actionRequestIri, String actionResponseBody) {
        this.actionRequestId = uuid;
        this.actionRequestIri = actionRequestIri;
        this.actionResponseBody = actionResponseBody;
        this.actionRequestStatus = ActionRequestStatus.REQUEST_PENDING.name();
        this.createTime = LocalDateTime.now();
    }
}
