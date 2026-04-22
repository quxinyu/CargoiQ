package com.efreight.base.module.one.record.neone.parse.lo.v3;


import com.efreight.base.common.core.enmus.LoModuleType;
import com.efreight.base.common.core.enmus.OneRecordParseVersionType;
import com.efreight.base.common.core.model.NeOneResolvedData;
import com.efreight.base.common.core.utils.CaacParseTransferTools;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * @author fu yuan hui
 * @since 2024-08-21 14:08:47 星期三
 * <p>
 * FWB
 */
public class FwbLoBodyMessageParse extends AbstractMessageBodyParse {

    @Override
    public boolean select(String body) {
        String type = JsonPath.using(CONFIG).parse(body).read("$.@type");
        List<String> wayBillTypes = JsonPath.using(CONFIG).parse(body).read("$.houseWaybills");
        return "cargo:Waybill".contains(type) && CollectionUtils.isEmpty(wayBillTypes);
    }

    @Override
    public NeOneResolvedData parse(String body) {
        NeOneResolvedData neOne = new NeOneResolvedData();
        String awbNumber = CaacParseTransferTools.resolveMasterCodeFromOneRecordBody(body, OneRecordParseVersionType.V3, LoModuleType.LOGISTICS_OBJECT);
        DocumentContext parse = JsonPath.using(CONFIG).parse(body);
        String flightNumber = parse.read("$.shipment.pieces[0].involvedInActions[0].servedActivity.transportIdentifier");
        String flightDateString = parse.read("$.shipment.pieces[0].involvedInActions[0].servedActivity.movementTimes[0].movementTimestamp");

        neOne.setMawbNumber(awbNumber);
        neOne.setDepPort(JsonPath.using(CONFIG).parse(body).read("$.departureLocation.locationCodes[0].code"));
        neOne.setArrPort(JsonPath.using(CONFIG).parse(body).read("$.arrivalLocation.locationCodes[0].code"));
        neOne.setContextType("FWB");
        neOne.setFlightNo(flightNumber);
        neOne.setFlightDate(flightDateString);
        neOne.setLoModuleType(LoModuleType.LOGISTICS_OBJECT.name());
        neOne.setMeta(NeOneResolvedData.Meta.builder().version(OneRecordParseVersionType.V3).eventType(LoModuleType.LOGISTICS_OBJECT).build());

        return neOne;

    }
}
