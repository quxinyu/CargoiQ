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

import java.util.List;
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

    @ApiOperation(value = "手动核验", notes = "手动核验")
    @PostMapping("/check")
    public Result<?> check(@RequestBody List<NeOneShipmentSendRequest> request) {
        return this.neOneShipmentDataService.check(request);
    }

    @ApiOperation(value = "自动核验", notes = "自动核验")
    @PostMapping("/auto-check")
    public Result<?> autoCheck(@RequestBody NeOneShipmentSendRequest request) {
        return this.neOneShipmentDataService.autoCheck(request);
    }

    @ApiOperation(value = "查询核验结果", notes = "查询核验结果")
    @GetMapping("/query-check")
    public Result<?> queryCheck(@RequestParam String id) {
        return this.neOneShipmentDataService.queryCheck(id);
    }

    @ApiOperation(value = "查询核验结果", notes = "查询核验结果")
    @GetMapping("/query-ehc-content")
    public Result<?> queryEhcContent(@RequestParam String id) {
        return this.neOneShipmentDataService.queryEhcContent(id);
    }


}
