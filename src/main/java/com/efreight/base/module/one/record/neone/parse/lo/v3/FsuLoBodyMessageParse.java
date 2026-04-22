package com.efreight.base.module.one.record.neone.parse.lo.v3;


import com.efreight.base.common.core.enmus.LoModuleType;
import com.efreight.base.common.core.enmus.OneRecordParseVersionType;
import com.efreight.base.common.core.model.NeOneResolvedData;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

/**
 * @author fu yuan hui
 * @since 2024-08-21 14:13:30 星期三
 *
 * FSU
 */
public class FsuLoBodyMessageParse extends AbstractMessageBodyParse {
    @Override
    public boolean select(String body) {
        return "cargo:LogisticsEvent".contains(JsonPath.using(CONFIG).parse(body).read("$.@type"));
    }

    @Override
    public NeOneResolvedData parse(String body) {
        NeOneResolvedData neOne = new NeOneResolvedData();
        DocumentContext dc = JsonPath.using(CONFIG).parse(body);
        String awbNumber = dc.read("$.eventFor.waybillPrefix") + "-" + dc.read("$.eventFor.waybillNumber");
        neOne.setMawbNumber(awbNumber);
        neOne.setMeta(NeOneResolvedData.Meta.builder().version(OneRecordParseVersionType.V3).eventType(LoModuleType.LOGISTICS_EVENT).build());

        String eventCode = dc.read("$.cargo:eventCode");
        neOne.setContextType("FSU:" + eventCode);
        neOne.setLoModuleType(LoModuleType.LOGISTICS_EVENT.name());

        DocumentContext parse = JsonPath.using(CONFIG).parse(body);
        String flightNumber = parse.read("$.eventFor.shipment.containedPieces[0].involvedInActions[0].servedActivity.transportIdentifier");
        neOne.setFlightNo(flightNumber);

        String pol = parse.read("$.eventFor.shipment.containedPieces[0].involvedInActions[0].servedActivity.departureLocation.locationCode");
        String pod = parse.read("$.eventFor.shipment.containedPieces[0].involvedInActions[0].servedActivity.arrivalLocation.locationCode");
        neOne.setDepPort(pol);
        neOne.setArrPort(pod);

        String flightDateString = parse.read("$.eventFor.shipment.containedPieces[0].involvedInActions[0].servedActivity.movementTimes[0].movementTimestamp");
        neOne.setFlightDate(flightDateString);
        return neOne;
    }
}
