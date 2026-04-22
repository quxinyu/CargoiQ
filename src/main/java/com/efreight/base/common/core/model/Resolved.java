package com.efreight.base.common.core.model;

import lombok.Data;

/**
 * @author fu yuan hui
 * @since 2024-11-29 15:40:23 星期五
 */
@Data
public class Resolved {

    private String mawbCode;

    private String hawbCode;

    private String flightNo;

    private String flightDate;

    private String msgType;

    private String fsuTypeValue;

    private String totalPieces;

    private String totalGrossWeight;

    private String depPort;

    private String arrPort;
}
