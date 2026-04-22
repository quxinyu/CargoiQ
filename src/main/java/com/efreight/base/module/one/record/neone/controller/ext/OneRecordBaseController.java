package com.efreight.base.module.one.record.neone.controller.ext;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.efreight.base.common.core.model.Result;
import com.efreight.base.module.one.record.neone.annotations.Authenticated;
import com.efreight.base.common.core.enmus.MessageDataType;
import com.efreight.base.common.core.enmus.OneRecordParseVersionType;
import com.efreight.base.module.one.record.neone.model.entity.NeOneResolvedLogisticsObject;
import com.efreight.base.module.one.record.neone.model.objects.LoQueryRequestParam;
import com.efreight.base.module.one.record.neone.model.objects.OneRecordRequestBody;
import com.efreight.base.module.one.record.neone.model.objects.ReportBodyContext;
import com.efreight.base.module.one.record.neone.service.OneRecordResolvedLogisticsObjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author fu yuan hui
 * @since 2024-08-13 16:55:44 星期二
 */
@Authenticated
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/lo/base")
@RestController
public class OneRecordBaseController {

    private final OneRecordResolvedLogisticsObjectService logisticsInfoService;


    @PostMapping(value = "/uploadReport", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Result<?> uploadReport(@RequestBody ReportBodyContext ctx) {
        MessageDataType messageDataType = MessageDataType.select(ctx.getType());
        OneRecordParseVersionType parseVersion = OneRecordParseVersionType.selectByName(ctx.getParseVersion());
        return Result.ok(logisticsInfoService.uploadReport(ctx.getReport(), messageDataType, parseVersion));
    }

    @PostMapping(value = "/uploadOneRecordBody")
    public Result<?> pushBody(@RequestBody OneRecordRequestBody body) {
        return Result.ok(logisticsInfoService.uploadOneRecordBody(body));
    }


    @GetMapping("/listLos/{masterCode}")
    public Result<?> listLos(@PathVariable String masterCode) {
        return Result.ok(logisticsInfoService.listLos(masterCode));
    }

    @GetMapping("/page")
    public Result<IPage<?>> listLos(Page<NeOneResolvedLogisticsObject> page, LoQueryRequestParam request) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>查询参数： {}", JSON.toJSONString(request));
        return Result.ok(logisticsInfoService.pageList(page, request));
    }

    @GetMapping("/get/{id}")
    public Result<?> get(@PathVariable Long id) {
        return Result.ok(logisticsInfoService.getById(id));
    }
}
