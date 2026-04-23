package com.efreight.base.module.one.record.neone.service.impl;

import cn.gov.caac.issp.utility.CaacParseTransfer;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.efreight.base.common.core.exception.EftException;
import com.efreight.base.common.core.model.Result;
import com.efreight.base.module.one.record.neone.enums.FromType;
import com.efreight.base.module.one.record.neone.helper.IriGenerator;
import com.efreight.base.module.one.record.neone.mapper.NeOneShipmentDataMapper;
import com.efreight.base.module.one.record.neone.model.entity.NeOneShipmentData;
import com.efreight.base.module.one.record.neone.model.onerecord.LogisticsEventFSU;
import com.efreight.base.module.one.record.neone.model.request.NeOneShipmentDataRequest;
import com.efreight.base.module.one.record.neone.model.request.NeOneShipmentSendRequest;
import com.efreight.base.module.one.record.neone.service.NeOneLogisticsEventsService;
import com.efreight.base.module.one.record.neone.service.NeOneLogisticsObjectsService;
import com.efreight.base.module.one.record.neone.service.NeOneShipmentDataService;
import com.efreight.base.module.one.record.neone.utils.UUIDTools;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RequiredArgsConstructor
@Service
public class NeOneShipmentDataServiceImpl extends ServiceImpl<NeOneShipmentDataMapper, NeOneShipmentData>implements NeOneShipmentDataService {

    private final NeOneLogisticsObjectsService logisticsObjectsService;

    private final NeOneLogisticsEventsService logisticsObjectsEventService;

    private final IriGenerator iriGenerator;

