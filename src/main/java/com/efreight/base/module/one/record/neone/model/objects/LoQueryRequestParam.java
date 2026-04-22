package com.efreight.base.module.one.record.neone.model.objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author fu yuan hui
 * @since 2024-08-20 14:52:26 星期二
 */
@Data
public class LoQueryRequestParam {

    private String awbNumber;

    private String subNumber;

    private String flightNumber;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private String flightDateStart;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private String flightDateEnd;

    private String logisticsObjectId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTimeStart;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTimeEnd;
}
