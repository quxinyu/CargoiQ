package com.efreight.base.common.core.utils;

import cn.gov.caac.issp.utility.CaacParseTransfer;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.efreight.base.common.core.enmus.*;
import com.efreight.base.common.core.exception.OneRecordServerException;
import com.efreight.base.common.core.model.NeOneResolvedData;
import com.efreight.base.common.core.model.Resolved;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @author fu yuan hui
 * @since 2023-12-15 12:11:50 Friday
 *
 * 统一入口，方便调整
 */
@Slf4j
public final class CaacParseTransferTools {

    /**
     * 如果某一个key不存在，默认会报错，使用这个配置就不会报错，而是返回null
     */
    private static final Configuration CONF = Configuration.defaultConfiguration().addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);


    /**
     * 组报
     * @param jsonData
     * @param reportType
     */
    public static String combine(Map<String, Object> jsonData, String reportType) {
        try {
            return CaacParseTransfer.transfer(jsonData, reportType);
        } catch (Exception e) {
            log.error(">>>>>>>>>>>>>>>>>>组报失败，CODE: {}, 原始数据：{}， 原始类型: {}", "REPORT_TRANSFER_ERROR",jsonData, reportType);
        }

        return null;
    }

    @SuppressWarnings("all")
    public static Map<String, Object> parse(String parseBody, MessageDataType messageDataType, OneRecordParseVersionType oneType) {
        Map<String, Object> record;
        try {
            String body = convertParseBodyIfNecessary(parseBody);
            String resolveType = messageDataType != null ? messageDataType.getType() : MessageDataType.CARGO_IMP.getType();
            String resolveVersion = oneType == null ? OneRecordParseVersionType.V2.getType() : oneType.getType();

            log.info(">>>>>>>>>>>>>>>Caac工具包开始解析,解析版本： {}, 解析类型: {}, 原始报文: {}", resolveVersion, resolveType, body);
            record = CaacParseTransfer.parse(body, resolveType, "2", resolveVersion);
        } catch (Exception e) {
            log.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>IMP报文解析失败, 原始报文：{}", parseBody, e);
            throw new OneRecordServerException("Caac转换发生错误,请检查报文格式是否为标准格式,2.0版本支持FWB,FHL,FSU; 3.0版本支持FWB,FHL,FSU,FFR", e);
        }

        Object o = record.get("ResultCode");
        if (ObjectUtils.isEmpty(o) || !"1".equals(o)) {
            log.error(">>>>>>>>>>>>>>>>>>>>>>>>Caac解析报文返回错误，原始报文：{}, 解析的报文内容: {}", parseBody, record);
            throw new OneRecordServerException("Caac报文转换发生错误，请检查报文格式是否为标准格式,2.0版本支持FWB,FHL,FSU; 3.0版本支持FWB,FHL,FSU,FFR");
        }

        return record;
    }

    public static NeOneResolvedData resolve(String parseBody) {
        return resolve(parseBody, selectTypeByParseReportBody(parseBody), OneRecordParseVersionType.V3);
    }

    public static NeOneResolvedData resolve(String parseBody, MessageDataType messageDataType) {
        return resolve(parseBody, messageDataType, OneRecordParseVersionType.V3);
    }

    public static NeOneResolvedData resolve(String parseBody, OneRecordParseVersionType versionEnums) {
        return resolve(parseBody, selectTypeByParseReportBody(parseBody), versionEnums);
    }

    @SuppressWarnings("all")
    public static NeOneResolvedData resolve(String parseBody, MessageDataType messageDataType, OneRecordParseVersionType version) {
        String body = convertParseBodyIfNecessary(parseBody);
        if (messageDataType == null) {
            messageDataType = selectTypeByParseReportBody(parseBody);
        }
        version = version == null ? OneRecordParseVersionType.V3 : version;
        Map<String, Object> parse = parse(body, messageDataType, version);
        NeOneResolvedData baseRecord = new NeOneResolvedData();
        String msgType = toString(parse.get("MsgType"));
        Map<String, Object> dataMap = (Map<String, Object>) parse.get("DataMap");
        if (msgType.contains("FSU")) {
            baseRecord.setContextType("FSU:" + dataMap.get("FsuType"));
        } else {
            baseRecord.setContextType(msgType);
        }
        baseRecord.setMawbNumber(toString(parse.get("MawbCode")));
        if (msgType.contains("FHL")) {
            Object subNumber = dataMap.get("HawbCode");
            baseRecord.setHawbNumber(ObjectUtil.isNull(subNumber) ? null : subNumber.toString());
        }
        baseRecord.setDepPort(toString(parse.get("DepPort")));
        baseRecord.setArrPort(toString(parse.get("ArrPort")));
        baseRecord.setFlightNo(toString(parse.get("FlightNo")));
        baseRecord.setFlightDate(toString(parse.get("FlightDate")));
        baseRecord.setFirstMessageType(messageDataType.getType());
        String oneRecordJson = toString(parse.get("DataText"));
        if (org.apache.commons.lang3.StringUtils.isBlank(oneRecordJson)) {
            log.warn(">>>>>>>>>>>>>>>>>>>>>.当前报文类型是 ' " + msgType +"' 不支持OneRecord");
            return baseRecord;
        }

        baseRecord.setOneRecordJsonBody(oneRecordJson);
        baseRecord.setContextType(parseLoEventTypeAndVersion(oneRecordJson).getContextType());
        baseRecord.setCanResolveOneRecord(true);
        NeOneResolvedData.Meta eventRecord = new NeOneResolvedData.Meta();
        eventRecord.setEventType(msgType.contains("FSU") ? LoModuleType.LOGISTICS_EVENT : LoModuleType.LOGISTICS_OBJECT);
        eventRecord.setVersion(version);

        baseRecord.setMeta(eventRecord);
        baseRecord.setInputSourceReport(body);


        return baseRecord;
    }

    /**
     * 默认转成3.0
     */
    public static Map<String, Object> parse(String parseBody) {
        return parse(parseBody, selectTypeByParseReportBody(parseBody), OneRecordParseVersionType.V3);
    }

    public static Map<String, Object> parse(String parseBody, MessageDataType messageDataType) {
        return parse(parseBody, messageDataType, OneRecordParseVersionType.V3);
    }

    public static Map<String, Object> parse(String parseBody, OneRecordParseVersionType selector) {
        return parse(parseBody, selectTypeByParseReportBody(parseBody), selector);
    }

    public static String parseGetOneRecord(String parseBody) {
        return parseGetOneRecord(parseBody, selectTypeByParseReportBody(parseBody), OneRecordParseVersionType.V3);
    }

    public static String parseGetOneRecord(String parseBody, MessageDataType messageDataType) {
        return parseGetOneRecord(parseBody, messageDataType, OneRecordParseVersionType.V3);
    }

    public static String parseGetOneRecord(String parseBody, OneRecordParseVersionType selector) {
        return parseGetOneRecord(parseBody, selectTypeByParseReportBody(parseBody), selector);
    }

    public static String parseGetOneRecord(String parseBody, MessageDataType messageDataType, OneRecordParseVersionType oneType) {
        Map<String, Object> record = parse(parseBody, messageDataType, oneType);
        if (ObjectUtils.isNotEmpty(record.get("DataText"))) {
            return String.valueOf(record.get("DataText"));
        }

        log.error(">>>>>>>>>>>>>>>>没有发现可用的OneRecord数据，原始报文：{}", parseBody);
        throw new OneRecordServerException("没有发现OneRecord数据");
    }


    public static OneRecordParseVersionType parseOneRecordVersion(String oneRecordBody) {
        try {
            /*
               3.0 是cargo,  2.0 是iata
             */
            String cargo = JsonPath.using(CONF).parse(oneRecordBody).read("$.@context.cargo");
            return StringUtils.isNotBlank(cargo) ? OneRecordParseVersionType.V3 : OneRecordParseVersionType.V2;

        } catch (OneRecordServerException e) {
            throw e;
        } catch (Exception e) {
            log.error(">>>>>>>>>>>>>OneRecord JSON Body 解析发生错误", e);
            throw new IllegalArgumentException("无效的 OneRecord JSON Body");
        }
    }

    /**
     * 后面再用jsonpath 重写吧，暂时就先这样了
     * @param oneRecordBody
     * @return
     */
    public static NeOneResolvedData.Meta parseLoEventTypeAndVersion(String oneRecordBody) {
        NeOneResolvedData.Meta record = new NeOneResolvedData.Meta();
        try {
            List<Object> company = JsonPath.using(CONF).parse(oneRecordBody).read("$.cargo:contactPersons");
            if (!CollectionUtils.isEmpty(company)) {
                record.setEventType(LoModuleType.LOGISTICS_COMPANY);
                record.setVersion(OneRecordParseVersionType.V3);
                record.setContextType("Company");
                return record;
            }

            String type = JsonPath.using(CONF).parse(oneRecordBody).read("$.@type");
            if (StringUtils.isBlank(type)) {
                throw new OneRecordServerException("OneRecord报文格式错误, 没有发现@type节点");
            }

            OneRecordParseVersionType versionType = JsonPath.using(CONF).parse(oneRecordBody).read("$.@context.cargo") != null ? OneRecordParseVersionType.V3 : OneRecordParseVersionType.V2;
            //3.0
            if (OneRecordParseVersionType.V3 == versionType) {
                if ("cargo:LogisticsEvent".contains(type)) {
                    record.setEventType(LoModuleType.LOGISTICS_EVENT);
                } else if("cargo:Booking".contains(type)) {
                    record.setEventType(LoModuleType.LOGISTICS_BOOKING);
                } else {
                    record.setEventType(LoModuleType.LOGISTICS_OBJECT);
                }
                record.setVersion(OneRecordParseVersionType.V3);
                record.setContextType(type.replaceAll("cargo:", ""));
            } else {
                String loFlag = JsonPath.using(CONF).parse(oneRecordBody).read("$.iata:logisticsObject:companyIdentifier");
                record.setEventType(StringUtils.isNotBlank(loFlag) ? LoModuleType.LOGISTICS_OBJECT : LoModuleType.LOGISTICS_EVENT);
                record.setVersion(OneRecordParseVersionType.V2);
                record.setContextType(type.replaceAll("iata:", ""));
            }

            return record;

        } catch (OneRecordServerException e) {
            throw e;
        } catch (Exception e) {
            log.error(">>>>>>>>>>>>>OneRecord JSON Body 解析发生错误", e);
            throw new IllegalArgumentException("无效的 OneRecord JSON Body");
        }
    }


    /**
     * 提取主单号：这个都是基于CAAC 转换包来做的，并不完全符合oneRecord3.0官方文档的结构, 所以有可能主单号获取不到
     */
    public static String resolveMasterCodeFromOneRecordBody(String oneRecordBody, OneRecordParseVersionType selector, LoModuleType type) {
        if (OneRecordParseVersionType.V2 == selector) {
            String prefix = JsonPath.using(CONF).parse(oneRecordBody).read("$.iata:waybill:waybillPrefix");
            String waybillNumber = JsonPath.using(CONF).parse(oneRecordBody).read("$.iata:waybill:waybillNumber");
            if (org.apache.commons.lang3.StringUtils.isNotBlank(prefix) && StringUtils.isNotBlank(waybillNumber)) {
                return prefix + "-" + waybillNumber;
            }

        } else {
            //3.0的FSU
            String prefix;
            String suffix;
            if (LoModuleType.LOGISTICS_EVENT == type) {
                prefix = JsonPath.using(CONF).parse(oneRecordBody).read("$.eventFor.waybillPrefix");
                suffix = JsonPath.using(CONF).parse(oneRecordBody).read("$.eventFor.waybillNumber");
            } else {
                //3.0的 FWB, FHL, FFR
                prefix = JsonPath.using(CONF).parse(oneRecordBody).read("$.waybillPrefix");
                suffix = JsonPath.using(CONF).parse(oneRecordBody).read("$.waybillNumber");
            }
            return prefix + "-" + suffix;
        }

        throw new OneRecordServerException("OneRecord报文格式错误, 没有发现运单号");
    }

    private static String toString(Object data) {
        return data == null ? null : data.toString();
    }

    public static String convertParseBodyIfNecessary(String parseBody) {
        if (parseBody.startsWith("&lt;") && parseBody.endsWith("&gt;")) {
            return parseBody.replaceAll("&lt;", "<").replaceAll("&gt;", ">");
        }
        return parseBody;
    }

    private static MessageDataType selectTypeByParseReportBody(String parseBody) {
        String body = convertParseBodyIfNecessary(parseBody);
        if (body.startsWith("<")) {
            throw new OneRecordServerException("报文是XML格式，请选择是Cargo-XML 或者 CAAC-XML");
        }

        return MessageDataType.CARGO_IMP;
    }

    public static String resolveFsuEventType(String logisticsEventBody) {
        String type = JsonPath.using(CONF).parse(logisticsEventBody).read("$.@type");
        if (type.contains("cargo")) {
            //cc 向霞包是直接获取到FSU的code
            Object eventCode = JsonPath.using(CONF).parse(logisticsEventBody).read("$.cargo:eventCode");
            if (eventCode instanceof Map) {
                //官方的FSU
                eventCode = JsonPath.using(CONF).parse(logisticsEventBody).read("$.cargo:eventCode.cargo:code");
            }
            return eventCode.toString();
        } else if(type.contains("iata")) {
            return JsonPath.using(CONF).parse(logisticsEventBody).read("$.iata:waybill:booking.iata:booking:shipmentDetails.iata:shipment:containedPieces[0].iata:piece:event.iata:event:eventCode");
        }

        throw new OneRecordServerException("无法解析的 ne one logistics object event body");
    }

    public static Resolved caacResolve(String oneRecordJson) {
        Map record = CaacParseTransfer.parse(oneRecordJson, "OneRecord3", "2", "JSON");
        String msgType = toString(record.get("MsgType"));
        String mawbCode = toString(record.get("MawbCode"));
        String hawbCode = toString(record.get("HawbCode"));
        String depPort;
        String arrPort;
        String flightNo;
        String flightDate;
        String fsuTypeValue = null;
        String totalPieces = null;
        String totalWeight = null;


        Object dataMapObject = record.get("DataMap");
        if (dataMapObject != null) {
            Map<String, String> dataMap = (Map<String, String>)dataMapObject;
            depPort = dataMap.get("DepPort");
            arrPort = dataMap.get("ArrPort");
            flightNo = dataMap.get("FlightNo");
            flightDate = dataMap.get("FlightDate");
            fsuTypeValue = dataMap.get("FsuType");
            totalPieces = dataMap.get("TotalPieceQuantity");
            totalWeight = dataMap.get("TotalGrossWeight");
        } else {
            depPort = toString(record.get("DepPort"));
            arrPort = toString(record.get("ArrPort"));
            flightNo = toString(record.get("FlightNo"));
            flightDate = toString(record.get("FlightDate"));
            //无FsuType，件数，重量信息
        }

        Resolved resolved = new Resolved();
        resolved.setMawbCode(mawbCode);
        resolved.setHawbCode(hawbCode);
        resolved.setFlightNo(flightNo);
        resolved.setFlightDate(flightDate);
        resolved.setDepPort(depPort);
        resolved.setArrPort(arrPort);
        resolved.setTotalPieces(totalPieces);
        resolved.setTotalGrossWeight(totalWeight);
        resolved.setFsuTypeValue(fsuTypeValue);
        resolved.setMsgType(msgType);

        return resolved;
    }

    String test = "{\\n \\\"@context\\\": {\\n \\\"cargo\\\": \\\"https://onerecord.iata.org/ns/cargo#\\\"\\n },\\n \\\"@type\\\": \\\"Waybill\\\",\\n \\\"cargo:waybillType\\\": {\\n \\\"@id\\\": \\\"cargo:MASTER\\\"\\n }, \\\"cargo:waybillPrefix\\\": \\\"112\\\",\\n \\\"cargo:waybillNumber\\\": \\\"33894954\\\",\\n \\\"cargo:houseWaybills\\\": [\\n ],\\n \\\"cargo:arrivalLocation\\\": {\\n \\\"@type\\\": \\\"Location\\\",\\n \\\"cargo:locationCodes\\\": [\\n {\\n \\\"@type\\\": \\\"CodeListElement\\\",\\n \\\"cargo:code\\\": \\\"PEK\\\"\\n }\\n ],\\n \\\"cargo:locationType\\\": \\\"AIRPORT\\\"\\n },\\n \\\"cargo:departureLocation\\\": {\\n \\\"@type\\\": \\\"Location\\\",\\n \\\"cargo:locationCodes\\\": [\\n {\\n \\\"@type\\\": \\\"CodeListElement\\\",\\n \\\"cargo:code\\\": \\\"PVG\\\"\\n }\\n ],\\n \\\"cargo:locationType\\\": \\\"AIRPORT\\\"\\n },\\n \\\"cargo:involvedParties\\\": [\\n {\\n \\\"@type\\\": \\\"Party\\\",\\n \\\"cargo:partyDetails\\\": {\\n \\\"@type\\\": [\\n \\\"LogisticsAgent\\\",\\n \\\"Company\\\"\\n ],\\n \\\"cargo:name\\\": \\\"A\\\",\\n \\\"cargo:basedAtLocation\\\": {\\n \\\"@type\\\": \\\"Location\\\",\\n \\\"cargo:address\\\": {\\n \\\"@type\\\": \\\"cargo:address\\\",\\n \\\"cargo:country\\\": {\\n \\\"@type\\\": \\\"CodeListElement\\\",\\n \\\"cargo:code\\\": \\\"CN\\\"\\n },\\n \\\"cargo:cityCode\\\": {\\n \\\"@type\\\": \\\"CodeListElement\\\",\\n \\\"cargo:code\\\": \\\"AAA\\\"\\n },\\n \\\"cargo:streetAddressLines\\\": [\\n \\\"A\\\"\\n ]\\n }\\n }\\n ,\\\"cargo:contactPersons\\\": []\\n },\\n \\\"cargo:partyRole\\\": {\\n \\\"@type\\\": \\\"CodeListElement\\\",\\n \\\"cargo:code\\\": \\\"SHP\\\"\\n }\\n },\\n {\\n \\\"@type\\\": \\\"Party\\\",\\n \\\"cargo:partyDetails\\\": {\\n \\\"@type\\\": [\\n \\\"LogisticsAgent\\\",\\n \\\"Company\\\"\\n ],\\n \\\"cargo:name\\\": \\\"A\\\",\\n \\\"cargo:basedAtLocation\\\": {\\n \\\"@type\\\": \\\"Location\\\",\\n \\\"cargo:address\\\": {\\n \\\"@type\\\": \\\"cargo:address\\\",\\n \\\"cargo:country\\\": {\\n \\\"@type\\\": \\\"CodeListElement\\\",\\n \\\"cargo:code\\\": \\\"JP\\\"\\n },\\n \\\"cargo:cityCode\\\": {\\n \\\"@type\\\": \\\"CodeListElement\\\",\\n \\\"cargo:code\\\": \\\"A\\\"\\n },\\n \\\"cargo:streetAddressLines\\\": [\\n \\\"A\\\"\\n ]\\n }\\n }\\n ,\\\"cargo:contactPersons\\\": []\\n },\\n \\\"cargo:partyRole\\\": {\\n \\\"@type\\\": \\\"CodeListElement\\\",\\n \\\"cargo:code\\\": \\\"CNE\\\"\\n }\\n },\\n {\\n \\\"@type\\\": \\\"Party\\\",\\n \\\"cargo:partyDetails\\\": {\\n \\\"@type\\\": [\\n \\\"LogisticsAgent\\\",\\n \\\"Company\\\"\\n ],\\n \\\"cargo:contactPersons\\\": [\\n {\\n \\\"@type\\\": [\\n \\\"Actor\\\",\\n \\\"Person\\\"\\n ],\\n \\\"employeeId\\\": \\\"AGT\\\",\\n \\\"salutation\\\": \\\"A\\\"\\n }\\n ],\\n \\\"cargo:basedAtLocation\\\": {\\n \\\"@type\\\": \\\"Location\\\",\\n \\\"cargo:locationCodes\\\": [\\n {\\n \\\"@type\\\": \\\"CodeListElement\\\",\\n \\\"cargo:code\\\": \\\"PVG\\\"\\n }\\n ],\\n \\\"cargo:locationType\\\": \\\"AIRPORT\\\"\\n }\\n },\\n \\\"cargo:partyRole\\\": {\\n \\\"@type\\\": \\\"CodeListElement\\\",\\n \\\"cargo:code\\\": \\\"REF\\\"\\n }\\n }, {\\n \\\"@type\\\": \\\"Party\\\",\\n \\\"cargo:partyDetails\\\": {\\n \\\"@type\\\": [\\n \\\"LogisticsAgent\\\",\\n \\\"Company\\\"\\n ],\\n \\\"cargo:name\\\": \\\"A\\\",\\n \\\"cargo:basedAtLocation\\\": {\\n \\\"@type\\\": \\\"Location\\\",\\n \\\"cargo:address\\\": {\\n \\\"@type\\\": \\\"cargo:address\\\",\\n \\\"cargo:cityCode\\\": {\\n \\\"@type\\\": \\\"CodeListElement\\\",\\n \\\"cargo:code\\\": \\\"AAA\\\"\\n }\\n }\\n },\\n \\\"cargo:iataCargoAgentCode\\\": \\\"0000000\\\"\\n ,\\\"cargo:iataCargoAgentLocationIdentifier\\\": \\\"000\\\"\\n },\\n \\\"cargo:partyRole\\\": {\\n \\\"@type\\\": \\\"CodeListElement\\\",\\n \\\"cargo:code\\\": \\\"AGT\\\"\\n }\\n }\\n ],\\n \\\"cargo:waybillLineItems\\\": [\\n {\\n \\\"@type\\\": \\\"WaybillLineItem\\\",\\n \\\"cargo:lineItemNumber\\\": 1,\\n \\\"cargo:pieceCountForRate\\\": 17,\\n \\\"cargo:grossWeightForRate\\\": {\\n \\\"@type\\\": \\\"Value\\\",\\n \\\"cargo:numericalValue\\\": 2221,\\n \\\"cargo:unit\\\": {\\n \\\"@type\\\": \\\"CodeListElement\\\",\\n \\\"cargo:code\\\": \\\"KGM\\\"\\n }\\n },\\n \\\"cargo:rateClassCode\\\": {\\n \\\"@type\\\": \\\"CodeListElement\\\",\\n \\\"cargo:code\\\": \\\"Q\\\"\\n },\\n \\\"cargo:volumetricWeightForRate\\\": {\\n \\\"@type\\\": \\\"VolumetricWeight\\\",\\n \\\"cargo:chargeableWeight\\\": {\\n \\\"@type\\\": \\\"Value\\\",\\n \\\"cargo:numericalValue\\\": 0,\\n \\\"cargo:unit\\\": {\\n \\\"@type\\\": \\\"CodeListElement\\\",\\n \\\"cargo:code\\\": \\\"KGM\\\"\\n }\\n }\\n },\\n \\\"cargo:rateCharge\\\": {\\n \\\"@type\\\": \\\"Value\\\",\\n \\\"cargo:numericalValue\\\": \\n },\\n \\\"cargo:total\\\": {\\n \\\"@type\\\": \\\"Value\\\",\\n \\\"cargo:numericalValue\\\": \\n },\\n \\\"cargo:goodsDescriptionForRate\\\": \\\"CONSOLIDATED CARGO\\\"\\n }\\n ,{\\n \\\"@type\\\": \\\"WaybillLineItem\\\",\\n \\\"cargo:lineItemNumber\\\": 2,\\n \\\"cargo:dimensionsForRate\\\": {\\n \\\"cargo:volume\\\": {\\n \\\"@type\\\": \\\"Value\\\",\\n \\\"cargo:numericalValue\\\": 13.15,\\n \\\"cargo:unit\\\": {\\n \\\"@type\\\": \\\"CodeListElement\\\",\\n \\\"cargo:code\\\": \\\"MC\\\"\\n }\\n }\\n }\\n }\\n ],\\n \\\"cargo:otherCharges\\\": [\\n {\\n \\\"@type\\\": \\\"OtherCharge\\\",\\n \\\"cargo:chargePaymentType\\\": {\\n \\\"@type\\\": \\\"CodeListElement\\\",\\n \\\"cargo:code\\\": \\\"A\\\"\\n },\\n \\\"cargo:entitlement\\\": {\\n \\\"@type\\\": \\\"CodeListElement\\\",\\n \\\"cargo:code\\\": \\\"A\\\"\\n },\\n \\\"cargo:otherChargeCode\\\": {\\n \\\"@type\\\": \\\"CodeListElement\\\",\\n \\\"cargo:code\\\": \\\"WT\\\"\\n },\\n \\\"cargo:otherChargeAmount\\\": {\\n \\\"@type\\\": \\\"CurrencyValue\\\",\\n \\\"cargo:numericalValue\\\": \\\"1\\\"\\n }\\n },\\n {\\n \\\"@type\\\": \\\"OtherCharge\\\",\\n \\\"cargo:chargePaymentType\\\": {\\n \\\"@type\\\": \\\"CodeListElement\\\",\\n \\\"cargo:code\\\": \\\"A\\\"\\n },\\n \\\"cargo:entitlement\\\": {\\n \\\"@type\\\": \\\"CodeListElement\\\",\\n \\\"cargo:code\\\": \\\"A\\\"\\n },\\n \\\"cargo:otherChargeCode\\\": {\\n \\\"@type\\\": \\\"CodeListElement\\\",\\n \\\"cargo:code\\\": \\\"OC\\\"\\n },\\n \\\"cargo:otherChargeAmount\\\": {\\n \\\"@type\\\": \\\"CurrencyValue\\\",\\n \\\"cargo:numericalValue\\\": \\\"\\\"\\n }\\n },\\n {\\n \\\"@type\\\": \\\"OtherCharge\\\",\\n \\\"cargo:chargePaymentType\\\": {\\n \\\"@type\\\": \\\"CodeListElement\\\",\\n \\\"cargo:code\\\": \\\"A\\\"\\n },\\n \\\"cargo:entitlement\\\": {\\n \\\"@type\\\": \\\"CodeListElement\\\",\\n \\\"cargo:code\\\": \\\"A\\\"\\n },\\n \\\"cargo:otherChargeCode\\\": {\\n \\\"@type\\\": \\\"CodeListElement\\\",\\n \\\"cargo:code\\\": \\\"CT\\\"\\n },\\n \\\"cargo:otherChargeAmount\\\": {\\n \\\"@type\\\": \\\"CurrencyValue\\\",\\n \\\"cargo:numericalValue\\\": \\\"1\\\"\\n }\\n }\\n ]\\n ,\\\"cargo:shipment\\\": {\\n \\\"@type\\\": \\\"cargo:shipment\\\",\\n \\\"cargo:pieces\\\": [\\n {\\n \\\"@type\\\": \\\"Piece\\\",\\n \\\"cargo:containedItems\\\": [\\n {\\n \\\"@type\\\": \\\"Item\\\",\\n \\\"cargo:itemQuantity\\\": {\\n \\\"@type\\\": \\\"Value\\\",\\n \\\"cargo:numericalValue\\\": 17\\n } }\\n ],\\n \\\"cargo:involvedInActions\\\": [\\n {\\n \\\"@type\\\": [\\n \\\"Loading\\\",\\n \\\"LogisticsAction\\\"\\n ],\\n \\\"cargo:servedActivity\\\": {\\n \\\"@type\\\": [\\n \\\"TransportMovement\\\",\\n \\\"LogisticsActivity\\\"\\n ],\\n \\\"cargo:arrivalLocation\\\": {\\n \\\"@type\\\": \\\"Location\\\",\\n \\\"cargo:locationCodes\\\": [\\n {\\n \\\"@type\\\": \\\"CodeListElement\\\",\\n \\\"cargo:code\\\": \\\"NRT\\\"\\n }\\n ]\\n },\\n \\\"cargo:departureLocation\\\": {\\n \\\"@type\\\": \\\"Location\\\",\\n \\\"cargo:locationCodes\\\": [\\n {\\n \\\"@type\\\": \\\"CodeListElement\\\",\\n \\\"cargo:code\\\": \\\"PVG\\\"\\n }\\n ]\\n }\\n ,\\\"cargo:movementTimes\\\": [\\n {\\n \\\"@type\\\": \\\"movementTime\\\",\\n \\\"cargo:direction\\\": \\\"OUTBOUND\\\",\\n \\\"cargo:movementTimeType\\\": \\\"ESTIMATED\\\",\\n \\\"cargo:movementMilestone\\\": \\\"AD\\\",\\n \\\"cargo:movementTimestamp\\\": \\\"2025-12-19T00:00:00.000Z\\\"\\n }\\n ]\\n ,\\\"cargo:transportIdentifier\\\": \\\"KZ0228\\\"\\n ,\\\"cargo:modeCode\\\": {\\n \\\"@type\\\": \\\"CodeListElement\\\",\\n \\\"cargo:code\\\": \\\"AIR_TRANSPORT\\\"\\n } }\\n }\\n ]\\n }\\n ],\\n \\\"cargo:totalGrossWeight\\\": {\\n \\\"@type\\\": \\\"Value\\\",\\n \\\"cargo:numericalValue\\\": 2221,\\n \\\"cargo:unit\\\": {\\n \\\"@type\\\": \\\"CodeListElement\\\",\\n \\\"cargo:code\\\": \\\"KGM\\\"\\n }\\n }\\n ,\\\"cargo:totalDimensions\\\": [\\n {\\n \\\"@type\\\": \\\"Dimensions\\\",\\n \\\"cargo:volume\\\": {\\n \\\"@type\\\": \\\"Value\\\",\\n \\\"cargo:numericalValue\\\": 13.15,\\n \\\"cargo:unit\\\": {\\n \\\"@type\\\": \\\"CodeListElement\\\",\\n \\\"cargo:code\\\": \\\"MC\\\"\\n }\\n }\\n }\\n ]\\n },\\n \\\"cargo:originCurrency\\\": {\\n \\\"@type\\\": \\\"CodeListElement\\\",\\n \\\"cargo:code\\\": \\\"AAA\\\"\\n },\\n \\\"cargo:otherChargesIndicator\\\": {\\n \\\"@type\\\": \\\"CodeListElement\\\",\\n \\\"cargo:code\\\": \\\"A\\\"\\n },\\n \\\"cargo:weightValuationIndicator\\\": {\\n \\\"@type\\\": \\\"CodeListElement\\\",\\n \\\"cargo:code\\\": \\\"A\\\"\\n },\\n \\\"cargo:accountingInformation\\\": \\\"FREIGHT PREPAID\\\",\\n \\\"cargo:carrierDeclarationDate\\\": \\\"2025-12-18T00:00:00.000Z\\\",\\n \\\"cargo:carrierDeclarationPlace\\\": \\\"A\\\",\\n \\\"cargo:carrierDeclarationSignature\\\": \\\"\\\",\\n \\\"cargo:consignorDeclarationSignature\\\": \\\"A\\\"\\n}";

}
