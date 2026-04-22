package com.efreight.base.common.core.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author alex
 * @date 2022/04/28
 */
@Data
public class LoginConfigInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String loginUrl;

    private String logoutUrl;

    private String gateway;

    private String gatewayInternal;

    private String appkey;

    private String apppwd;

    private String service;

    private String deviceId;
}
