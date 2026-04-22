package com.efreight.base.module.one.record.neone.model.objects;

import com.efreight.base.common.core.enmus.MessageDataType;
import com.efreight.base.common.core.enmus.OneRecordParseVersionType;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author fu yuan hui
 * @since 2024-08-20 16:02:42 星期二
 */
@Data
public class ReportBodyContext {

    @NotBlank
    private String report;

    /**
     * @see MessageDataType
     */
    @NotBlank
    private String type;

    /**
     * @see OneRecordParseVersionType#name
     */
    @NotBlank
    private String parseVersion;
}
