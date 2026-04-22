package com.efreight.base.module.one.record.neone.parse.lo.v2;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efreight.base.common.core.enmus.LoModuleType;
import com.efreight.base.common.core.enmus.OneRecordParseVersionType;
import com.efreight.base.common.core.model.NeOneResolvedData;

/**
 * @author fu yuan hui
 * @since 2024-08-21 14:08:47 星期三
 *
 * FWB/FHL
 */
public class FwbLoBodyMessageV2Parse extends AbstractBodyMessageV2Parse {

    @Override
    public boolean select(JSONObject select) {
        return select.containsKey("iata:logisticsObject:companyIdentifier") && !select.containsKey("iata:waybill:containedWaybill");
    }

    @Override
    public NeOneResolvedData parse(JSONObject jsonObject) {
        NeOneResolvedData lo = super.resolveMasterCode(jsonObject);
        lo.setMeta(NeOneResolvedData.Meta.builder().version(OneRecordParseVersionType.V2).eventType(LoModuleType.LOGISTICS_OBJECT).build());
        lo.setContextType("FWB");


        JSONObject bookingObject = jsonObject.getJSONObject("iata:waybill:booking");
        JSONObject transportObject = bookingObject.getJSONObject("iata:booking:transportMovement");
        super.resolveLocation(lo, transportObject);

        JSONObject shipmentObject = bookingObject.getJSONObject("iata:booking:shipmentDetails");
        JSONArray piecesArray = shipmentObject.getJSONArray("iata:shipment:containedPieces");
        if (piecesArray != null && !piecesArray.isEmpty()) {
            //暂时取第一个
            JSONObject firstObject = piecesArray.getJSONObject(0);
            JSONArray segmentArray = firstObject.getJSONArray("iata:piece:transportSegments");
            if (segmentArray != null && !segmentArray.isEmpty()) {
                for (int i = 0; i < segmentArray.size(); i++) {
                    JSONObject itemObject = segmentArray.getJSONObject(i);
                    if (itemObject != null
                            && itemObject.containsKey("iata:transportSegment:transportIdentifier")
                            && itemObject.containsKey("iata:transportSegment:departureDate")) {
                        String flightNumber = itemObject.getString("iata:transportSegment:transportIdentifier");
                        lo.setFlightNo(flightNumber);

                        String flightDate = itemObject.getString("iata:transportSegment:departureDate");
                        lo.setFlightDate(super.parseFlightDate(flightDate));
                    }
                }
            }

        }
        return lo;
    }

}
