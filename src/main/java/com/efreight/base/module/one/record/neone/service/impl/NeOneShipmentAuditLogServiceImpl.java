package com.efreight.base.module.one.record.neone.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.efreight.base.module.one.record.neone.mapper.NeOneShipmentAuditLogMapper;
import com.efreight.base.module.one.record.neone.model.entity.NeOneShipmentAuditLog;
import com.efreight.base.module.one.record.neone.model.request.NeOneShipmentAuditLogRequest;
import com.efreight.base.module.one.record.neone.service.NeOneShipmentAuditLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NeOneShipmentAuditLogServiceImpl
        extends ServiceImpl<NeOneShipmentAuditLogMapper, NeOneShipmentAuditLog>
        implements NeOneShipmentAuditLogService {

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveAuditLog(NeOneShipmentAuditLog auditLog) {
        this.save(auditLog);
    }

    @Override
    public IPage<?> pageList(NeOneShipmentAuditLogRequest request) {
        LambdaQueryWrapper<NeOneShipmentAuditLog> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StringUtils.isNotBlank(request.getOperateNo()), NeOneShipmentAuditLog::getOperateNo, request.getOperateNo());
        wrapper.like(StringUtils.isNotBlank(request.getOperatorName()), NeOneShipmentAuditLog::getOperatorName, request.getOperatorName());
        wrapper.orderByDesc(NeOneShipmentAuditLog::getCreateTime);
        return this.page(new Page<>(request.getCurrent(), request.getSize()), wrapper);
    }
}
