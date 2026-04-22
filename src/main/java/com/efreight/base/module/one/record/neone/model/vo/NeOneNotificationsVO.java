package com.efreight.base.module.one.record.neone.model.vo;

import com.efreight.base.module.one.record.neone.model.request.BaseParam;
import lombok.Data;

import java.io.Serializable;

/**
 * @author quxinyu
 * @since 2025-11-12
 */
@Data
public class NeOneNotificationsVO extends BaseParam implements Serializable {


    private String loId;

    private String notifyFrom;

    /**
     * @see com.efreight.base.module.one.record.neone.enums.NotifyEventType
     */
    private String notifyEventType;

    private String notifyBody;

}
