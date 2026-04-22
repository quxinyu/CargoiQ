package com.efreight.base.module.one.record.neone.service.impl;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.efreight.base.common.core.exception.EftException;
import com.efreight.base.module.one.record.neone.config.NeOneConfigProperties;
import com.efreight.base.module.one.record.neone.constants.BaseConstants;
import com.efreight.base.module.one.record.neone.constants.RedisConstants;
import com.efreight.base.module.one.record.neone.ex.NeOneAuthException;
import com.efreight.base.module.one.record.neone.model.entity.NeOneAuthToken;
import com.efreight.base.module.one.record.neone.model.entity.NeOneServerCompanyHolder;
import com.efreight.base.module.one.record.neone.model.objects.AuthInnerRequest;
import com.efreight.base.module.one.record.neone.model.objects.AuthRequest;
import com.efreight.base.module.one.record.neone.model.objects.AuthResponse;
import com.efreight.base.module.one.record.neone.service.NeOneAuthTokenService;
import com.efreight.base.module.one.record.neone.service.NeOneCompanyService;
import com.efreight.base.module.one.record.neone.service.OneRecordAuthService;
import com.efreight.base.module.one.record.neone.utils.AuthConfigGenUtils;
import com.efreight.base.module.one.record.neone.utils.KeyLockTokenUtils;
import com.efreight.base.module.one.record.neone.utils.UUIDTools;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

