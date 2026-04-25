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
import com.efreight.base.common.core.utils.WebUtils;
import com.efreight.base.common.core.constant.CommonConstants;
import com.efreight.base.module.one.record.neone.enums.FromType;
import com.efreight.base.module.one.record.neone.helper.IriGenerator;
import com.efreight.base.module.one.record.neone.mapper.NeOneShipmentDataMapper;
import com.efreight.base.module.one.record.neone.model.entity.NeOneShipmentAuditLog;
import com.efreight.base.module.one.record.neone.model.entity.NeOneShipmentData;
import com.efreight.base.module.one.record.neone.model.onerecord.LogisticsEventFSU;
import com.efreight.base.module.one.record.neone.model.request.NeOneShipmentDataRequest;
import com.efreight.base.module.one.record.neone.model.request.NeOneShipmentSendRequest;
import com.efreight.base.module.one.record.neone.service.NeOneShipmentAuditLogService;
import com.efreight.base.module.one.record.neone.service.NeOneLogisticsEventsService;
import com.efreight.base.module.one.record.neone.service.NeOneLogisticsObjectsService;
import com.efreight.base.module.one.record.neone.service.NeOneShipmentDataService;
import com.efreight.base.module.one.record.neone.utils.LogisticsEventUtils;
import com.efreight.base.module.one.record.neone.utils.UUIDTools;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RequiredArgsConstructor
@Service
public class NeOneShipmentDataServiceImpl extends ServiceImpl<NeOneShipmentDataMapper, NeOneShipmentData>implements NeOneShipmentDataService {

    private static final String OPERATOR_NAME = "gha";
    private static final String OPERATION_SEND_CHECK = "SEND_CHECK";
    private static final String OPERATION_CHECK = "CHECK";
    private static final String OPERATION_AUTO_CHECK = "AUTO_CHECK";
    private static final String RESULT_SUCCESS = "SUCCESS";
    private static final String RESULT_FAIL = "FAIL";

    private final NeOneLogisticsObjectsService logisticsObjectsService;

    private final NeOneLogisticsEventsService logisticsObjectsEventService;

    private final IriGenerator iriGenerator;

    private final NeOneShipmentAuditLogService shipmentAuditLogService;

