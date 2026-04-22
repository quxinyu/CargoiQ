package com.efreight.base.common.core.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yan jia pan
 * @since 2024-04-01 14:31:03 Monday
 */
@Data
public class OneRecordCheck {


    /**
     * 请求路径
     */
    private String url;

    /**
     * 请求方式 POST GET
     */
    private String method = "POST";

    /**
     * 请求头
     */
    private Map<String,String> headers = new HashMap<>();

    /**
     * 请求参数 get时
     */
    private Map<String,String> getParams = new HashMap<>();

    /**
     * 请求体信息 post时
     */
    private String postJsonBody;

}
