package com.efreight.base.module.one.record.neone.controller.ext;

import com.efreight.base.common.core.model.Result;
import com.efreight.base.module.one.record.neone.annotations.Authenticated;
import com.efreight.base.module.one.record.neone.helper.CompanyServerParamValidator;
import com.efreight.base.module.one.record.neone.helper.NeOneServerRequestHelper;
import com.efreight.base.module.one.record.neone.model.entity.NeOneServerCompanyHolder;
import com.efreight.base.module.one.record.neone.model.request.CompanyHolderRequest;
import com.efreight.base.module.one.record.neone.service.NeOneCompanyService;
import com.jayway.jsonpath.JsonPath;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author qiuguan
 * @date 2024/09/16 12:41:27  星期一
 * <p>
 * 以下都是拓展接口，官方是没有提供的
 */
@Slf4j
@Api(tags = "one record company")
@Authenticated
@RestController
@RequestMapping("/server")
@RequiredArgsConstructor
public class OneRecordCompanyController {

    private final NeOneCompanyService neOneCompanyService;

    private final NeOneServerRequestHelper requestHelper;

    /**
     * 拓展接口：都和目标1R ping通之后，就可以将返回的信息保存起来，留着就行订阅以及查看
     *
     * @return
     */
    @ApiOperation(value = "保存1R服务", notes = "保存1R服务")
    @PostMapping(produces = {"application/ld+json", "application/json"})
    public Result<?> save(@RequestBody String pingBody) {
        CompanyServerParamValidator.validate(pingBody);
        return Result.ok(this.neOneCompanyService.create(pingBody));
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存1R服务", notes = "保存1R服务")
    public Result<?> save(@RequestBody CompanyHolderRequest request) {
        return Result.ok(this.neOneCompanyService.createServer(request));
    }

    @GetMapping("/pings/{id}")
    public Result<?> ping(@PathVariable Long id) {
        NeOneServerCompanyHolder holder = this.neOneCompanyService.getById(id);

        try {
            this.requestHelper.ping(holder.getHost());
            this.requestHelper.getCompanyServer(holder.getCompanyIri());
        } catch (Exception e) {
            log.error(">>>>>>>>>>>>>>>>>>>>>>目标NeOne Server 访问失败,host: {}, companyIri: {}", holder.getHost(), holder.getCompanyIri(), e);
            return Result.fail("目标 NeOne Server无法访问");
        }

        return Result.ok("目标 NeOne Server 服务运行良好");
    }


    @ApiOperation(value = "分页获取1R服务", notes = "分页获取1R服务")
    @GetMapping("/page")
    public Result<?> page(CompanyHolderRequest request) {
        return Result.ok(this.neOneCompanyService.pageList(request));
    }

    @ApiOperation(value = "获取1R服务详情", notes = "获取1R服务详情")
    @GetMapping("/{id}")
    public Result<?> get(@PathVariable Long id) {
        return Result.ok(JsonPath.parse(this.neOneCompanyService.getById(id).getCompanyBody()).json());
    }

    @ApiOperation(value = "1R拉取企业信息", notes = "1R拉取企业信息")
    @GetMapping("/pull/{id}")
    public Result<?> pull(@PathVariable Long id) {
//        this.neOneCompanyService.pull(id);
        return Result.ok(null);
    }

    /**
     * @param request
     * @return
     */
    @ApiOperation(value = "更新1R服务", notes = "更新1R服务")
    @PostMapping("/update")
    public Result<?> update(@RequestBody CompanyHolderRequest request) {
        this.neOneCompanyService.updateCompanyInfo(request);
        return Result.ok();
    }

    @ApiOperation(value = "删除1R服务", notes = "删除1R服务")
    @PostMapping("/delete")
    public Result<?> delete(@RequestBody CompanyHolderRequest request) {
        this.neOneCompanyService.removeCompanyInfo(request.getId());
        return Result.ok();
    }

    /**
     * 订阅列表选项会用到
     *
     * @return
     */
    @ApiOperation(value = "获取所有server", notes = "获取所有server")
    @GetMapping("/all")
    public Result<?> all() {
        return Result.ok(this.neOneCompanyService.allServer());
    }
}
