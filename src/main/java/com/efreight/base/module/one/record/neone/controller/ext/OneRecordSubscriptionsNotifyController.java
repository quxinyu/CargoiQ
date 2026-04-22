package com.efreight.base.module.one.record.neone.controller.ext;

import com.efreight.base.common.core.exception.EftException;
import com.efreight.base.common.core.model.Result;
import com.efreight.base.module.one.record.neone.annotations.Authenticated;
import com.efreight.base.module.one.record.neone.model.request.SubscriptionRequestBody;
import com.efreight.base.module.one.record.neone.model.vo.NeOneNotificationsVO;
import com.efreight.base.module.one.record.neone.model.vo.SubscriptionRequestVo;
import com.efreight.base.module.one.record.neone.service.NeOneNotifyService;
import com.efreight.base.module.one.record.neone.service.OneRecordSubscriptionsNotifyService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author fu yuan hui
 * @since 2024-09-19 17:54:37 星期四
 */
@Api(tags = "one record company")
@Authenticated
@RestController
@RequestMapping("/sn")
@RequiredArgsConstructor
public class OneRecordSubscriptionsNotifyController {

    private final NeOneNotifyService neOneNotifyService;
    private final OneRecordSubscriptionsNotifyService subscriptionsNotifyService;

    @PostMapping("/subscribe")
    public Result<?> subscribe(@RequestBody @Valid SubscriptionRequestBody request) {
        boolean subscribe = this.subscriptionsNotifyService.subscribe(request);
        return subscribe ? Result.ok("订阅成功") : Result.fail("订阅失败：点击失败详情,查看具体的订阅失败原因");
    }

    /**
     * 取消订阅
     */
    @PostMapping("/unsubscribe/{id}")
    public Result<?> unSubscribe(@PathVariable Long id) {
        return subscriptionsNotifyService.unSubscribe(id);
    }

    /**
     * 更新订阅
     */
    @PostMapping("/update/subscribe")
    public Result<?> updateSubscribe(@RequestBody SubscriptionRequestBody request) {
        Long id = request.getId();
        if (id == null || StringUtils.isBlank(id.toString())) {
            throw new EftException("id不能为空");
        }
        boolean subscribe = this.subscriptionsNotifyService.updateSubscribe(request);
        return subscribe ? Result.ok("更新订阅信息成功") : Result.fail("订阅更新失败：点击失败详情,查看具体的订阅更新失败原因");
    }

    @GetMapping("/page")
    public Result<?> page(SubscriptionRequestVo requestParam) {
        return Result.ok(this.subscriptionsNotifyService.pageList(requestParam));
    }

    @GetMapping("/notifyPage")
    public Result<?> notifyPage(NeOneNotificationsVO requestParam) {
        return Result.ok(this.neOneNotifyService.notifyPage(requestParam));
    }

    @GetMapping("/notifyDetails/{id}")
    public Result<?> notifyDetails(@PathVariable Long id) {
        return Result.ok(this.neOneNotifyService.notifyDetails(id));
    }

    @GetMapping("/failDetails/{id}")
    public Result<?> getFailDetails(@PathVariable Long id) {
        return Result.ok(this.subscriptionsNotifyService.getFailCause(id));
    }

    @GetMapping("/details/{id}")
    public Result<?> details(@PathVariable Long id) {
        return Result.ok(this.subscriptionsNotifyService.getDetails(id));
    }

    @GetMapping("/neOneDetails/{id}")
    public Result<?> neOneDetails(@PathVariable Long id) {
        return Result.ok(this.subscriptionsNotifyService.neOneDetails(id));
    }

    @PostMapping("/delete")
    public Result<?> removeSubscribe(@RequestBody SubscriptionRequestBody request) {
        return this.subscriptionsNotifyService.removeSubscribe(request.getId());
    }
}
