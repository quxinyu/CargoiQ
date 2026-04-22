package com.efreight.base.module.one.record.neone.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.efreight.base.common.core.enmus.LoModuleType;
import com.efreight.base.common.core.enmus.OneRecordParseVersionType;
import com.efreight.base.common.core.exception.OneRecordServerException;
import com.efreight.base.common.core.model.NeOneResolvedData;
import com.efreight.base.common.core.utils.CaacParseTransferTools;
import com.efreight.base.module.one.record.neone.enums.ActionRequestStatus;
import com.efreight.base.module.one.record.neone.enums.FromType;
import com.efreight.base.module.one.record.neone.enums.NotifyEventType;
import com.efreight.base.module.one.record.neone.ex.LogisticsObjectException;
import com.efreight.base.module.one.record.neone.ex.LogisticsObjectPatchException;
import com.efreight.base.module.one.record.neone.ex.NeoneRequestBodyException;
import com.efreight.base.module.one.record.neone.helper.IriGenerator;
import com.efreight.base.module.one.record.neone.helper.LocalCompanyCacheHelper;
import com.efreight.base.module.one.record.neone.mapper.NeOneLogisticsObjectsMapper;
import com.efreight.base.module.one.record.neone.model.entity.NeOneActionRequests;
import com.efreight.base.module.one.record.neone.model.entity.NeOneLogisticsObjects;
import com.efreight.base.module.one.record.neone.notify.NotifyHandler;
import com.efreight.base.module.one.record.neone.service.ActionRequestService;
import com.efreight.base.module.one.record.neone.service.NeOneLogisticsObjectsService;
import com.efreight.base.module.one.record.neone.utils.Iso8601UtcTime2LocalDateTimeUtils;
import com.efreight.base.module.one.record.neone.utils.UrlFormatUtils;
import com.google.common.eventbus.EventBus;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author fu yuan hui
 * @since 2024-08-13 16:54:02 星期二
 */
@RequiredArgsConstructor
@Service
public class NeOneLogisticsObjectsServiceImpl extends ServiceImpl<NeOneLogisticsObjectsMapper, NeOneLogisticsObjects> implements NeOneLogisticsObjectsService {

    private final NotifyHandler<NeOneLogisticsObjects> notifyHandler;

    private final EventBus asyncNeOneEventBus;

    private final ActionRequestService actionRequestService;

    private final IriGenerator iriGenerator;

    private final LocalCompanyCacheHelper localCompanyRedisHelper;