    @Override
    public IPage<?> pageList(NeOneShipmentDataRequest req) {
        LambdaQueryWrapper<NeOneShipmentData> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StringUtils.isNotBlank(req.getMawbCode()), NeOneShipmentData::getMawbCode, req.getMawbCode());
        wrapper.like(StringUtils.isNotBlank(req.getLoId()), NeOneShipmentData::getLoId, req.getLoId());
        return this.page(new Page<>(req.getCurrent(), req.getSize()), wrapper);
    }

    @Override
    public Result<?> send(String id) {
        NeOneShipmentData byId = this.getById(id);
        String s = JSON.toJSONString(byId);
        ObjectMapper mapper = new ObjectMapper();
        String responseType="OneRecord3";
        HashMap map;
        try {
            map = mapper.readValue(s, new TypeReference<HashMap>(){});
        } catch (JsonProcessingException e) {
            log.error("=====================================> OneRecord数据转换出现问题");
            throw new RuntimeException(e);
        }
        map.put("MsgType", "FWB");
        String OneR = CaacParseTransfer.transfer(map, responseType);

        String uuid = UUIDTools.generateSimpleUUID();
        String iri = iriGenerator.generateLogisticsObjectLoId(uuid);
        logisticsObjectsService.createResolveBody(iri, OneR, FromType.CREATE_SELF);

        byId.setLoId(iri);
        updateById(byId);

        return Result.ok(iri);
    }

    @Override
    public Result<?> sendCheck(NeOneShipmentSendRequest request) {
        String id = request.getId();
        NeOneShipmentData byId = getById(id);

        String checkStatus = byId.getCheckStatus();
        String aiCheckStatus = byId.getAiCheckStatus();
        if("1".equals(checkStatus) && "1".equals(aiCheckStatus)){
            sendOneRecord(new ArrayList<>(), byId, "RCS");
        } else {
            return Result.fail("请先完成数据审核");
        }
        return Result.ok();
    }

    @Override
    public void getObjectFromOneRecord(String oneRecordBody, String loId) {
        Map record2 = CaacParseTransfer
                .parse(oneRecordBody, "OneRecord3", "2", "JSON");
        String jsonObject = record2.get("DataText").toString();
        ObjectMapper mapper = new ObjectMapper();
        NeOneShipmentData shipmentData;
        try {
            shipmentData = mapper.readValue(jsonObject, NeOneShipmentData.class);
        } catch (JsonProcessingException e) {
            log.error("=====================================> 转换NeOneShipmentData出现问题");
            throw new RuntimeException(e);
        }
        shipmentData.setLoId(loId);
        save(shipmentData);
    }

    @Override
    public Result<?> check(List<NeOneShipmentSendRequest> request) {
        NeOneShipmentSendRequest neOneShipmentSendRequest = request.get(0);
        String id = neOneShipmentSendRequest.getId();
        NeOneShipmentData byId = getById(id);
        //如果成功则更新状态
        if("1".equals(neOneShipmentSendRequest.getCheckResult())){
            byId.setCheckStatus("1");
            updateById(byId);
            return Result.ok();
        }
        //失败则更新状态并写入原因
        String checkResult = byId.getCheckResult();
        JSONArray jsonArray = (checkResult != null && !checkResult.isEmpty())
                ? JSON.parseArray(checkResult)
                : new JSONArray();
        request.forEach(item -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("checkCode", item.getCheckCode());
            jsonObject.put("checkDescription", item.getCheckDescription());
            jsonArray.add(jsonObject);
        });
        String checkResult2 = JSON.toJSONString(jsonArray);
        byId.setCheckResult(checkResult2);
        byId.setCheckStatus("0");
        updateById(byId);

        sendOneRecord(request, byId, "SAC");

        return Result.ok();
    }

    private void sendOneRecord(List<NeOneShipmentSendRequest> request, NeOneShipmentData byId, String type) {
        String uuid = UUIDTools.generateSimpleUUID();
        String loId = byId.getLoId();
        String id = loId.substring(loId.lastIndexOf("/") + 1);
        String iri = this.iriGenerator.generateLogisticsEventLoId(id, uuid);
        String logisticsEvent = generateLogisticsEvent(byId, request, iri, type);
        logisticsObjectsEventService.create(id, uuid, iri, logisticsEvent,loId);
    }

    @Override
    public Result<?> autoCheck(NeOneShipmentSendRequest request) {
        String id = request.getId();
        NeOneShipmentData byId = getById(id);
        //如果成功则更新状态
        if("1".equals(request.getCheckResult())){
            byId.setAiCheckStatus("1");
            updateById(byId);
            return Result.ok();
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("checkCode", request.getCheckCode());
        jsonObject.put("checkDescription", request.getCheckDescription());
        String checkResult = byId.getCheckResult();
        JSONArray jsonArray = (checkResult != null && !checkResult.isEmpty())
                ? JSON.parseArray(checkResult)
                : new JSONArray();
        jsonArray.add(jsonObject);
        String s = JSON.toJSONString(jsonArray);
        byId.setCheckResult(s);
        byId.setAiCheckStatus("0");
        updateById(byId);
        sendOneRecord(Collections.singletonList(request), byId, "SAC");
        return Result.ok();
    }

    @Override
    public Result<?> queryCheck(String id) {
        NeOneShipmentData byId = getById(id);
        return Result.ok(byId.getCheckResult());
    }

    private String generateLogisticsEvent(NeOneShipmentData shipmentData,
                                          List<NeOneShipmentSendRequest> request, String iri, String type){
        String timeStr = OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        String mawbCode = shipmentData.getMawbCode();
        String prefix = (mawbCode != null && mawbCode.length() >= 3)
                ? mawbCode.substring(0, 3)
                : "";

        LogisticsEventFSU event = new LogisticsEventFSU();

        event.setCreationDate(timeStr);

        LogisticsEventFSU.CodeListElement eventCode = new LogisticsEventFSU.CodeListElement();
        eventCode.setCode(type);
        eventCode.setCodeDescription("FSU Status Codes");
        event.setEventCode(eventCode);

        if("SAC".equals( type)){
            List<LogisticsEventFSU.ExceptionHandlingCode> exceptionHandlingCodes = new ArrayList<>();
            request.forEach(item -> {
                LogisticsEventFSU.ExceptionHandlingCode code = new LogisticsEventFSU.ExceptionHandlingCode();
                code.setCode(item.getCheckCode());
                code.setCodeDescription(item.getCheckDescription());
                exceptionHandlingCodes.add(code);
            });
            event.setExceptionHandlingCodes(exceptionHandlingCodes);
        }

        LogisticsEventFSU.LogisticsObject logisticsObject = new LogisticsEventFSU.LogisticsObject();
        logisticsObject.setId(shipmentData.getLoId());
        logisticsObject.setType("cargo:LogisticsObject");
        LogisticsEventFSU.Waybill waybill = new LogisticsEventFSU.Waybill();
        LogisticsEventFSU.Location arrivalLocation = new LogisticsEventFSU.Location();
        LogisticsEventFSU.CodeListElement alCodeListElement = new LogisticsEventFSU.CodeListElement();
        alCodeListElement.setCode(shipmentData.getArrPort());
        arrivalLocation.setLocationCodes(alCodeListElement);
        LogisticsEventFSU.Location departureLocation = new LogisticsEventFSU.Location();
        LogisticsEventFSU.CodeListElement dlCodeListElement = new LogisticsEventFSU.CodeListElement();
        dlCodeListElement.setCode(shipmentData.getDepPort());
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
        return JSON.toJSONString(event);
    }

}
