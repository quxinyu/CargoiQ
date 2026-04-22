package com.efreight.base.module.one.record.neone.parse.lo.v2;

import com.alibaba.fastjson.JSONObject;
import com.efreight.base.common.core.enmus.LoModuleType;
import com.efreight.base.common.core.enmus.OneRecordParseVersionType;
import com.efreight.base.common.core.model.NeOneResolvedData;

/**
 * @author fu yuan hui
 * @since 2024-08-21 15:36:37 星期三
 */
public class FhlLoBodyMessageV2Parse extends AbstractBodyMessageV2Parse {
    @Override
    public boolean select(JSONObject select) {
        return select.containsKey("iata:logisticsObject:companyIdentifier") && select.containsKey("iata:waybill:containedWaybill");
    }

    @Override
    public NeOneResolvedData parse(JSONObject jsonObject) {
        NeOneResolvedData lo = super.resolveMasterCode(jsonObject);
        lo.setMeta(NeOneResolvedData.Meta.builder().version(OneRecordParseVersionType.V2).eventType(LoModuleType.LOGISTICS_OBJECT).build());
        lo.setContextType("FHL");


        JSONObject containWaybillObject = jsonObject.getJSONObject("iata:waybill:containedWaybill");
        String fhlNumber = containWaybillObject.getString("iata:waybill:waybillNumber");
        lo.setHawbNumber(fhlNumber);


        JSONObject bookingObject = containWaybillObject.getJSONObject("iata:waybill:booking");

        JSONObject transportObject = bookingObject.getJSONObject("iata:booking:transportMovement");
        super.resolveLocation(lo, transportObject);


        return lo;
    }
}