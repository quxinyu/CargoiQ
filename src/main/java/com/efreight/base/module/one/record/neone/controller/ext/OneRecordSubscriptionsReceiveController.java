package com.efreight.base.module.one.record.neone.controller.ext;

import com.efreight.base.common.core.model.Result;
import com.efreight.base.module.one.record.neone.annotations.Authenticated;
import com.efreight.base.module.one.record.neone.model.entity.NeOneSubscriptions;
import com.efreight.base.module.one.record.neone.model.vo.SubscriptionVo;
import com.efreight.base.module.one.record.neone.service.SubscriptionsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 接收订阅
 *
 * @author quxinyu
 * @since 2025-11-10
 */
@Api(tags = "")
@Authenticated
@RestController
@RequestMapping("/se")
@RequiredArgsConstructor
public class OneRecordSubscriptionsReceiveController {

    private final SubscriptionsService subscriptionsService;


    @GetMapping("/page")
    public Result<?> page(SubscriptionVo requestParam) {
        return Result.ok(this.subscriptionsService.pageList(requestParam));
    }


    @GetMapping("/receiveDetails/{id}")
    public Result<?> receiveDetails(@PathVariable Long id) {
        return Result.ok(this.subscriptionsService.receiveDetails(id));
    }

    @PostMapping("/delete")
    public Result<?> deleteSubscription(@RequestBody SubscriptionVo request) {
        return Result.ok(subscriptionsService.deleteSubscription(request.getId()));
    }

    @PostMapping("/internalSubscription")
    @ApiOperation(value = "添加内部订阅", notes = "添加内部订阅")
    public Result<?> internalSubscription(@RequestBody NeOneSubscriptions neOneSubscriptions) {
        this.subscriptionsService.internalSubscription(neOneSubscriptions);
        return Result.ok();
    }

}
