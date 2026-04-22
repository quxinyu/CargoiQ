package com.efreight.base.module.one.record.neone.utils;

import com.efreight.base.common.core.enmus.LoModuleType;
import com.efreight.base.common.core.enmus.OneRecordParseVersionType;
import com.efreight.base.common.core.model.NeOneResolvedData;
import com.efreight.base.common.core.utils.CaacParseTransferTools;
import com.efreight.base.module.one.record.neone.parse.lo.v2.LoBodyV2Parse;
import com.efreight.base.module.one.record.neone.parse.lo.v3.LoBodyV3Parse;
import org.apache.commons.lang3.StringUtils;

/**
 * @author fu yuan hui
 * @since 2024-08-22 14:59:55 星期四
 */
public final class ReverseResolveOneRecordBodyTools {

    public static NeOneResolvedData parse(String oneRecordJsonBody) {
        if (StringUtils.isBlank(oneRecordJsonBody)) {
            throw new IllegalArgumentException("待解析的OneRecord body 是空");
        }

        NeOneResolvedData.Meta eventRecord = CaacParseTransferTools.parseLoEventTypeAndVersion(oneRecordJsonBody);
        if (LoModuleType.LOGISTICS_COMPANY == eventRecord.getEventType()) {
            NeOneResolvedData base = new NeOneResolvedData();
            base.setMawbNumber("COMPANY_SPACE");
            base.setOneRecordJsonBody(oneRecordJsonBody);
            base.setMeta(eventRecord);

            return base;
        }

        OneRecordParseVersionType version = eventRecord.getVersion();
        if (OneRecordParseVersionType.V2 == version) {
            return LoBodyV2Parse.parse(oneRecordJsonBody);
        }

        return LoBodyV3Parse.parse(oneRecordJsonBody);
    }
}
