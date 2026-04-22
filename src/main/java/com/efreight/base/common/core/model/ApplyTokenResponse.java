package com.efreight.base.common.core.model;

import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * @author fu yuan hui
 * @since 2023-12-26 11:53:43 Tuesday
 */
@Data
public class ApplyTokenResponse {

    private int code;

    private String message;

    private TokenData data;

    public boolean isSuccess() {
        return code == HttpStatus.OK.value();
    }

    public static ApplyTokenResponse error(String message) {
        ApplyTokenResponse response = new ApplyTokenResponse();
        response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setMessage("feign execute fail: " + message);
        return response;
    }

    @Data
    public static class TokenData {

        private String userLanguageInfo;

        //token
        private String authorization;

        private String userKey;

        private int resetPwd;
    }
}
