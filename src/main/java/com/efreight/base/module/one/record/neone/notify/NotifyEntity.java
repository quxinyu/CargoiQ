package com.efreight.base.module.one.record.neone.notify;

import com.baomidou.mybatisplus.annotation.TableField;
import com.efreight.base.module.one.record.neone.enums.NotifyEventType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author fu yuan hui
 * @since 2024-09-11 15:14:08 星期三
 */
@Getter
@Setter
public abstract class NotifyEntity {

    @TableField(exist = false)
    private NotifyEventType notifyEventType;
}
