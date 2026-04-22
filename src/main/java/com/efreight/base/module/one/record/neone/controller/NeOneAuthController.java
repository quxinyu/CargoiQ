package com.efreight.base.module.one.record.neone.controller;

import cn.hutool.json.JSONUtil;
import com.efreight.base.common.core.model.Result;
import com.efreight.base.module.one.record.neone.model.objects.AuthInnerRequest;
import com.efreight.base.module.one.record.neone.model.objects.AuthRequest;
import com.efreight.base.module.one.record.neone.model.objects.AuthResponse;
import com.efreight.base.module.one.record.neone.service.OneRecordAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 1R服务器认证相关接口controller
 *
 * @author fu yuan hui
 * @since 2024-08-28 15:22:47 星期三
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class NeOneAuthController {

    private final OneRecordAuthService oneRecordAuthService;

    // ********************************新增类接口********************************

    /**
     * 新增Token配置
     *
     * @param authInnerRequest 内部认证token参数
     * @return 通用消息返回对象
     */
    @PostMapping("/authapi/add")
    public Result<?> addTokenConfig(@RequestBody @Valid AuthInnerRequest authInnerRequest) {
        log.info("====新增Token配置，参数：{}====", JSONUtil.toJsonStr(authInnerRequest));
        this.oneRecordAuthService.addTokenConfig(authInnerRequest);
        return Result.ok();
    }

    // ********************************删除类接口********************************

    /**
     * 删除Token配置
     *
     * @param id Token配置id
     * @return 通用消息返回对象
     */
    @DeleteMapping("/authapi/delete/{id}")
    public Result<?> deleteTokenConfig(@PathVariable("id") Integer id) {
        log.info("====删除Token配置，id：{}====", id);
        this.oneRecordAuthService.deleteTokenConfig(id);
        return Result.ok();
    }

    // ********************************修改类接口********************************

    /**
     * 更新Token配置
     *
     * @param authInnerRequest 内部认证token参数
     * @return 通用消息返回对象
     */
    @PostMapping("/authapi/update")
    public Result<?> updateTokenConfig(@RequestBody @Valid AuthInnerRequest authInnerRequest) {
        log.info("====更新Token配置，参数：{}====", JSONUtil.toJsonStr(authInnerRequest));
        this.oneRecordAuthService.updateTokenConfig(authInnerRequest);
        return Result.ok();
    }

    // ********************************查询类接口********************************

    /**
     * 分页获取Token配置
     *
     * @param authInnerRequest 内部认证token参数
     * @return 通用消息返回对象
     */
    @GetMapping("/authapi/page")
    public Result<?> page(AuthInnerRequest authInnerRequest) {
        log.info("====分页获取Token配置，参数：{}====", JSONUtil.toJsonStr(authInnerRequest));
        return Result.ok(this.oneRecordAuthService.pageList(authInnerRequest));
    }

    /**
     * 获取单个信息
     *
     * @param id
     * @return 通用消息返回对象
     */
    @GetMapping("/authapi/{id}")
    public Result<?> getOne(@PathVariable("id") Integer id) {
        log.info("====删除Token配置，id：{}====", id);
        return Result.ok(this.oneRecordAuthService.getOne(id));
    }

    @GetMapping("/authapi/oppositeToken/{id}")
    public Result<?> oppositeToken(@PathVariable("id") Integer id) {
        log.info("====删除Token配置，id：{}====", id);
        return Result.ok(this.oneRecordAuthService.oppositeToken(id));
    }

    /**
     * 获取全部自研keylock 公司名称
     *
     * @param
     * @return
     */
    @GetMapping("/authapi/all")
    public Result<?> all(AuthInnerRequest authInnerRequest) {
        return Result.ok(this.oneRecordAuthService.allOrgName(authInnerRequest));
    }

    /**
     * GET方式获取token
     *
     * @param clientId     clientId
     * @param clientSecret clientSecret
     * @param grantType    grantType
     * @param username     用户名
     * @param password     密码
     * @return AuthResponse对象
     */
    //@RepeatSubmit
    @GetMapping("/authapi/token")
    public AuthResponse applyToken(@RequestParam("client_id") String clientId,
                                   @RequestParam("client_secret") String clientSecret,
                                   @RequestParam("grant_type") String grantType,
                                   @RequestParam(value = "username", required = false) String username,
                                   @RequestParam(value = "password", required = false) String password) {
        log.info("====获取token，client_id：{}，client_secret：{}，grant_type：{}，username：{}，password：{}====", clientId, clientSecret, grantType, username, password);
        return this.oneRecordAuthService.applyToken(new AuthRequest(username, password, grantType, clientId, clientSecret));
    }

    /**
     * POST表单方式获取token
     * Content-Type: application/x-www-form-urlencoded
     *
     * @param authRequest 获取token请求参数
     * @return AuthResponse对象
     */
    @PostMapping("/authapi/token")
    public AuthResponse applyToken(@Valid AuthRequest authRequest) {
        log.info("====获取token，参数：{}====", JSONUtil.toJsonStr(authRequest));
        return this.oneRecordAuthService.applyToken(authRequest);
    }

    /**
     * POST表单方式获取token
     * Content-Type: application/json
     * 参考：<a href="https://iata-cargo.github.io/ONE-Record/stable/API-Security/security/authn-application-layer/#implementation-example">官方认证</a>
     * <p>
     * 官方文档里面有一个IDP的服务，用来生成一个tokenId, 然后将其放到header中，在配合client_id, client_secret 参数来获取token
     * 但是我们这里就不打算用tokenId了，直接用client_id, client_secret来获取token
     * <p>
     */
    @PostMapping("/authapis/token")
    public AuthResponse token(@RequestBody @Valid AuthRequest authRequest) {
        log.info("====获取token，参数：{}====", JSONUtil.toJsonStr(authRequest));
        return this.oneRecordAuthService.applyToken(authRequest);
    }
}
