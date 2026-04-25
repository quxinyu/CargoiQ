package com.efreight.base.module.one.record.neone.test;

import cn.gov.caac.issp.utility.CaacParseTransfer;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.efreight.base.module.one.record.neone.helper.IriGenerator;
import com.efreight.base.module.one.record.neone.model.entity.NeOneShipmentData;
import com.efreight.base.module.one.record.neone.model.onerecord.LogisticsEventFSU;
import com.efreight.base.module.one.record.neone.utils.LogisticsEventUtils;
import com.efreight.base.module.one.record.neone.utils.UUIDTools;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParseTest {

    static String test = "[\n" +
            "    {\n" +
            "        \"functionType\": \"TP\",\n" +
            "        \"pushContent\": [\n" +
            "            \"FSU/14\\n921-10667893HGHLAX/T30K30.12\\nBKD/O31698/08APR/HGHLAX/T30K30.12/S2050/S0830\"\n" +
            "        ],\n" +
            "        \"dataType\": \"CARGO_FSU\",\n" +
            "        \"channelName\": \"CBS_BOOKING\",\n" +
            "        \"channelCode\": \"system\",\n" +
            "        \"extendData\": \"383785\"\n" +
            "    }\n" +
            "]";

    static String fwb = "FWB/17\n" +
            "\n" +
            "784-30791202PVGAMS/T95K1667\n" +
            "\n" +
            "FLT/CZ495/24\n" +
            "\n" +
            "RTG/AMSCZ\n" +
            "\n" +
            "SHP\n" +
            "\n" +
            "NAM/SINOTRANS EXPRESS CO LTD\n" +
            "\n" +
            "ADR/NO 600 HAITIAN 5TH ROAD\n" +
            "\n" +
            "LOC/SHANGHAI\n" +
            "\n" +
            "/CN/201202/TE/18187314868\n" +
            "\n" +
            "CNE\n" +
            "\n" +
            "NAM/VIAEUROPE\n" +
            "\n" +
            "ADR/PUDONGWEG 21\n" +
            "\n" +
            "LOC/AMSTERDAM\n" +
            "\n" +
            "/NL/1437/TE/310204057460\n" +
            "\n" +
            "AGT//0832237\n" +
            "\n" +
            "/CED SHA\n" +
            "\n" +
            "/SHA\n" +
            "\n" +
            "SSR/CED NO SOLID WOOD PACKING MATERIALS\n" +
            "\n" +
            "ACC/GEN/FREIGHT PREPAID 08322372\n" +
            "\n" +
            "CVD/CNY//PP/NVD/NCV/XXX\n" +
            "\n" +
            "RTD/1/P95/K1667/CQ/W1667/R46.52/T77548.84\n" +
            "\n" +
            "/NG/WOMEN S CLOTHES\n" +
            "\n" +
            "/2/NH/620442\n" +
            "\n" +
            "/3/NV/MC10\n" +
            "\n" +
            "/4/ND//CMT55-45-42/94\n" +
            "\n" +
            "/5/ND//CMT65-64-55/1\n" +
            "\n" +
            "OTH/P/CGC10\n" +
            "\n" +
            "/P/MSC2000.4\n" +
            "\n" +
            "/P/AWC50\n" +
            "\n" +
            "PPD/WT77548.84\n" +
            "\n" +
            "/OC2060.4/CT79609.24\n" +
            "\n" +
            "CER/CED SHA\n" +
            "\n" +
            "ISU/23DEC24/PVG/CED\n" +
            "\n" +
            "REF///AGT/CEDSHA/PVG\n" +
            "\n" +
            "SPH/EAW/BUP/XPS";

    static String input = "{\"@type\": \"Waybill\", \"@context\": {\"cargo\": \"https://onerecord.iata.org/ns/cargo#\"}, \"cargo:shipment\": {\"@type\": \"cargo:shipment\", \"cargo:pieces\": [{\"@type\": \"Piece\", \"cargo:containedItems\": [{\"@type\": \"Item\", \"cargo:itemQuantity\": {\"@type\": \"Value\", \"cargo:numericalValue\": 111}}], \"cargo:involvedInActions\": [{\"@type\": [\"Loading\", \"LogisticsAction\"], \"cargo:servedActivity\": {\"@type\": [\"TransportMovement\", \"LogisticsActivity\"], \"cargo:modeCode\": {\"@type\": \"CodeListElement\", \"cargo:code\": \"AIR_TRANSPORT\"}, \"cargo:movementTimes\": [{\"@type\": \"movementTime\", \"cargo:direction\": \"OUTBOUND\", \"cargo:movementTimeType\": \"ESTIMATED\", \"cargo:movementMilestone\": \"AD\", \"cargo:movementTimestamp\": \"2024-08-13T00:00:00.000Z\"}], \"cargo:arrivalLocation\": {\"@type\": \"Location\", \"cargo:locationCodes\": [{\"@type\": \"CodeListElement\", \"cargo:code\": \"TNA\"}]}, \"cargo:departureLocation\": {\"@type\": \"Location\", \"cargo:locationCodes\": [{\"@type\": \"CodeListElement\", \"cargo:code\": \"GHH\"}]}, \"cargo:transportIdentifier\": \"K4996\"}}, {\"@type\": [\"Loading\", \"LogisticsAction\"], \"cargo:servedActivity\": {\"@type\": [\"TransportMovement\", \"LogisticsActivity\"], \"cargo:modeCode\": {\"@type\": \"CodeListElement\", \"cargo:code\": \"AIR_TRANSPORT\"}, \"cargo:movementTimes\": [{\"@type\": \"movementTime\", \"cargo:direction\": \"OUTBOUND\", \"cargo:movementTimeType\": \"ESTIMATED\", \"cargo:movementMilestone\": \"AD\", \"cargo:movementTimestamp\": \"2024-08-15T00:00:00.000Z\"}], \"cargo:arrivalLocation\": {\"@type\": \"Location\", \"cargo:locationCodes\": [{\"@type\": \"CodeListElement\", \"cargo:code\": \"HGH\"}]}, \"cargo:departureLocation\": {\"@type\": \"Location\", \"cargo:locationCodes\": [{\"@type\": \"CodeListElement\", \"cargo:code\": \"TNA\"}]}, \"cargo:transportIdentifier\": \"K4998\"}}, {\"@type\": [\"Loading\", \"LogisticsAction\"], \"cargo:servedActivity\": {\"@type\": [\"TransportMovement\", \"LogisticsActivity\"], \"cargo:arrivalLocation\": {\"@type\": \"Location\", \"cargo:locationCodes\": [{\"@type\": \"CodeListElement\", \"cargo:code\": \"PEK\"}]}, \"cargo:departureLocation\": {\"@type\": \"Location\", \"cargo:locationCodes\": [{\"@type\": \"CodeListElement\", \"cargo:code\": \"HGH\"}]}}}]}], \"cargo:totalDimensions\": [{\"@type\": \"Dimensions\", \"cargo:volume\": {\"@type\": \"Value\", \"cargo:unit\": {\"@type\": \"CodeListElement\", \"cargo:code\": \"MC\"}, \"cargo:numericalValue\": 10.05}}], \"cargo:totalGrossWeight\": {\"@type\": \"Value\", \"cargo:unit\": {\"@type\": \"CodeListElement\", \"cargo:code\": \"KGM\"}, \"cargo:numericalValue\": 27627}, \"cargo:specialHandlingCodes\": [{\"@type\": \"CodeListElement\", \"cargo:code\": \"SHR\"}, {\"@type\": \"CodeListElement\", \"cargo:code\": \"EAP\"}], \"cargo:textualHandlingInstructions\": [\"HANDLING INFO\", \"OTHER SERVICE INFOMATION\"]}, \"cargo:waybillType\": {\"@id\": \"cargo:MASTER\"}, \"cargo:otherCharges\": [{\"@type\": \"OtherCharge\", \"cargo:entitlement\": {\"@type\": \"CodeListElement\", \"cargo:code\": \"A\"}, \"cargo:otherChargeCode\": {\"@type\": \"CodeListElement\", \"cargo:code\": \"WT\"}, \"cargo:chargePaymentType\": {\"@type\": \"CodeListElement\", \"cargo:code\": \"P\"}, \"cargo:otherChargeAmount\": {\"@type\": \"CurrencyValue\", \"cargo:numericalValue\": \"969\"}}, {\"@type\": \"OtherCharge\", \"cargo:entitlement\": {\"@type\": \"CodeListElement\", \"cargo:code\": \"A\"}, \"cargo:otherChargeCode\": {\"@type\": \"CodeListElement\", \"cargo:code\": \"OC\"}, \"cargo:chargePaymentType\": {\"@type\": \"CodeListElement\", \"cargo:code\": \"P\"}, \"cargo:otherChargeAmount\": {\"@type\": \"CurrencyValue\", \"cargo:numericalValue\": \"\"}}, {\"@type\": \"OtherCharge\", \"cargo:entitlement\": {\"@type\": \"CodeListElement\", \"cargo:code\": \"A\"}, \"cargo:otherChargeCode\": {\"@type\": \"CodeListElement\", \"cargo:code\": \"CT\"}, \"cargo:chargePaymentType\": {\"@type\": \"CodeListElement\", \"cargo:code\": \"P\"}, \"cargo:otherChargeAmount\": {\"@type\": \"CurrencyValue\", \"cargo:numericalValue\": \"2311.00\"}}], \"cargo:houseWaybills\": [], \"cargo:waybillNumber\": \"72685281\", \"cargo:waybillPrefix\": \"618\", \"cargo:originCurrency\": {\"@type\": \"CodeListElement\", \"cargo:code\": \"USD\"}, \"cargo:arrivalLocation\": {\"@type\": \"Location\", \"cargo:locationType\": \"AIRPORT\", \"cargo:locationCodes\": [{\"@type\": \"CodeListElement\", \"cargo:code\": \"LAX\"}]}, \"cargo:involvedParties\": [{\"@type\": \"Party\", \"cargo:partyRole\": {\"@type\": \"CodeListElement\", \"cargo:code\": \"SHP\"}, \"cargo:partyDetails\": {\"@type\": [\"LogisticsAgent\", \"Company\"], \"cargo:name\": \"DHL GLOBAL FORWARDING CHICAGO\", \"cargo:contactPersons\": [{\"@type\": [\"Actor\", \"Person\"], \"cargo:contactDetails\": [{\"@type\": \"ContactDetail\", \"cargo:textualValue\": \"18472337900\", \"cargo:contactDetailType\": {\"@type\": \"CodeListElement\", \"cargo:code\": \"PHONE_NUMBER\"}}]}], \"cargo:basedAtLocation\": {\"@type\": \"Location\", \"cargo:address\": {\"@type\": \"cargo:address\", \"cargo:country\": {\"@type\": \"CodeListElement\", \"cargo:code\": \"US\"}, \"cargo:cityCode\": {\"@type\": \"CodeListElement\", \"cargo:code\": \"CHICAGO\"}, \"cargo:postalCode\": {\"@type\": \"CodeListElement\", \"cargo:code\": \"60666\"}, \"cargo:regionCode\": {\"@type\": \"CodeListElement\", \"cargo:code\": \"BC\"}, \"cargo:streetAddressLines\": [\"11601 W TOUHY AVENUE BLDG 895\"]}}}}, {\"@type\": \"Party\", \"cargo:partyRole\": {\"@type\": \"CodeListElement\", \"cargo:code\": \"CNE\"}, \"cargo:partyDetails\": {\"@type\": [\"LogisticsAgent\", \"Company\"], \"cargo:name\": \"DHL GLOBAL FORWARDING CHINA\", \"cargo:contactPersons\": [{\"@type\": [\"Actor\", \"Person\"], \"cargo:contactDetails\": [{\"@type\": \"ContactDetail\", \"cargo:textualValue\": \"861069448288\", \"cargo:contactDetailType\": {\"@type\": \"CodeListElement\", \"cargo:code\": \"PHONE_NUMBER\"}}]}], \"cargo:basedAtLocation\": {\"@type\": \"Location\", \"cargo:address\": {\"@type\": \"cargo:address\", \"cargo:country\": {\"@type\": \"CodeListElement\", \"cargo:code\": \"CN\"}, \"cargo:cityCode\": {\"@type\": \"CodeListElement\", \"cargo:code\": \"BEIJING\"}, \"cargo:postalCode\": {\"@type\": \"CodeListElement\", \"cargo:code\": \"3233\"}, \"cargo:streetAddressLines\": [\"7F EAST ZONE NO 566 SHUNPING RD\"]}}}}, {\"@type\": \"Party\", \"cargo:partyRole\": {\"@type\": \"CodeListElement\", \"cargo:code\": \"REF\"}, \"cargo:partyDetails\": {\"@type\": [\"LogisticsAgent\", \"Company\"], \"cargo:contactPersons\": [{\"@type\": [\"Actor\", \"Person\"], \"employeeId\": \"AGT\", \"salutation\": \"AGENTINFO\"}], \"cargo:basedAtLocation\": {\"@type\": \"Location\", \"cargo:locationType\": \"AIRPORT\", \"cargo:locationCodes\": [{\"@type\": \"CodeListElement\", \"cargo:code\": \"GHH\"}]}}}, {\"@type\": \"Party\", \"cargo:partyRole\": {\"@type\": \"CodeListElement\", \"cargo:code\": \"AGT\"}, \"cargo:partyDetails\": {\"@type\": [\"LogisticsAgent\", \"Company\"], \"cargo:name\": \"AGENT INFO\", \"cargo:basedAtLocation\": {\"@type\": \"Location\", \"cargo:address\": {\"@type\": \"cargo:address\", \"cargo:cityCode\": {\"@type\": \"CodeListElement\", \"cargo:code\": \"ORD\"}}}, \"cargo:otherIdentifiers\": [{\"@type\": \"OtherIdentifier\", \"cargo:textualValue\": \"3323ED\", \"cargo:otherIdentifierType\": \"ACCOUNT NUMBER\"}], \"cargo:iataCargoAgentCode\": \"1111111\", \"cargo:iataCargoAgentLocationIdentifier\": \"1234\"}}], \"cargo:waybillLineItems\": [{\"@type\": \"WaybillLineItem\", \"cargo:total\": {\"@type\": \"Value\", \"cargo:numericalValue\": 969}, \"cargo:rateCharge\": {\"@type\": \"Value\", \"cargo:numericalValue\": 3}, \"cargo:rateClassCode\": {\"@type\": \"CodeListElement\", \"cargo:code\": \"Q\"}, \"cargo:lineItemNumber\": 1, \"cargo:pieceCountForRate\": 111, \"cargo:grossWeightForRate\": {\"@type\": \"Value\", \"cargo:unit\": {\"@type\": \"CodeListElement\", \"cargo:code\": \"KGM\"}, \"cargo:numericalValue\": 27627}, \"cargo:goodsDescriptionForRate\": \"CONSOL\", \"cargo:volumetricWeightForRate\": {\"@type\": \"VolumetricWeight\", \"cargo:chargeableWeight\": {\"@type\": \"Value\", \"cargo:unit\": {\"@type\": \"CodeListElement\", \"cargo:code\": \"KGM\"}, \"cargo:numericalValue\": 323}}}, {\"@type\": \"WaybillLineItem\", \"cargo:slacForRate\": 4, \"cargo:lineItemNumber\": 2}, {\"@type\": \"WaybillLineItem\", \"cargo:lineItemNumber\": 3, \"cargo:dimensionsForRate\": {\"@type\": \"Dimensions\", \"cargo:width\": {\"@type\": \"Value\", \"cargo:unit\": {\"@type\": \"CodeListElement\", \"cargo:code\": \"CMT\"}, \"cargo:numericalValue\": 23}, \"cargo:height\": {\"@type\": \"Value\", \"cargo:unit\": {\"@type\": \"CodeListElement\", \"cargo:code\": \"CMT\"}, \"cargo:numericalValue\": 32}, \"cargo:length\": {\"@type\": \"Value\", \"cargo:unit\": {\"@type\": \"CodeListElement\", \"cargo:code\": \"CMT\"}, \"cargo:numericalValue\": 123}}, \"cargo:pieceCountForRate\": 111}, {\"@type\": \"WaybillLineItem\", \"cargo:lineItemNumber\": 4, \"cargo:dimensionsForRate\": {\"cargo:volume\": {\"@type\": \"Value\", \"cargo:unit\": {\"@type\": \"CodeListElement\", \"cargo:code\": \"MC\"}, \"cargo:numericalValue\": 10.05}}}, {\"@type\": \"WaybillLineItem\", \"cargo:hsCodeForRate\": {\"@type\": \"CodeListElement\", \"cargo:code\": \"HS873233\"}, \"cargo:lineItemNumber\": 5}], \"cargo:departureLocation\": {\"@type\": \"Location\", \"cargo:locationType\": \"AIRPORT\", \"cargo:locationCodes\": [{\"@type\": \"CodeListElement\", \"cargo:code\": \"GHH\"}]}, \"cargo:otherChargesIndicator\": {\"@type\": \"CodeListElement\", \"cargo:code\": \"P\"}, \"cargo:carrierDeclarationDate\": \"2022-05-13T00:00:00.000Z\", \"cargo:carrierDeclarationPlace\": \"YIP\", \"cargo:declaredValueForCustoms\": {\"@type\": \"CurrencyValue\", \"cargo:currencyUnit\": {\"@type\": \"CodeListElement\", \"cargo:code\": \"USD\"}, \"cargo:numericalValue\": 27627}, \"cargo:weightValuationIndicator\": {\"@type\": \"CodeListElement\", \"cargo:code\": \"P\"}, \"cargo:carrierDeclarationSignature\": \"FDS\", \"cargo:consignorDeclarationSignature\": \"FDS\"}";

    @Test
    public void testG(){
        JSONArray jsonArray = JSON.parseArray(test);
        JSONObject o = (JSONObject)jsonArray.get(0);
        JSONArray jsonArray2 = (JSONArray)o.get("pushContent");
        System.out.println(jsonArray2);
    }

    static String jsonTest = "{\"MawbCode\":\"618-72685281\",\"ConsigneePartyPostcodeCode\":\"3233\",\"FreightForwarderPartyIATACode\":\"11111111234\",\"FlightDate2\":\"2024-08-15\",\"SourceCurrencyCode\":\"USD\",\"ActualDateTime\":\"2022-05-13\",\"IssueAuthenticationLocation\":\"YIP\",\"ConsignorPartyCityName\":\"CHICAGO\",\"DeclaredValueForCustomsAmount\":\"27627\",\"ConsigneePartyTelephone\":\"861069448288\",\"ArrPort\":\"\",\"SpecialServiceRequest\":\"HANDLING INFO\",\"ConsignorPartyPostcodeCode\":\"60666\",\"TotalChargeableWeight\":\"323\",\"OtherChargs\":[{\"subTotal\":\"50.00\",\"chargeCode\":\"AWC\"},{\"subTotal\":\"323.00\",\"chargeCode\":\"MYC\"},{\"subTotal\":\"969.0\",\"chargeCode\":\"SCC\"}],\"RateClass\":\"Q\",\"ConsigneePartyName\":\"DHL GLOBAL FORWARDING CHINA\",\"HSCode\":\"HS873233\",\"TotalPrepaidChargeAmount\":\"2311.00\",\"ConsignorPartyCountrySubDivisionID\":\"BC\",\"WeightTotalChargeAmount\":\"969\",\"InsuranceValueAmount\":\"XXX\",\"ConsignorPartyName\":\"DHL GLOBAL FORWARDING CHICAGO\",\"GrossWeight\":\"27627\",\"SPHCode\":\"SHR,EAP\",\"Recipient\":\"RHKAIR08CPA\",\"ConsigneePartyStreetName\":\"7F EAST ZONE NO 566 SHUNPING RD\",\"NotifyTelephone\":\"7883233422\",\"PackageQuantity\":\"4\",\"ConsignorPartyCountryID\":\"US\",\"OtherCustomsInfos\":[{\"CountyCode\":\"\",\"InfoIdentifier\":\"\",\"CustInfoIdentifier\":\"SM\",\"SuppCustInfo\":\"XRY\"},{\"CountyCode\":\"US\",\"InfoIdentifier\":\"SHP\",\"CustInfoIdentifier\":\"T\",\"SuppCustInfo\":\"EIN43435325\"},{\"CountyCode\":\"CN\",\"InfoIdentifier\":\"CNE\",\"CustInfoIdentifier\":\"T\",\"SuppCustInfo\":\"99993323\"},{\"CountyCode\":\"CN\",\"InfoIdentifier\":\"CNE\",\"CustInfoIdentifier\":\"CP\",\"SuppCustInfo\":\"DHL GLOBAL FORWARDING CHINA\"}],\"Sender\":\"EZCAGT86CNGTL/XMN85\",\"ChargeableWeight\":\"323\",\"CarrierTotalDisbursementAmount\":\"1342.00\",\"FlightDate\":\"2024-08-13\",\"TotalGrossWeight\":\"27627\",\"FlightNo2\":\"K4998\",\"ConsignorPartyStreetName\":\"11601 W TOUHY AVENUE BLDG 895\",\"TransportPaymentMethodCode\":\"\",\"ArrivalLocationID2\":\"HGH\",\"DepPort\":\"TNA\",\"ArrivalLocationID1\":\"TNA\",\"DeclaredValueForCarriageAmount\":\"NVD\",\"ArrivalLocationID3\":\"PEK\",\"FreightForwarderPartyAddress\":\"ORD\",\"CarrierID3\":\"K4\",\"NotifyPostcodeCode\":\"100000\",\"ConsigneePartyCountryID\":\"CN\",\"Signatory\":\"FDS\",\"NotifyName\":\"NOFITY NAME\",\"IncludedAccountingNote\":\"FREIGHT PREPAID\\nJFKDLA\\nJFDSAF\",\"PieceQuantity\":\"111\",\"GrossWeightUnitCode\":\"K\",\"FlightNo\":\"K4996\",\"AppliedRate\":\"3\",\"TotalPieceQuantity\":\"111\",\"SequenceNumeric\":\"1\",\"ConsignorPartyTelephone\":\"18472337900\",\"SignatoryConsignorAuthentication\":\"FDS\",\"IsTransit\":\"0\",\"NotifyCountryID\":\"CN\",\"OtherServiceInformation\":\"OTHER SERVICE INFOMATION\",\"AppliedAmount\":\"969\",\"Dimensions\":[{\"Length\":\"123\",\"ItemQuantity\":\"111\",\"Height\":\"32\",\"Width\":\"23\"}],\"MsgType\":\"FWB\",\"PrepaidIndicator\":\"PP\",\"NatureIdentificationTransportCargo\":\"CONSOL\",\"ConsigneePartyCityName\":\"BEIJING\",\"FreightForwarderPartyAccountNo\":\"3323ED\",\"NotifyStreetName\":\"NOTIFY ADDRESS\",\"NotifyCityName\":\"BEIJING\",\"CarrierID1\":\"K4\",\"CarrierID2\":\"K4\",\"FreightForwarderPartyName\":\"AGENT INFO\",\"VolumnAmount\":\"10.05\"}";

    /**
     * 外运生产,外运推送国货航
     */
    @Test
    public void test() throws JsonProcessingException {
//        String jsonStr = FileUtil.readUtf8String("D:/Code_EF/工作文档/新加坡json.json");
        ObjectMapper mapper = new ObjectMapper();
        String responseType="OneRecord3";
        HashMap map = mapper.readValue(jsonTest, new TypeReference<HashMap>(){});
        map.put("MsgType", "FWB");
        String result = CaacParseTransfer.transfer(map, responseType);
//        System.out.println(result);
        Map record2 = CaacParseTransfer
                .parse(result, "OneRecord3", "2", "JSON");
        String dataText2 = record2.get("DataText").toString();
        ObjectMapper mapper2 = new ObjectMapper();
        NeOneShipmentData shipmentData = mapper2.readValue(dataText2, NeOneShipmentData.class);
        System.out.println("转了一圈回来以后的json为：" + shipmentData.toString());
    }

    static String FSU = "{\n" +
            "    \"@type\": \"cargo:LogisticsEvent\",\n" +
            "    \"cargo:creationDate\": \"2026-04-22T11:32:34.003708+08:00\",\n" +
            "    \"cargo:eventCode\": {\n" +
            "        \"@type\": \"cargo:CodeListElement\",\n" +
            "        \"cargo:Code\": \"BKD\",\n" +
            "        \"cargo:codeDescription\": \"FSU Status Codes\"\n" +
            "    },\n" +
            "    \"cargo:eventFor\": {\n" +
            "        \"@id\": \"https://sc-onerecordapi.cathaycargo.com/onerecordapi/logistics-objects/C6A7C69C-7FDB-40BB-8180-C9C439E8B3EB\",\n" +
            "        \"@type\": \"cargo:LogisticsObject\",\n" +
            "        \"cargo:waybill\": {\n" +
            "            \"@type\": \"cargo:Waybill\",\n" +
            "            \"cargo:arrivalLocation\": {\n" +
            "                \"@type\": \"cargo:Location\",\n" +
            "                \"cargo:locationCodes\": {\n" +
            "                    \"@type\": \"cargo:CodeListElement\",\n" +
            "                    \"cargo:Code\": \"ORD\"\n" +
            "                }\n" +
            "            },\n" +
            "            \"cargo:departureLocation\": {\n" +
            "                \"@type\": \"cargo:Location\",\n" +
            "                \"cargo:locationCodes\": {\n" +
            "                    \"@type\": \"cargo:CodeListElement\",\n" +
            "                    \"cargo:Code\": \"HKG\"\n" +
            "                }\n" +
            "            },\n" +
            "            \"cargo:waybillNumber\": \"160-08716945\",\n" +
            "            \"cargo:waybillPrefix\": \"160\"\n" +
            "        }\n" +
            "    },\n" +
            "    \"cargo:eventLocation\": {\n" +
            "        \"@type\": \"cargo:Location\"\n" +
            "    },\n" +
            "    \"cargo:eventName\": \"CONVERT FSU TO LOGISTICSEVENT SUCCESSFULLY - AWB 160-08716945\",\n" +
            "    \"cargo:eventTimeType\": \"SCHEDULED\",\n" +
            "    \"cargo:externalReferences\": {\n" +
            "        \"@type\": \"cargo:ExternalReference\"\n" +
            "    },\n" +
            "    \"cargo:recordingActor\": {\n" +
            "        \"@type\": \"cargo:Actor\"\n" +
            "    },\n" +
            "    \"cargo:recordingOrganization\": {\n" +
            "        \"@type\": \"cargo:Organization\",\n" +
            "        \"cargo:shortName\": \"RHKXDW08CPA\"\n" +
            "    },\n" +
            "    \"@context\": {\n" +
            "        \"cargo\": \"https://onerecord.iata.org/ns/cargo#\"\n" +
            "    },\n" +
            "    \"@id\": \"https://sc-onerecordapi.cathaycargo.com/onerecordapi/logistics-objects/C6A7C69C-7FDB-40BB-8180-C9C439E8B3EB/logistics-events/e8250a63-3dfb-11f1-9931-005056a3e0d3\"\n" +
            "}";

    @Test
    public void test2() throws JsonProcessingException {
        String timeStr = OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        String mawbCode = "160-08716945";
        String prefix = mawbCode.substring(0, 3);

        String loid = "https://sc-onerecordapi.cathaycargo.com/onerecordapi/logistics-objects/C6A7C69C-7FDB-40BB-8180-C9C439E8B3EB";

        String iri = "https://sc-onerecordapi.cathaycargo.com/onerecordapi/logistics-objects/C6A7C69C-7FDB-40BB-8180-C9C439E8B3EB/logistics-events/e8250a63-3dfb-11f1-9931-005056a3e0d3";
        // ================= 1. 将对象转为 JSON =================
        LogisticsEventFSU event = new LogisticsEventFSU();

        event.setCreationDate(timeStr);

        LogisticsEventFSU.CodeListElement eventCode = new LogisticsEventFSU.CodeListElement();
        eventCode.setCode("SAC");
        eventCode.setCodeDescription("FSU Status Codes");
        event.setEventCode(eventCode);

        List<LogisticsEventFSU.ExceptionHandlingCode> exceptionHandlingCodes = new ArrayList<>();
        LogisticsEventFSU.ExceptionHandlingCode code = new LogisticsEventFSU.ExceptionHandlingCode();
        code.setCode("SPC-CHK");
        code.setCodeDescription("危险品(DGR)核对有误");
        exceptionHandlingCodes.add(code);
        event.setExceptionHandlingCodes(exceptionHandlingCodes);

        LogisticsEventFSU.LogisticsObject logisticsObject = new LogisticsEventFSU.LogisticsObject();
        logisticsObject.setId(loid);
        logisticsObject.setType("cargo:LogisticsObject");
        LogisticsEventFSU.Waybill waybill = new LogisticsEventFSU.Waybill();
        LogisticsEventFSU.Location arrivalLocation = new LogisticsEventFSU.Location();
        LogisticsEventFSU.CodeListElement alCodeListElement = new LogisticsEventFSU.CodeListElement();
        alCodeListElement.setCode("ORD");
        arrivalLocation.setLocationCodes(alCodeListElement);
        LogisticsEventFSU.Location departureLocation = new LogisticsEventFSU.Location();
        LogisticsEventFSU.CodeListElement dlCodeListElement = new LogisticsEventFSU.CodeListElement();
        dlCodeListElement.setCode("HKG");
        departureLocation.setLocationCodes(dlCodeListElement);
        waybill.setArrivalLocation(arrivalLocation);
        waybill.setDepartureLocation(departureLocation);
        waybill.setWaybillNumber(mawbCode);
        waybill.setWaybillPrefix(prefix);
        logisticsObject.setWaybill(waybill);
        event.setEventFor(logisticsObject);

        event.setEventLocation(new LogisticsEventFSU.Location());

        event.setEventName("CONVERT SAC TO LOGISTICSEVENT SUCCESSFULLY - AWB " + mawbCode);
        event.setEventTimeType("SCHEDULED");

        event.setExternalReferences(new LogisticsEventFSU.ExternalReference());
        event.setRecordingActor(new LogisticsEventFSU.Actor());

        Map<String, String> context = new HashMap<>();
        context.put("cargo", "https://onerecord.iata.org/ns/cargo#");
        event.setContext(context);

        event.setId(iri);

        // 生成 JSON
        String generatedJson = LogisticsEventUtils.toJson(event, true);
        System.out.println("====== 生成的 JSON ======");
        System.out.println(generatedJson);
    }

    static String SAC = "{\n" +
            "  \"@type\" : \"cargo:LogisticsEvent\",\n" +
            "  \"@id\" : \"https://sc-onerecordapi.cathaycargo.com/onerecordapi/logistics-objects/C6A7C69C-7FDB-40BB-8180-C9C439E8B3EB/logistics-events/e8250a63-3dfb-11f1-9931-005056a3e0d3\",\n" +
            "  \"@context\" : {\n" +
            "    \"cargo\" : \"https://onerecord.iata.org/ns/cargo#\"\n" +
            "  },\n" +
            "  \"cargo:creationDate\" : \"2026-04-25T08:34:08.9076953+08:00\",\n" +
            "  \"cargo:eventCode\" : {\n" +
            "    \"@type\" : \"cargo:CodeListElement\",\n" +
            "    \"cargo:code\" : \"SAC\",\n" +
            "    \"cargo:codeDescription\" : \"FSU Status Codes\"\n" +
            "  },\n" +
            "  \"cargo:exceptionHandlingCodes\" : [ {\n" +
            "    \"@type\" : \"cargo:EHC\",\n" +
            "    \"cargo:Code\" : \"SPC-CHK\",\n" +
            "    \"cargo:codeDescription\" : \"危险品(DGR)核对有误\"\n" +
            "  } ],\n" +
            "  \"cargo:eventFor\" : {\n" +
            "    \"@id\" : \"https://sc-onerecordapi.cathaycargo.com/onerecordapi/logistics-objects/C6A7C69C-7FDB-40BB-8180-C9C439E8B3EB\",\n" +
            "    \"@type\" : \"cargo:LogisticsObject\",\n" +
            "    \"cargo:waybill\" : {\n" +
            "      \"@type\" : \"cargo:Waybill\",\n" +
            "      \"cargo:arrivalLocation\" : {\n" +
            "        \"@type\" : \"cargo:Location\",\n" +
            "        \"cargo:locationCodes\" : {\n" +
            "          \"@type\" : \"cargo:CodeListElement\",\n" +
            "          \"cargo:code\" : \"ORD\"\n" +
            "        }\n" +
            "      },\n" +
            "      \"cargo:departureLocation\" : {\n" +
            "        \"@type\" : \"cargo:Location\",\n" +
            "        \"cargo:locationCodes\" : {\n" +
            "          \"@type\" : \"cargo:CodeListElement\",\n" +
            "          \"cargo:code\" : \"HKG\"\n" +
            "        }\n" +
            "      },\n" +
            "      \"cargo:waybillNumber\" : \"160-08716945\",\n" +
            "      \"cargo:waybillPrefix\" : \"160\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"cargo:eventLocation\" : {\n" +
            "    \"@type\" : \"cargo:Location\"\n" +
            "  },\n" +
            "  \"cargo:eventName\" : \"CONVERT SAC TO LOGISTICSEVENT SUCCESSFULLY - AWB 160-08716945\",\n" +
            "  \"cargo:eventTimeType\" : \"SCHEDULED\",\n" +
            "  \"cargo:externalReferences\" : {\n" +
            "    \"@type\" : \"cargo:ExternalReference\"\n" +
            "  },\n" +
            "  \"cargo:recordingActor\" : {\n" +
            "    \"@type\" : \"cargo:Actor\"\n" +
            "  }\n" +
            "}";

    @Test
    public void testSACtoJson(){
        LogisticsEventFSU parsedEvent = LogisticsEventUtils.fromJson(SAC);
        String jsonString = JSON.toJSONString(parsedEvent.getExceptionHandlingCodes());
        String code = parsedEvent.getEventCode().getCode();
        String loId = parsedEvent.getEventFor().getId();
        System.out.println("loId: " + loId);
        System.out.println("Event Code: " + code);
        System.out.println("Exception Handling Codes: " + jsonString);
        System.out.println("\n====== 解析出的对象 ======");
        System.out.println("Creation Date: " + parsedEvent.getCreationDate());
        System.out.println("Event Name: " + parsedEvent.getEventName());
    }
}
