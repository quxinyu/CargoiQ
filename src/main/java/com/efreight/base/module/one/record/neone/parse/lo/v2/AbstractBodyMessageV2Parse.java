package com.efreight.base.module.one.record.neone.parse.lo.v2;

import com.alibaba.fastjson.JSONObject;
import com.efreight.base.common.core.model.NeOneResolvedData;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author fu yuan hui
 * @since 2024-08-21 17:29:01 星期三
 */
public abstract class AbstractBodyMessageV2Parse implements LoBodyMessageV2Parser {

    private static final String REGEX= "(\\d{4}-[0-9]{1,2}-[0-9]{1,2})";
    private static final Pattern FLIGHT_DATE_PATTERN = Pattern.compile(REGEX);

    protected NeOneResolvedData resolveMasterCode(JSONObject dataObject) {
        NeOneResolvedData lo = new NeOneResolvedData();
        String prefix = dataObject.getString("iata:waybill:waybillPrefix");
        String number = dataObject.getString("iata:waybill:waybillNumber");
        String awbNumber = prefix + "-" + number;
        lo.setMawbNumber(awbNumber);

        return lo;
    }

    protected void resolveLocation(NeOneResolvedData lo, JSONObject transportObject) {
        if (transportObject != null) {
            JSONObject departureLocationObject = transportObject.getJSONObject("iata:transportMovement:departureLocation");
            if (departureLocationObject != null) {
                lo.setDepPort(departureLocationObject.getString("iata:location:code"));
            }
            JSONObject arrivalLocationObject = transportObject.getJSONObject("iata:transportMovement:arrivalLocation");
            if (arrivalLocationObject != null) {
                lo.setArrPort(arrivalLocationObject.getString("iata:location:code"));
            }
        }
    }


    protected String parseFlightDate(String flightDate) {
        Matcher matcher = FLIGHT_DATE_PATTERN.matcher(flightDate);

        // 查找并打印匹配结果
        if (matcher.find()) {
            return matcher.group(1);
        }

        return flightDate;
    }
}
