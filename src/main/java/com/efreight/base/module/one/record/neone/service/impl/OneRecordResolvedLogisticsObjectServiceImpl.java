package com.efreight.base.module.one.record.neone.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.efreight.base.common.core.exception.OneRecordServerException;
import com.efreight.base.common.core.enmus.LoModuleType;
import com.efreight.base.common.core.enmus.MessageDataType;
import com.efreight.base.common.core.enmus.OneRecordParseVersionType;
import com.efreight.base.common.core.utils.CaacParseTransferTools;
import com.efreight.base.module.one.record.neone.helper.IriGenerator;
import com.efreight.base.module.one.record.neone.mapper.OneRecordResolvedLogisticsObjectMapper;
import com.efreight.base.module.one.record.neone.model.entity.NeOneResolvedLogisticsObject;
import com.efreight.base.common.core.model.NeOneResolvedData;
import com.efreight.base.module.one.record.neone.model.objects.LoQueryRequestParam;
import com.efreight.base.module.one.record.neone.model.objects.OneRecordRequestBody;
import com.efreight.base.module.one.record.neone.model.vo.OneRecordLogisticsInfoVo;
import com.efreight.base.module.one.record.neone.service.OneRecordResolvedLogisticsObjectService;
import com.efreight.base.module.one.record.neone.utils.ReverseResolveOneRecordBodyTools;
import com.efreight.base.module.one.record.neone.utils.UUIDTools;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author fu yuan hui
 * @since 2024-08-14 15:26:19 星期三
 */
@RequiredArgsConstructor
@Service
public class OneRecordResolvedLogisticsObjectServiceImpl extends ServiceImpl<OneRecordResolvedLogisticsObjectMapper, NeOneResolvedLogisticsObject> implements OneRecordResolvedLogisticsObjectService {

    private final IriGenerator iriGenerator;

    @Transactional
    @Override
    public String uploadReport(String report, OneRecordParseVersionType parseVersion) {
        return uploadReport(report, null, parseVersion);
    }

    @Override
    public String uploadReport(String report, MessageDataType messageDataType, OneRecordParseVersionType parseVersion) {
        NeOneResolvedData baseRecord = CaacParseTransferTools.resolve(report, messageDataType, parseVersion);

        if (!baseRecord.isCanResolveOneRecord()) {
            throw new OneRecordServerException("当前报文类型是" + baseRecord.getContextType() +" 不支持转换成OneRecord,目前支持的有, 2.0支持FWB, FHL, FSU; 3.0支持FWB,FHL,FSU,FFR");
        }

        LoModuleType type = baseRecord.getContextType().toUpperCase().contains("FSU") ? LoModuleType.LOGISTICS_EVENT : LoModuleType.LOGISTICS_OBJECT;
        NeOneResolvedLogisticsObject info = NeOneResolvedLogisticsObject.create(baseRecord);
        info.setLoModuleType(type.name());
        info.setParseVersion(parseVersion.getName());

        //bindLoObject(type, baseRecord.getOneRecordJsonBody(), parseVersion, info);

        save(info);

        return info.getLoIri();
    }

    @Transactional
    @Override
    public String uploadOneRecordBody(OneRecordRequestBody recordRequestBody) {
        if (StringUtils.isBlank(recordRequestBody.getOneRecordBody())) {
            throw new IllegalArgumentException("one record json body must be not null");
        }
        NeOneResolvedData loRecord = ReverseResolveOneRecordBodyTools.parse(recordRequestBody.getOneRecordBody());
        NeOneResolvedLogisticsObject info = NeOneResolvedLogisticsObject.create(loRecord);
        info.setAwbNumber(StringUtils.isBlank(info.getAwbNumber()) ? recordRequestBody.getAwbNumber() : info.getAwbNumber());

        //bindLoObject(loRecord.getMeta().getEventType(), recordRequestBody.getOneRecordBody(), loRecord.getMeta().getVersion(), info);

        info.setLoModuleType(loRecord.getMeta().getEventType().name());

        save(info);

        return info.getLoIri();

    }

