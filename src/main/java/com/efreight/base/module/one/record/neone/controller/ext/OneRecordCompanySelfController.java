package com.efreight.base.module.one.record.neone.controller.ext;

import com.efreight.base.common.core.model.Result;
import com.efreight.base.module.one.record.neone.annotations.Authenticated;
import com.efreight.base.module.one.record.neone.model.entity.NeOneServerCompanyHolder;
import com.efreight.base.module.one.record.neone.model.request.CompanyHolderRequest;
import com.efreight.base.module.one.record.neone.service.NeOneCompanyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author qiuguan
 * @date 2024/09/16 12:41:27  星期一
 *
 * 以下都是拓展接口，官方是没有提供的
 */
@Slf4j
@Api(tags = "one record self company")
@Authenticated
@RestController
@RequestMapping("/server")
@RequiredArgsConstructor
public class OneRecordCompanySelfController {

    private final NeOneCompanyService neOneCompanyService;

    @ApiOperation(value = "创建或更新自己的1R服务", notes = "创建或更新自己的1R服务")
    @PostMapping("/save/self")
    public Result<?> createMySelfServer(@RequestBody CompanyHolderRequest request) {
        return Result.ok(this.neOneCompanyService.createMySelfServer(request));
    }


    /**
     * 查看自己的one record server数据
     * @return
     */
    @ApiOperation(value = "查看自己的1R服务", notes = "查看自己的1R服务")
    @PutMapping("/get/self")
    public Result<?> getLocalServer() {
        NeOneServerCompanyHolder localServer = this.neOneCompanyService.getLocalServer();
        return Result.ok(localServer);
    }
}
