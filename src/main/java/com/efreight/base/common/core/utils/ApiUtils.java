package com.efreight.base.common.core.utils;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.efreight.base.common.core.exception.EftException;
import com.efreight.base.common.core.model.OneRecordCheck;
import lombok.extern.slf4j.Slf4j;

/**
 * API请求工具类
 *
 * @Author leo
 * @date 2024/5/15 17:16
 **/
@Slf4j
public class ApiUtils {

    private static final String CONTENT_TYPE_JSON = "application/json;charset=utf-8";
    private static final int TIMEOUT = 30000;

    /**
     * 发送http请求
     *
     * @param oneRecordCheck
     * @return
     */
    public static String sendHttpSync(OneRecordCheck oneRecordCheck) {
        return sendHttp(oneRecordCheck, false);
    }

    /**
     * 发送http请求
     *
     * @param oneRecordCheck
     * @param isAsync        是否异步
     * @return
     */
    public static String sendHttp(OneRecordCheck oneRecordCheck, boolean isAsync) {
        if ("POST".equalsIgnoreCase(oneRecordCheck.getMethod())) {
            return sendPost(oneRecordCheck, isAsync);
        } else if ("GET".equalsIgnoreCase(oneRecordCheck.getMethod())) {
            return sendGet(oneRecordCheck, isAsync);
        } else {
            throw EftException.of("暂不支持的请求类型：" + oneRecordCheck.getMethod());
        }
    }

    /**
     * 发送http post 请求
     *
     * @param oneRecordCheck
     * @param isAsync        是否异步
     * @return
     */
    public static String sendPost(OneRecordCheck oneRecordCheck, boolean isAsync) {
        return HttpUtil.createPost(oneRecordCheck.getUrl())
                .header("Content-Type", CONTENT_TYPE_JSON)
                .addHeaders(oneRecordCheck.getHeaders())
                .timeout(TIMEOUT)
                .body(oneRecordCheck.getPostJsonBody()).execute(isAsync).body();
    }

    /**
     * 发送http get 请求
     *
     * @param apiProperties
     * @param isAsync       是否异步
     * @return
     */
    public static String sendGet(OneRecordCheck apiProperties, boolean isAsync) {
        return HttpUtil.createGet(apiProperties.getUrl())
                .addHeaders(apiProperties.getHeaders())
                .form(JSONObject.toJSONString(apiProperties.getGetParams()))
                .timeout(TIMEOUT)
                .execute(isAsync).body();
    }

}
