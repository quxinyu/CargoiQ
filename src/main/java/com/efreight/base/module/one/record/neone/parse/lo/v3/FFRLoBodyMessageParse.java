package com.efreight.base.module.one.record.neone.parse.lo.v3;

import com.efreight.base.common.core.enmus.LoModuleType;
import com.efreight.base.common.core.enmus.OneRecordParseVersionType;
import com.efreight.base.common.core.model.NeOneResolvedData;
import com.efreight.base.common.core.utils.CaacParseTransferTools;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

/**
 * @author fu yuan hui
 * @since 2024-08-21 14:12:22 星期三
 *
 *
 * FFR
 */
public class FFRLoBodyMessageParse extends AbstractMessageBodyParse {

    @Override
    public boolean select(String body) {
        return "cargo:Booking".contains(JsonPath.using(CONFIG).parse(body).read("$.@type"));
    }

    @Override
    public NeOneResolvedData parse(String body) {
        NeOneResolvedData neOne = new NeOneResolvedData();
        String awbNumber = CaacParseTransferTools.resolveMasterCodeFromOneRecordBody(body, OneRecordParseVersionType.V3, LoModuleType.LOGISTICS_OBJECT);
        neOne.setMawbNumber(awbNumber);
        neOne.setMeta(NeOneResolvedData.Meta.builder().version(OneRecordParseVersionType.V3).eventType(LoModuleType.LOGISTICS_OBJECT).build());
        neOne.setContextType("FFR");
        neOne.setLoModuleType(LoModuleType.LOGISTICS_BOOKING.name());

        /*
          以下数据都只取了第一段航程的
         */
        DocumentContext dc = JsonPath.using(CONFIG).parse(body);
        String pol = dc.read("$.bookingRequest.bookingOption.transportLegs[0].departureLocation.code");
        String pod = dc.read("$.bookingRequest.bookingOption.transportLegs[0].arrivalLocation.code");
        String flightNumber = dc.read("$.bookingRequest.bookingOption.transportLegs[0].transportIdentifier");
        String flightDateString = dc.read("$.bookingRequest.bookingOption.transportLegs[0].departureDate");


        neOne.setDepPort(pol);
        neOne.setArrPort(pod);
        neOne.setFlightNo(flightNumber);
        neOne.setFlightDate(flightDateString);
        return neOne;
    }
}