    @Override
    public IPage<?> pageList(NeOneShipmentDataRequest req) {
        LambdaQueryWrapper<NeOneShipmentData> wrapper = Wrappers.lambdaQuery();
        wrapper.orderByDesc(NeOneShipmentData::getCreateTime);
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
        ShipmentAuditContext auditContext = buildAuditContext(Collections.singletonList(request));
        try {
            String id = request.getId();
            NeOneShipmentData byId = getById(id);

            String checkStatus = byId.getCheckStatus();
            String aiCheckStatus = byId.getAiCheckStatus();
            Result<?> result;
            if("1".equals(checkStatus) && "1".equals(aiCheckStatus)){
                sendOneRecord(new ArrayList<>(), byId, "RCS");
                result = Result.ok();
            } else {
                result = Result.fail("请先完成数据审核");
            }
            saveAuditLog(auditContext, OPERATION_SEND_CHECK, request, result.isOk() ? RESULT_SUCCESS : RESULT_FAIL, result.getMessage());
            return result;
        } catch (RuntimeException ex) {
            saveAuditLog(auditContext, OPERATION_SEND_CHECK, request, RESULT_FAIL, ex.getMessage());
            throw ex;
        }
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
    public void getObjectFromOneRecordEvent(String oneRecordBody) {
        LogisticsEventFSU parsedEvent = LogisticsEventUtils.fromJson(oneRecordBody);
        String ehcContent = JSON.toJSONString(parsedEvent.getExceptionHandlingCodes());
        String code = parsedEvent.getEventCode().getCode();
        String loId = parsedEvent.getEventFor().getId();
        LambdaQueryWrapper<NeOneShipmentData> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(NeOneShipmentData::getLoId, loId);
        List<NeOneShipmentData> neOneShipmentData = getBaseMapper().selectList(wrapper);
        if(CollectionUtils.isNotEmpty(neOneShipmentData)){
            neOneShipmentData.forEach(data -> {
                data.setFsuStatus(code);
                data.setEhcContent(ehcContent);
                updateById(data);
            });
        }
    }

    @Override
    public Result<?> check(List<NeOneShipmentSendRequest> request) {
        ShipmentAuditContext auditContext = buildAuditContext(request);
        try {
            NeOneShipmentSendRequest neOneShipmentSendRequest = request.get(0);
            String id = neOneShipmentSendRequest.getId();
            NeOneShipmentData byId = getById(id);
            Result<?> result;
            //如果成功则更新状态
            if("1".equals(neOneShipmentSendRequest.getCheckResult())){
                byId.setCheckStatus("1");
                updateById(byId);
                result = Result.ok();
            } else {
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
                result = Result.ok();
            }

            saveAuditLog(auditContext, OPERATION_CHECK, request, result.isOk() ? RESULT_SUCCESS : RESULT_FAIL, buildCheckSummary(request));
            return result;
        } catch (RuntimeException ex) {
            saveAuditLog(auditContext, OPERATION_CHECK, request, RESULT_FAIL, ex.getMessage());
            throw ex;
        }
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
        ShipmentAuditContext auditContext = buildAuditContext(Collections.singletonList(request));
        try {
            String id = request.getId();
            NeOneShipmentData byId = getById(id);
            Result<?> result;
            //如果成功则更新状态
            if("1".equals(request.getCheckResult())){
                byId.setAiCheckStatus("1");
                updateById(byId);
                result = Result.ok();
            } else {
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
                result = Result.ok();
            }
            saveAuditLog(auditContext, OPERATION_AUTO_CHECK, request, result.isOk() ? RESULT_SUCCESS : RESULT_FAIL, request.getCheckDescription());
            return result;
        } catch (RuntimeException ex) {
            saveAuditLog(auditContext, OPERATION_AUTO_CHECK, request, RESULT_FAIL, ex.getMessage());
            throw ex;
        }
    }

    @Override
    public Result<?> queryCheck(String id) {
        NeOneShipmentData byId = getById(id);
        return Result.ok(byId.getCheckResult());
    }

    @Override
    public Result<?> queryEhcContent(String id) {
        NeOneShipmentData byId = getById(id);
        String ehcContent = byId.getEhcContent();
        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("ehcContent", ehcContent);
        return Result.ok(objectObjectHashMap);
    }

    @Override
    public Result<?> queryFsuEvent(String loId) {
        LambdaQueryWrapper<NeOneShipmentData> wrapper = Wrappers.lambdaQuery();
        wrapper.like(NeOneShipmentData::getLoId, loId);
        wrapper.orderByDesc(NeOneShipmentData::getCreateTime);
        List<NeOneShipmentData> neOneShipmentData = getBaseMapper().selectList(wrapper);
        if(CollectionUtils.isNotEmpty(neOneShipmentData)){
            NeOneShipmentData data = neOneShipmentData.get(0);
            HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
            objectObjectHashMap.put("fsuStatus", data.getFsuStatus());
            objectObjectHashMap.put("ehcContent", data.getEhcContent());
            return Result.ok(objectObjectHashMap);
        }
        return Result.ok();
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

    private ShipmentAuditContext buildAuditContext(List<NeOneShipmentSendRequest> requests) {
        if (CollectionUtils.isEmpty(requests)) {
            return new ShipmentAuditContext("", "");
        }
        List<String> ids = new ArrayList<>();
        List<String> operateNos = new ArrayList<>();
        for (NeOneShipmentSendRequest request : requests) {
            if (StringUtils.isBlank(request.getId())) {
                continue;
            }
            ids.add(request.getId());
            NeOneShipmentData shipmentData = this.getById(request.getId());
            if (shipmentData != null && StringUtils.isNotBlank(shipmentData.getMawbCode())) {
                operateNos.add(shipmentData.getMawbCode());
            }
        }
        return new ShipmentAuditContext(joinDistinct(ids), joinDistinct(operateNos));
    }

    private void saveAuditLog(ShipmentAuditContext auditContext,
                              String operationType,
                              Object requestBody,
                              String resultStatus,
                              String resultMessage) {
        try {
            NeOneShipmentAuditLog auditLog = new NeOneShipmentAuditLog();
            auditLog.setShipmentDataIds(auditContext.shipmentDataIds);
            auditLog.setOperateNo(auditContext.operateNo);
            auditLog.setOperationType(operationType);
            auditLog.setOperatorName(OPERATOR_NAME);
            auditLog.setOperatorIp(resolveOperatorIp());
            auditLog.setResultStatus(resultStatus);
            auditLog.setResultMessage(StringUtils.defaultString(resultMessage));
            auditLog.setRequestBody(JSON.toJSONString(requestBody));
            auditLog.setTraceId(MDC.get(CommonConstants.TRACE_ID));
            shipmentAuditLogService.saveAuditLog(auditLog);
        } catch (Exception ex) {
            log.error("save shipment audit log failed, operationType=" + operationType, ex);
        }
    }

    private String resolveOperatorIp() {
        try {
            return WebUtils.getIP();
        } catch (Exception ex) {
            return "";
        }
    }

    private String joinDistinct(List<String> values) {
        if (CollectionUtils.isEmpty(values)) {
            return "";
        }
        LinkedHashSet<String> distinct = new LinkedHashSet<>();
        values.stream().filter(StringUtils::isNotBlank).forEach(distinct::add);
        return String.join(",", distinct);
    }

    private String buildCheckSummary(List<NeOneShipmentSendRequest> requests) {
        if (CollectionUtils.isEmpty(requests)) {
            return "";
        }
        long successCount = requests.stream().filter(item -> "1".equals(item.getCheckResult())).count();
        long failCount = requests.size() - successCount;
        return "total=" + requests.size() + ",success=" + successCount + ",fail=" + failCount;
    }

    private static class ShipmentAuditContext {
        private final String shipmentDataIds;
        private final String operateNo;

        private ShipmentAuditContext(String shipmentDataIds, String operateNo) {
            this.shipmentDataIds = shipmentDataIds;
            this.operateNo = operateNo;
        }
    }

}
