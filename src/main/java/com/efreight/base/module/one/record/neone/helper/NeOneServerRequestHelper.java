package com.efreight.base.module.one.record.neone.helper;

import com.alibaba.fastjson2.JSON;
import com.efreight.base.module.one.record.neone.ex.NeOneRequestWrapperException;
import com.efreight.base.module.one.record.neone.ex.SubscribeNotifyException;
import com.efreight.base.module.one.record.neone.model.vo.SubscriptionResponseVo;
import com.efreight.base.module.one.record.neone.utils.HttpClientUtil;
import com.efreight.base.module.one.record.neone.utils.UrlFormatUtils;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * @author qiuguan
 * @date 2024/09/16 13:00:08  星期一
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class NeOneServerRequestHelper {

    private static final Configuration CONFIG = Configuration.defaultConfiguration().addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);

    private final OkHttpClient okHttpClient;

    private final HttpClientUtil httpClientUtil;

    public String ping(String url) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>和目标1R连通性测试 url: {}", url);
        Request request = new Request.Builder().url(url).build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                if (body == null) {
                    throw new NeOneRequestWrapperException("根据[" + url + "] 无法和目标1R进行联通");
                }
                log.info(">>>>>>>>>>>>>>>>>>>>>>>>>和目标1R连通性测试成功!");
                return body.string();
            }
        } catch (IOException e) {
            log.error(">>>>>>>>>>>>>>>>>>>>>>>>>和目标1R连通性测试发生错误", e);
            throw new NeOneRequestWrapperException("和目标1R连通性测试发生错误：" + e.getMessage());
        }

        return null;
    }

    /**
     * 根据company lo iri 获取company基本信息
     *
     * @return
     */
    public String getCompanyServer(String companyIri) {
        Request request = new Request.Builder()
                .url(companyIri)
                .build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                if (body == null) {
                    throw new NeOneRequestWrapperException("根据 company holder iri = [" + companyIri + "] 无法获取 company info");
                }
                return body.string();
            }
        } catch (IOException e) {
            log.error(">>>>>>>>>>>>>>>>>>>>>>>>>根据 company iri 获取company info 发生错误", e);
            throw new NeOneRequestWrapperException("根据 company holder iri = [" + companyIri + "] 获取 company info 发生错误：" + e.getMessage());
        }

        return null;
    }

    public SubscriptionResponseVo subscribe(String host, String subscribeUrl, String jsonData, String subscribeToken) throws Exception {
        if (StringUtils.isNotBlank(subscribeUrl)) {
            host = UrlFormatUtils.resolveUrl(host + subscribeUrl);
        } else {
            // 对方为ebase的1R服务
            host = UrlFormatUtils.resolveUrl(host + "/apis/callback/subscribe");
        }
        return postSubscribe(host, jsonData, subscribeToken);
    }

    public SubscriptionResponseVo updateSubscribe(String host, String requestJsonBody, String subscribeToken) throws Exception {
        return postSubscribe(host, requestJsonBody, subscribeToken);
    }

    public boolean unsubscribe(String targetHost, String host) throws Exception {
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        Map<String, String> map = new HashMap<>();
        map.put("host", host);
        RequestBody body = RequestBody.create(JSON.toJSONString(map), mediaType);

        Request request = new Request.Builder()
                .url(UrlFormatUtils.resolveUrl(targetHost + "/apis/callback/unsubscribe"))
                .post(body)
                .build();

        try (Response response = this.okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                assert response.body() != null;
                String string = response.body().string();
                String code = JsonPath.using(CONFIG).parse(string).read("$.code");
                if ("200".equals(code)) {
                    return true;
                }
                throw new SubscribeNotifyException("取消订阅失败：" + string);
            } else {
                throw new SubscribeNotifyException("取消订阅失败：" + (response.body() != null ? response.body().string() : "错误码：" + response.code() + ", 错误原因：" + response.message() + ", 请求的URL:" + request.url()));
            }
        } catch (SubscribeNotifyException e) {
            throw e;
        } catch (Exception e) {
            log.error(">>>>>>>>>>>>>>>>>>>>>>>>>>> 取消订阅请求发生未知错误", e);
            throw e;
        }
    }

    private SubscriptionResponseVo postSubscribe(String url, String requestJsonBody, String subscribeToken) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/ld+json; version=2.1.0");
        headers.add("Accept", "application/ld+json; version=2.1.0");
        headers.add("Authorization", "Bearer " + subscribeToken);
        log.info(">>>>>>>>>>>>>>>>>>>>> 请求订阅消息体打印: {}, 地址打印: {}, token打印: {}", requestJsonBody, url, subscribeToken);
        SubscriptionResponseVo responseVo = new SubscriptionResponseVo();
        responseVo.setStatus(false);
        try {
            ResponseEntity<String> response = httpClientUtil.post(url, headers, requestJsonBody, String.class);
            log.info(">>>>>>>>>>>>>>>>>>>>> 请求订阅 response 打印: {}, ", response);
            if (ObjectUtils.isEmpty(response)) {
                log.error(">>>>>>>>>>>>>>>>>>>>> 请求订阅 response 为空!");
                throw new SubscribeNotifyException("请求订阅 response 为空!");
            }
            log.info(">>>>>>>>>>>>>>>>>>>>> 请求订阅 response body: {},response headers: {},response status: {}, ",
                    response.getBody(), response.getHeaders(), response.getStatusCode());
            responseVo.setCode(response.getStatusCode().value());
            if (response.getStatusCode() == HttpStatus.CREATED) {
                log.info("----请求订阅成功，请求结果：{}", com.alibaba.fastjson.JSON.toJSONString(response));
                HttpHeaders httpHeaders = response.getHeaders();
                URI locationUri = httpHeaders.getLocation();
                String locationStr = StringUtils.EMPTY;
                if (ObjectUtils.isNotEmpty(locationUri)) {
                    locationStr = locationUri.toString();
                    responseVo.setStatus(true);
                    responseVo.setLocationUri(locationStr);
                    log.info(">>>>>>>>>>>>>>>>>>>>> 获取locationUri: {}", locationStr);
                }
                return responseVo;
            } else {
                log.error("----请求订阅失败，请求结果：{}", com.alibaba.fastjson.JSON.toJSONString(response));
                throw new SubscribeNotifyException("订阅失败：" + response.getBody() != null ? response.getBody() :
                        "错误码：" + response.getStatusCode() + ", 错误原因：" + response.getBody() + ", 请求的URL:" + url);
            }
        } catch (Throwable e) {
            log.error("----请求订阅失败，原因：{}", e.getMessage());
            throw new SubscribeNotifyException("订阅失败：" + e.getMessage());
        }
    }

}
