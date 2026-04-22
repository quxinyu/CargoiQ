package com.efreight.base.common.core.model;

import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * @author fu yuan hui
 * @since 2023-12-08 16:43:57 Friday
 */
@Data
public class ApplyAuthResponse {

    private int code;

    private String message;

    private String data;

    public boolean ok() {
        return code == HttpStatus.OK.value();
    }

    public static ApplyAuthResponse error (String message) {
        ApplyAuthResponse response = new ApplyAuthResponse();
        response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setMessage("feign execute fail: " + message);
        return response;
    }
}
