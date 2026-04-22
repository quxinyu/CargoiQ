package com.efreight.base.module.one.record.neone.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.efreight.base.module.one.record.neone.model.entity.NeOneAuthToken;
import com.efreight.base.module.one.record.neone.model.objects.AuthInnerRequest;
import com.efreight.base.module.one.record.neone.model.objects.AuthRequest;
import com.efreight.base.module.one.record.neone.model.objects.AuthResponse;

import java.util.List;

/**
 * 1R服务器认证相关接口service接口
 *
 * @author fu yuan hui
 * @since 2024-08-29 10:42:38 星期四
 */
public interface OneRecordAuthService {

    // ********************************新增类接口********************************

    /**
     * 新增Token配置
     *
     * @param authInnerRequest 内部认证token参数
     */
    void addTokenConfig(AuthInnerRequest authInnerRequest);

    // ********************************删除类接口********************************

    /**
     * 删除Token配置
     *
     * @param id Token配置id
     */
    void deleteTokenConfig(Integer id);

    // ********************************修改类接口********************************

    /**
     * 更新Token配置
     *
     * @param authInnerRequest 内部认证token参数
     */
    void updateTokenConfig(AuthInnerRequest authInnerRequest);

    // ********************************查询类接口********************************

    /**
     * 获取token
     *
     * @param authRequest 获取token请求参数
     * @return AuthResponse对象
     */
    AuthResponse applyToken(AuthRequest authRequest);

    /**
     * 获取token
     *
     * @param id 获取token请求参数
     * @return AuthResponse对象
     */
    NeOneAuthToken getOne(Integer id);

    /**
     * 分页获取Token配置
     *
     * @param authInnerRequest 内部认证token参数
     * @return token配置实体分页对象
     */
    IPage<?> pageList(AuthInnerRequest authInnerRequest);

    List<?> allOrgName(AuthInnerRequest authInnerRequest);

    String oppositeToken(Integer id);

}
