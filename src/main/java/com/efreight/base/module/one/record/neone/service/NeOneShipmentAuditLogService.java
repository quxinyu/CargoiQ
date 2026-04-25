package com.efreight.base.module.one.record.neone.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.efreight.base.module.one.record.neone.model.entity.NeOneShipmentAuditLog;
import com.efreight.base.module.one.record.neone.model.request.NeOneShipmentAuditLogRequest;

public interface NeOneShipmentAuditLogService extends IService<NeOneShipmentAuditLog> {

    void saveAuditLog(NeOneShipmentAuditLog auditLog);

    IPage<?> pageList(NeOneShipmentAuditLogRequest request);
}
