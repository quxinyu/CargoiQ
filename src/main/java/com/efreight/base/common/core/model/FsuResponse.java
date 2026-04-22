package com.efreight.base.common.core.model;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.List;

/**
 * @author fu yuan hui
 * @since 2023-12-08 15:00:06 Friday
 */
@Data
public class FsuResponse implements Serializable {

    private int code;

    private String message;

    private List<FusData> data;

    private String secret;

    private String fsuType;

    public boolean isSuccess() {
        return HttpStatus.OK.value() == this.code;
    }

    @Data
    public static class FusData {

        private String orderNumber;

        private String airMessage;
    }

    public static FsuResponse error(Throwable throwable) {
        FsuResponse response = new FsuResponse();
        response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setMessage(throwable.getMessage());
        return response;
    }
}