    @Override
    public List<String> listLos(String masterCode) {
        LambdaQueryWrapper<NeOneResolvedLogisticsObject> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(NeOneResolvedLogisticsObject::getAwbNumber, masterCode);

        List<NeOneResolvedLogisticsObject> list = list(wrapper);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }

        return list.stream().map(NeOneResolvedLogisticsObject::getLoIri).collect(Collectors.toList());
    }

    @Override
    public List<String> listLos(String masterCode, LoModuleType type) {
        LambdaQueryWrapper<NeOneResolvedLogisticsObject> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(NeOneResolvedLogisticsObject::getAwbNumber, masterCode);
        wrapper.eq(NeOneResolvedLogisticsObject::getLoModuleType, type.name());

        List<NeOneResolvedLogisticsObject> list = list(wrapper);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }

        return list.stream().map(NeOneResolvedLogisticsObject::getLoIri).collect(Collectors.toList());
    }

    @Override
    public IPage<?> pageList(Page<NeOneResolvedLogisticsObject> page, LoQueryRequestParam request) {
        LambdaQueryWrapper<NeOneResolvedLogisticsObject> queryWrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(request.getAwbNumber())) {
            queryWrapper.like(NeOneResolvedLogisticsObject::getAwbNumber, request.getAwbNumber());
        }

        if (StringUtils.isNotBlank(request.getSubNumber())) {
            queryWrapper.like(NeOneResolvedLogisticsObject::getSubNumber, request.getSubNumber());
        }

        if (StringUtils.isNotBlank(request.getFlightNumber())) {
            queryWrapper.like(NeOneResolvedLogisticsObject::getFlightNumber, request.getFlightNumber());
        }

        if (StringUtils.isNotBlank(request.getLogisticsObjectId())) {
            queryWrapper.like(NeOneResolvedLogisticsObject::getLoIri, request.getLogisticsObjectId());
        }

        if (!Objects.isNull(request.getFlightDateStart())) {
            queryWrapper.ge(NeOneResolvedLogisticsObject::getFlightDate, request.getFlightDateStart());
        }

        if (!Objects.isNull(request.getFlightDateEnd())) {
            queryWrapper.le(NeOneResolvedLogisticsObject::getFlightDate, request.getFlightDateEnd());
        }

        queryWrapper.orderByDesc(NeOneResolvedLogisticsObject::getCreateTime);

        queryWrapper.select(NeOneResolvedLogisticsObject.class, t -> !"input_source_report".equals(t.getColumn()));
        return page(page, queryWrapper).convert(OneRecordLogisticsInfoVo::convert);
    }

    private void bindLoObject(LoModuleType type, String baseRecord, OneRecordParseVersionType parseVersion, NeOneResolvedLogisticsObject info) {
        if (LoModuleType.LOGISTICS_EVENT == type) {
            String master = CaacParseTransferTools.resolveMasterCodeFromOneRecordBody(baseRecord, parseVersion, type);
            List<String> loObjects = listLos(master, LoModuleType.LOGISTICS_OBJECT);
            if (CollectionUtils.isEmpty(loObjects)) {
                throw new OneRecordServerException("无法创建FSU的物流事件，请先创建主单的OneRecord物流对象后再进行创建");
            }

            String loId = loObjects.get(0);
            String uuid = loId.substring(loId.lastIndexOf("/"));
            String eventId = UUIDTools.generateSafeUUID();
            String iri = this.iriGenerator.generateLogisticsEventLoId(uuid, eventId);
            //this.baseService.create(iri, baseRecord, LoModuleType.LOGISTICS_EVENT, parseVersion);
            info.setLoIri(iri);
        } else {
            String uuid = UUIDTools.generateSafeUUID();
            String neId = this.iriGenerator.generateLogisticsObjectLoId(uuid);
            //String iri = this.baseService.create(neId, baseRecord, type, parseVersion);
            //info.setLoIri(iri);
        }

        info.setCreateTime(LocalDateTime.now());
    }
}