/**
 * 1R服务器认证相关接口service实现类
 *
 * @author fu yuan hui
 * @since 2024-08-29 10:43:04 星期四
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OneRecordAuthServiceImpl implements OneRecordAuthService {

    private final NeOneConfigProperties neOneConfigProperties;
    private final NeOneAuthTokenService neOneAuthTokenService;
    private final NeOneCompanyService neOneCompanyService;

    private final StringRedisTemplate stringRedisTemplate;

    // ********************************新增类接口********************************

    @Override
    public void addTokenConfig(AuthInnerRequest authInnerRequest) {
        LambdaQueryWrapper<NeOneAuthToken> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(NeOneAuthToken::getOrgName, authInnerRequest.getOrgName());
        if (neOneAuthTokenService.count(wrapper) > 0) {
            throw new EftException("存在相同公司名称请修改!");
        }
        NeOneAuthToken neOneAuthToken = new NeOneAuthToken();
        neOneAuthToken.setClientId(AuthConfigGenUtils.genClientId());
        neOneAuthToken.setClientSecret(AuthConfigGenUtils.genClientSecret());
        neOneAuthToken.setGrantType(authInnerRequest.getGrantType());
        neOneAuthToken.setOrgName(authInnerRequest.getOrgName());
        neOneAuthToken.setParentOrgName(authInnerRequest.getParentOrgName());
        neOneAuthToken.setUsername(authInnerRequest.getUsername());
        neOneAuthToken.setPassword(authInnerRequest.getPassword());
        neOneAuthToken.setOpposite(authInnerRequest.getOpposite());
        neOneAuthToken.setExp(authInnerRequest.getExp());
        neOneAuthToken.setCreateTime(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
        try {
            this.neOneAuthTokenService.save(neOneAuthToken);
        } catch (Throwable e) {
            log.error(e.toString(), e);
            throw new EftException("系统异常，请联系管理员");
        }
    }


    // ********************************删除类接口********************************

    @Override
    public void deleteTokenConfig(Integer id) {
        try {
            this.neOneAuthTokenService.removeById(id);
        } catch (Throwable e) {
            log.error(e.toString(), e);
            throw new EftException("系统异常，请联系管理员");
        }
    }

    // ********************************修改类接口********************************

    @Override
    public void updateTokenConfig(AuthInnerRequest authInnerRequest) {
        Integer id = authInnerRequest.getId();
        if (id == null) {
            throw new EftException("id不能为空");
        }
        NeOneAuthToken token = this.neOneAuthTokenService.getById(id);
        token.setOrgName(authInnerRequest.getOrgName());
        token.setParentOrgName(authInnerRequest.getParentOrgName());
        token.setUsername(authInnerRequest.getUsername());
        token.setPassword(authInnerRequest.getPassword());
        token.setOpposite(authInnerRequest.getOpposite());
        token.setExp(authInnerRequest.getExp());
        token.setUpdateTime(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
        try {
            this.neOneAuthTokenService.updateById(token);
        } catch (Throwable e) {
            log.error(e.toString(), e);
            throw new EftException("系统异常，请联系管理员");
        }
    }


    // ********************************查询类接口********************************

    @Override
    public AuthResponse applyToken(AuthRequest authRequest) {
        this.verifySecretConfig(authRequest);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime refreshTokenExpire = now.plusHours(this.neOneConfigProperties.getRefreshTokenExpireHours());
        String refreshToken = JWT.create()
                .withClaim("client_id", authRequest.getClientId())
                .withClaim("client_secret", authRequest.getClientSecret())
                .withClaim("grant_type", StringUtils.isBlank(authRequest.getGrantType()) ? authRequest.getGrantType() : BaseConstants.DEFAULT_GRANT_TYPE)
                .withClaim("username", StringUtils.isNotBlank(authRequest.getUsername()) ? authRequest.getUsername() : "")
                .withClaim("password", StringUtils.isNotBlank(authRequest.getPassword()) ? authRequest.getPassword() : "")
                .withClaim("uuid", UUIDTools.generateSimpleUUID())
                .withPayload(JSON.toJSONString(authRequest))
                .withExpiresAt(refreshTokenExpire.toInstant(ZoneOffset.ofHours(8)))
                .sign(Algorithm.HMAC384(BaseConstants.JWT_SIGN_KEY));
        LocalDateTime expireDate = now.plusHours(this.neOneConfigProperties.getTokenExpireHours());
        String accessToken = JWT.create()
                .withHeader(BaseConstants.JWT_TOKEN_HEADER)
                .withClaim("client_id", authRequest.getClientId())
                .withClaim("client_secret", authRequest.getClientSecret())
                .withClaim("grant_type", StringUtils.isBlank(authRequest.getGrantType()) ? authRequest.getGrantType() : BaseConstants.DEFAULT_GRANT_TYPE)
                .withClaim("username", StringUtils.isNotBlank(authRequest.getUsername()) ? authRequest.getUsername() : "")
                .withClaim("password", StringUtils.isNotBlank(authRequest.getPassword()) ? authRequest.getPassword() : "")
                .withClaim("uuid", UUIDTools.generateSimpleUUID())
                .withPayload(JSON.toJSONString(authRequest))
                .withExpiresAt(expireDate.toInstant(ZoneOffset.ofHours(8)))
                .sign(Algorithm.HMAC384(BaseConstants.JWT_SIGN_KEY));
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType(BaseConstants.DEFAULT_TOKEN_TYPE)
                .expiresIn((this.neOneConfigProperties.getTokenExpireHours() * 3600L))
                .build();
    }

    @Override
    public NeOneAuthToken getOne(Integer id) {
        return neOneAuthTokenService.getById(id);
    }

    @Override
    public IPage<?> pageList(AuthInnerRequest req) {
        LambdaQueryWrapper<NeOneAuthToken> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StringUtils.isNotBlank(req.getOrgName()), NeOneAuthToken::getOrgName, req.getOrgName());
        wrapper.like(StringUtils.isNotBlank(req.getParentOrgName()), NeOneAuthToken::getParentOrgName, req.getParentOrgName());
        wrapper.like(StringUtils.isNotBlank(req.getClientId()), NeOneAuthToken::getClientId, req.getClientId());
        wrapper.like(StringUtils.isNotBlank(req.getClientSecret()), NeOneAuthToken::getClientSecret, req.getClientSecret());
        wrapper.like(StringUtils.isNotBlank(req.getUsername()), NeOneAuthToken::getUsername, req.getUsername());
        wrapper.like(StringUtils.isNotBlank(req.getGrantType()), NeOneAuthToken::getGrantType, req.getGrantType());
        wrapper.like(StringUtils.isNotBlank(req.getPassword()), NeOneAuthToken::getPassword, req.getPassword());

        return this.neOneAuthTokenService.page(new Page<>(req.getCurrent(), req.getSize()), wrapper);
    }

    @Override
    public List<?> allOrgName(AuthInnerRequest req) {
        LambdaQueryWrapper<NeOneAuthToken> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StringUtils.isNotBlank(req.getOrgName()), NeOneAuthToken::getOrgName, req.getOrgName());
        wrapper.like(StringUtils.isNotBlank(req.getParentOrgName()), NeOneAuthToken::getParentOrgName, req.getParentOrgName());
        wrapper.like(StringUtils.isNotBlank(req.getClientId()), NeOneAuthToken::getClientId, req.getClientId());
        wrapper.like(StringUtils.isNotBlank(req.getClientSecret()), NeOneAuthToken::getClientSecret, req.getClientSecret());
        wrapper.like(StringUtils.isNotBlank(req.getUsername()), NeOneAuthToken::getUsername, req.getUsername());
        wrapper.like(StringUtils.isNotBlank(req.getGrantType()), NeOneAuthToken::getGrantType, req.getGrantType());
        wrapper.like(StringUtils.isNotBlank(req.getPassword()), NeOneAuthToken::getPassword, req.getPassword());
        return neOneAuthTokenService.list(wrapper);
    }

    @Override
    public String oppositeToken(Integer id) {
        NeOneAuthToken neOneAuthToken = neOneAuthTokenService.getById(id);
        String orgName = neOneAuthToken.getOrgName();
        LambdaQueryWrapper<NeOneServerCompanyHolder> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(NeOneServerCompanyHolder::getCompanyName, orgName);
        NeOneServerCompanyHolder neOneServerCompanyHolder = neOneCompanyService.getOne(wrapper);
        stringRedisTemplate.delete(RedisConstants.COMPANY_TOKEN_KEY + neOneServerCompanyHolder.getCompanyName());
        String token = KeyLockTokenUtils.getCompanyToekn(neOneServerCompanyHolder);
        return token;
    }


    // ********************************公共函数********************************
    // ********************************私有函数********************************

    /**
     * 校验clientId和clientSecret
     * 规则：优先验证本地配置文件中的白名单，如果不存在，则从数据库验证
     *
     * @param authRequest 获取token请求参数
     */
    private void verifySecretConfig(AuthRequest authRequest) {
        String clientId = authRequest.getClientId();
        String clientSecret = authRequest.getClientSecret();
        // 优先看本地配置文件
        if (verifySecretConfigFromProperties(authRequest)) {
            return;
        }
        log.info(">>>>>>>>>>>>>>>>>>>本地配置未命中认证信息，将从数据库读取配置");
        NeOneAuthToken token = this.neOneAuthTokenService.getToken(clientId, clientSecret, authRequest.getGrantType());
        if (token == null) {
            throw new NeOneAuthException("无效的客户端ID或客户端密钥,请联系管理员进行分配");
        }
    }

    /**
     * 验证本地配置文件中的 clientId 和 clientSecret
     *
     * @param authRequest 获取token请求参数
     * @return 验证通过返回true，失败返回false
     */
    private boolean verifySecretConfigFromProperties(AuthRequest authRequest) {
        String clientId = authRequest.getClientId();
        String clientSecret = authRequest.getClientSecret();
        log.info(">>>>>>>>>>>>>>>>> neone server 验证：{} - {}", clientId, clientSecret);
        List<AuthRequest> allowRequestSecretGroups = this.neOneConfigProperties.getAllowRequestSecretGroups();
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>本地配置的认证客户信息：{}", allowRequestSecretGroups);
        if (CollectionUtils.isEmpty(allowRequestSecretGroups)) {
            return false;
        }
        log.info("----本地配置：{}", JSONUtil.toJsonStr(allowRequestSecretGroups));
        return Objects.nonNull(allowRequestSecretGroups.stream()
                .filter(item -> clientId.equals(item.getClientId()) && clientSecret.equals(item.getClientSecret()))
                .findFirst()
                .orElse(null));
    }

}
