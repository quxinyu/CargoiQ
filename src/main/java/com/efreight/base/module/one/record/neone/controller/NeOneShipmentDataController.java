package com.efreight.base.module.one.record.neone.controller;

import com.efreight.base.common.core.model.Result;
import com.efreight.base.module.one.record.neone.model.entity.NeOneShipmentData;
import com.efreight.base.module.one.record.neone.model.request.NeOneShipmentDataRequest;
import com.efreight.base.module.one.record.neone.model.request.NeOneShipmentSendRequest;
import com.efreight.base.module.one.record.neone.service.NeOneShipmentDataService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/freight-forwarder")
public class NeOneShipmentDataController {

    private final NeOneShipmentDataService neOneShipmentDataService;

    @PostMapping("/save")
    @ApiOperation(value = "保存", notes = "保存")
    public Result<?> save(@RequestBody NeOneShipmentData entity) {
        return Result.ok(this.neOneShipmentDataService.save(entity));
    }

    @ApiOperation(value = "更新", notes = "更新")
    @PostMapping("/update")
    public Result<?> update(@RequestBody NeOneShipmentData entity) {
        this.neOneShipmentDataService.updateById(entity);
        return Result.ok();
    }

    @ApiOperation(value = "分页", notes = "分页")
    @GetMapping("/page")
    public Result<?> page(NeOneShipmentDataRequest request) {
        return Result.ok(this.neOneShipmentDataService.pageList(request));
    }

    @ApiOperation(value = "删除", notes = "删除")
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable String id) {
        this.neOneShipmentDataService.removeById(id);
        return Result.ok();
    }

    @ApiOperation(value = "发送Lo", notes = "发送Lo")
    @PostMapping("/send")
    public Result<?> send(@RequestBody Map<String, Object> request) {
        String id = String.valueOf(request.get("id"));
        return Result.ok(this.neOneShipmentDataService.send(id));
    }

    @ApiOperation(value = "发送Lo", notes = "发送Lo")
    @PostMapping("/send-check")
    public Result<?> sendCheck(@RequestBody NeOneShipmentSendRequest request) {
        return Result.ok(this.neOneShipmentDataService.sendCheck(request));
    }


}
