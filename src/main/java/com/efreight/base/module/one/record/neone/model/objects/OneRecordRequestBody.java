package com.efreight.base.module.one.record.neone.model.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author fu yuan hui
 * @since 2024-01-14 17:25:37 Sunday
 */
@Data
public class OneRecordRequestBody implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long orgId;

    private Long userId;

    @JsonProperty("awbNumber")
    private String awbNumber;

    /**
     * ONE-RECORD JSON BODY: 是一个JSON字符串
     */
    @NotBlank
    private String oneRecordBody;

}