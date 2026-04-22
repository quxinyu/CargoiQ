package com.efreight.base.module.one.record.neone.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.efreight.base.common.core.exception.OneRecordServerException;
import com.efreight.base.module.one.record.neone.constants.RedisConstants;
import com.efreight.base.module.one.record.neone.model.entity.NeOneAuthToken;
import com.efreight.base.module.one.record.neone.model.entity.NeOneServerCompanyHolder;
import com.efreight.base.module.one.record.neone.service.NeOneAuthTokenService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.concurrent.TimeUnit;

/**
 * KeyLockTokenUtils工具类
 *
 * @author quxinyu
 */
@Slf4j
@Component
public final class KeyLockTokenUtils {

    private static HttpClientUtil httpClientUtil;
    private static StringRedisTemplate stringRedisTemplate;
    private static NeOneAuthTokenService neOneAuthTokenService;

    public KeyLockTokenUtils(StringRedisTemplate stringRedisTemplate, NeOneAuthTokenService neOneAuthTokenService, HttpClientUtil httpClientUtil) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.neOneAuthTokenService = neOneAuthTokenService;
        this.httpClientUtil = httpClientUtil;
    }

    public static String getCompanyToekn(NeOneServerCompanyHolder companyHolder) {
        String subscribeToken = stringRedisTemplate.opsForValue().get(RedisConstants.COMPANY_TOKEN_KEY + companyHolder.getCompanyName());
//        if (StringUtils.isNotBlank(subscribeToken)) {
//            return subscribeToken;
//        }
        LambdaQueryWrapper<NeOneAuthToken> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(NeOneAuthToken::getOrgName, companyHolder.getCompanyName());
        NeOneAuthToken neOneAuthToken = neOneAuthTokenService.getOne(wrapper);
        if (ObjectUtils.isEmpty(neOneAuthToken)) {
            throw new OneRecordServerException(String.format("未获取到服务地址: %s 对应的KeyLock配置!", companyHolder.getCompanyName()));
        }
        String opposite = neOneAuthToken.getOpposite();
        if (StringUtils.isBlank(opposite)) {
            throw new OneRecordServerException(String.format("请完善公司: %s 提供的KeyLock配置!", companyHolder.getCompanyName()));
        }
        JSONObject oppositeObject = JSON.parseObject(opposite);
        String url = UrlFormatUtils.resolveUrl(companyHolder.getHost() + companyHolder.getTokenUrl());
        log.info(String.format("调用公司: %s 获取token请求地址: %s", companyHolder.getCompanyName(), url));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("username", String.valueOf(oppositeObject.get("username")));
        formData.add("password", String.valueOf(oppositeObject.get("password")));
        formData.add("grant_type", String.valueOf(oppositeObject.get("grant_type")));
        formData.add("client_id", String.valueOf(oppositeObject.get("client_id")));
        formData.add("client_secret", String.valueOf(oppositeObject.get("client_secret")));
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(formData, headers);
        log.info(String.format("调用公司: %s 获取token请求参数体: %s", companyHolder.getCompanyName(), entity));
        try {
            String response = httpClientUtil.postForFormUrlencoded(url, entity);
            log.info(String.format("调用公司: %s 获取token 返回信息: %s", companyHolder.getCompanyName(), response));
            subscribeToken = JSON.parseObject(response).getString("access_token");
        } catch (Throwable e) {
            log.error("----获取1R服务器的TOKEN失败，原因：{}", e.getMessage());
            log.error(e.toString(), e);
        }
        log.info(">>>>>>>>>>>>>>调用: {} 获取token : {}", companyHolder.getCompanyName(), subscribeToken);
        if (StringUtils.isNotBlank(subscribeToken)) {
            Integer tokenExp = companyHolder.getTokenExp();
            if (ObjectUtils.isNotEmpty(tokenExp)) {
                stringRedisTemplate.opsForValue().set(RedisConstants.COMPANY_TOKEN_KEY + companyHolder.getCompanyName(), subscribeToken,
                        tokenExp, TimeUnit.HOURS);
            } else {
                stringRedisTemplate.opsForValue().set(RedisConstants.COMPANY_TOKEN_KEY + companyHolder.getCompanyName(), subscribeToken);
            }
        }
        return subscribeToken;
    }
}
