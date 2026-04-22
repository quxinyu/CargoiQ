package com.efreight.base.module.one.record.neone.parse.lo.v2;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efreight.base.common.core.enmus.LoModuleType;
import com.efreight.base.common.core.enmus.OneRecordParseVersionType;
import com.efreight.base.common.core.model.NeOneResolvedData;

/**
 * @author fu yuan hui
 * @since 2024-08-21 14:13:30 星期三
 *
 * FSU
 */
public class FsuLoBodyMessageV2Parse extends AbstractBodyMessageV2Parse {
    @Override
    public boolean select(JSONObject select) {
        return !select.containsKey("iata:logisticsObject:companyIdentifier") && !select.containsKey("iata:waybill:containedWaybill");
    }

    @Override
    public NeOneResolvedData parse(JSONObject jsonObject) {
        NeOneResolvedData lo = super.resolveMasterCode(jsonObject);
        lo.setMeta(NeOneResolvedData.Meta.builder().version(OneRecordParseVersionType.V2).eventType(LoModuleType.LOGISTICS_EVENT).build());

        JSONObject bookingObject = jsonObject.getJSONObject("iata:waybill:booking");
        JSONObject transportObject = bookingObject.getJSONObject("iata:booking:transportMovement");
        this.resolveLocation(lo, transportObject);


        JSONObject shipmentObject = bookingObject.getJSONObject("iata:booking:shipmentDetails");
        if (shipmentObject != null) {
            JSONArray containPieceArray = shipmentObject.getJSONArray("iata:shipment:containedPieces");
            if (containPieceArray != null && !containPieceArray.isEmpty()) {
                //这里暂时只取第一个
                JSONObject first = containPieceArray.getJSONObject(0);
                JSONObject eventObject = first.getJSONObject("iata:piece:event");
                if (eventObject != null) {
                    lo.setContextType("FSU:" + eventObject.getString("iata:event:eventCode"));
                }
            }
        }

        return lo;

    }

    @Override
    protected void resolveLocation(NeOneResolvedData lo, JSONObject transportObject) {
        if (transportObject != null) {
            JSONObject departureLocationObject = transportObject.getJSONObject("iata:transportSegment:departureLocation");
            if (departureLocationObject != null) {
                lo.setDepPort(departureLocationObject.getString("iata:location:code"));
            }
            JSONObject arrivalLocationObject = transportObject.getJSONObject("iata:transportSegment:arrivalLocation");
            if (arrivalLocationObject != null) {
                lo.setArrPort(arrivalLocationObject.getString("iata:location:code"));
            }
        }
    }
}
