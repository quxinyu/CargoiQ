package com.efreight.base.module.one.record.neone.controller;

import com.efreight.base.common.core.model.Result;
import com.efreight.base.module.one.record.neone.model.request.NeOneShipmentAuditLogRequest;
import com.efreight.base.module.one.record.neone.service.NeOneShipmentAuditLogService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/freight-forwarder/audit-log")
public class NeOneShipmentAuditLogController {

    private final NeOneShipmentAuditLogService shipmentAuditLogService;

    @ApiOperation(value = "审计记录分页", notes = "根据单号、操作人分页查询审计记录")
    @GetMapping("/page")
    public Result<?> page(NeOneShipmentAuditLogRequest request) {
        return Result.ok(this.shipmentAuditLogService.pageList(request));
    }
}
