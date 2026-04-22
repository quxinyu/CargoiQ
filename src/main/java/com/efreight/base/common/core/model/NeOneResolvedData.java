package com.efreight.base.common.core.model;

import com.efreight.base.common.core.enmus.LoModuleType;
import com.efreight.base.common.core.enmus.MessageDataType;
import com.efreight.base.common.core.enmus.OneRecordParseVersionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author fu yuan hui
 * @since 2024-08-20 11:47:19 Friday
 */
@Data
public class NeOneResolvedData implements Serializable {

    private String flightNo;

    private String flightDate;

    private String depPort;

    private String arrPort;

    private String hawbNumber;

    private String mawbNumber;

    private String inputSourceReport;

    private String oneRecordJsonBody;

    private String totalPieces;

    private String totalGrossWeight;

    /**
     * @see MessageDataType
     */
    private String firstMessageType;

    private Meta meta;

    private String loModuleType;

    private String contextType;

    private boolean canResolveOneRecord;

    private boolean parseVersion;

    private Long neOneLogisticsObjectsId;



    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class Meta implements Serializable {

        private LoModuleType eventType;

        private OneRecordParseVersionType version;

        private String contextType;
    }
}