    private static final Configuration CONFIG = Configuration.defaultConfiguration().addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);

    private String auditTrailJson;

    @Override
    public String create(String iri, String oneRecordBody, LoModuleType type, FromType fromType) {
        return create(iri, oneRecordBody, type, fromType, OneRecordParseVersionType.V3);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public String create(String iri, String oneRecordBody, LoModuleType type, FromType fromType, OneRecordParseVersionType versionType) {
        if (StringUtils.isBlank(oneRecordBody)) {
            throw new NeoneRequestBodyException("one Record body must be not null");
        }
        NeOneLogisticsObjects base = new NeOneLogisticsObjects();
        base.setIri(iri);
        base.setLoId(iri.substring(iri.lastIndexOf("/") + 1));
        base.setBodyText(oneRecordBody);
        if (LoModuleType.LOGISTICS_COMPANY == type) {
            base.setContextType("Company");
            base.setLoModuleType(LoModuleType.LOGISTICS_COMPANY.name());
        } else {
            NeOneResolvedData.Meta meta = CaacParseTransferTools.parseLoEventTypeAndVersion(oneRecordBody);
            base.setLoModuleType(meta.getEventType().name());
            base.setContextType(meta.getContextType());
            base.setLoModuleType(meta.getEventType().name());
        }
        base.setCreateTime(LocalDateTime.now());
        base.setLoFromType(fromType.name());
        base.setVersion(versionType == null ? CaacParseTransferTools.parseOneRecordVersion(oneRecordBody).getName() : versionType.getName());
        save(base);
        base.setNotifyEventType(NotifyEventType.LOGISTICS_OBJECT_CREATED);
        this.asyncNeOneEventBus.post(base);
        // 逻辑重复  通过发布时间实现
//        notifyHandler.notify(NotifyEventType.LOGISTICS_OBJECT_CREATED, () -> base);
        return iri;
    }

    @Override
    public void createResolveBody(String iri, String oneRecordBody, FromType fromType) {
        create(iri, oneRecordBody, null, fromType);
    }

    @Override
    public NeOneLogisticsObjects get(String iri) {
        LambdaQueryWrapper<NeOneLogisticsObjects> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(NeOneLogisticsObjects::getIri, iri);
        return getOne(wrapper);
    }

    @Override
    public NeOneLogisticsObjects getWithLoId(String loId) {
        LambdaQueryWrapper<NeOneLogisticsObjects> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(NeOneLogisticsObjects::getLoId, loId);
        wrapper.orderByDesc(NeOneLogisticsObjects::getCreateTime);
        wrapper.last("LIMIT 1");
        return getOne(wrapper);
    }

    /**
     * 这样查询，随着物流数据越来越多，可能会很慢，但是这个东西一般很少查询，就先这样写吧
     *
     * @return
     */
    @Override
    public NeOneLogisticsObjects getServerInfo() {
        LambdaQueryWrapper<NeOneLogisticsObjects> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(NeOneLogisticsObjects::getLoModuleType, LoModuleType.SERVER_INFORMATION.name());
        return getOne(wrapper);
    }

    @Override
    public void removeLogisticsObject(String loId) {
        LambdaQueryWrapper<NeOneLogisticsObjects> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(NeOneLogisticsObjects::getLoId, loId);

        remove(wrapper);
    }

    @Override
    public String updateLogisticsObject(String loId, String oneRecordUpdateBody) {
        NeOneLogisticsObjects logisticsObjects = getWithLoId(loId);
        if (null == logisticsObjects) {
            throw new LogisticsObjectException("Logistics Object not found", 404);
        }

        String loUrl = JsonPath.using(CONFIG).parse(oneRecordUpdateBody).read("$.api:hasLogisticsObject.@id");
        if (StringUtils.isBlank(loUrl) || !loUrl.contains(loId)) {
            throw new LogisticsObjectPatchException(loUrl, 400);
        }

        NeOneActionRequests actionRequests = new NeOneActionRequests();
        actionRequests.setActionRequestId(loId);
        actionRequests.setActionRequestStatus(ActionRequestStatus.REQUEST_PENDING.name());
        actionRequests.setActionResponseBody(oneRecordUpdateBody);
        String actionRequestIri = iriGenerator.generateActionRequestIri(localCompanyRedisHelper.get().getHost(), loId);
        actionRequests.setActionRequestIri(actionRequestIri);
        actionRequests.setDeleted(0);
        actionRequests.setCreateTime(LocalDateTime.now());

        this.actionRequestService.saveActionRequest(actionRequests);

        return actionRequestIri;
    }

    @Override
    public String auditTrail(String loId, String updateFrom, String updateTo, String status) {

        NeOneLogisticsObjects logisticsObjects = getWithLoId(loId);
        if (null == logisticsObjects) {
            throw new LogisticsObjectException("Logistics Object not found", 404);
        }

        LambdaQueryWrapper<NeOneActionRequests> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(NeOneActionRequests::getActionRequestId, loId);
        wrapper.ge(StringUtils.isNotBlank(updateFrom), NeOneActionRequests::getCreateTime, Iso8601UtcTime2LocalDateTimeUtils.parse(updateFrom));
        wrapper.le(StringUtils.isNotBlank(updateTo), NeOneActionRequests::getCreateTime, Iso8601UtcTime2LocalDateTimeUtils.parse(updateTo));
        wrapper.eq(StringUtils.isNotBlank(status), NeOneActionRequests::getActionRequestStatus, status);

        List<NeOneActionRequests> list = this.actionRequestService.list(wrapper);

        if (CollectionUtils.isEmpty(list)) {
            throw new LogisticsObjectException("Logistics Object not found", 404);
        }

        List<Object> actionBody = list.stream().map(t -> JsonPath.parse(t.getActionResponseBody()).json()).collect(Collectors.toList());

        //构建JSON response
        return JsonPath.using(CONFIG).parse(this.auditTrailJson)
                .set("$.@id", UrlFormatUtils.resolveUrl(localCompanyRedisHelper.get().getHost() + "/logistics-objects/" + loId + "/audit-trail"))
                .set("$.api:hasLatestRevision.@value", actionBody.size())
                .set("$.api:hasActionRequest", actionBody)
                .jsonString();
    }

    @PostConstruct
    public void init() {
        try {
            this.auditTrailJson = IOUtils.resourceToString("onerecord/audit-trail.json", StandardCharsets.UTF_8, NeOneLogisticsObjectsServiceImpl.class.getClassLoader());
        } catch (IOException e) {
            throw new OneRecordServerException("JSON 文件初始化失败", e);
        }
    }

}
