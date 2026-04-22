package com.efreight.base.module.one.record.neone.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author quxinyu
 * @since 2025-11-11
 */
@Data
public class SubscriptionResponseVo implements Serializable {

    private int code;

    private boolean status;

    private String locationUri;

}
