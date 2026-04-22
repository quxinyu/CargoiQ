package com.efreight.base.module.one.record.neone.parse.lo.v3;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efreight.base.common.core.enmus.LoModuleType;
import com.efreight.base.common.core.enmus.OneRecordParseVersionType;
import com.efreight.base.common.core.model.NeOneResolvedData;
import com.efreight.base.common.core.utils.CaacParseTransferTools;
import com.jayway.jsonpath.JsonPath;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author fu yuan hui
 * @since 2024-08-21 15:36:37 星期三
 */
public class FhlLoBodyMessageParse extends AbstractMessageBodyParse {
    @Override
    public boolean select(String body) {
        String type = JsonPath.using(CONFIG).parse(body).read("$.@type");
        List<String> wayBillTypes = JsonPath.using(CONFIG).parse(body).read("$.houseWaybills");

        return "cargo:Waybill".contains(type) && CollectionUtils.isNotEmpty(wayBillTypes);
    }

    @Override
    public NeOneResolvedData parse(String body) {
        NeOneResolvedData neOne = new NeOneResolvedData();
        String awbNumber = CaacParseTransferTools.resolveMasterCodeFromOneRecordBody(body, OneRecordParseVersionType.V3, LoModuleType.LOGISTICS_OBJECT);
        neOne.setMawbNumber(awbNumber);
        neOne.setMeta(NeOneResolvedData.Meta.builder().version(OneRecordParseVersionType.V3).eventType(LoModuleType.LOGISTICS_OBJECT).build());
        neOne.setContextType("FHL");
        neOne.setLoModuleType(LoModuleType.LOGISTICS_OBJECT.name());

        neOne.setDepPort(JsonPath.using(CONFIG).parse(body).read("$.departureLocation.locationCodes[0].code"));
        neOne.setArrPort(JsonPath.using(CONFIG).parse(body).read("$.arrivalLocation.locationCodes[0].code"));

        List<String> fhlNumbers = JsonPath.using(CONFIG).parse(body).read("$.houseWaybills[*].waybillNumber");
        neOne.setHawbNumber(String.join(",", fhlNumbers));
        return neOne;
    }
}
