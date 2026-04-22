package com.efreight.base.module.one.record.neone.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Http客户端封装
 *
 * @author Ling, Jiatong
 * Date: 2020/10/23 9:09 上午
 */
@Component
public class HttpClientUtil {

    @Autowired
    @Qualifier("commonRestTemplate")
    private RestTemplate restTemplate;

    /**
     * GET请求调用
     *
     * @param url          请求URL
     * @param responseType 返回对象类型
     * @return 响应对象封装类
     */
    public <T> ResponseEntity<T> get(String url, Class<T> responseType) {
        return restTemplate.getForEntity(url, responseType);
    }

    /**
     * GET请求调用（携带请求头）
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param responseType 返回对象类型
     * @return 响应对象封装类
     */
    public <T> ResponseEntity<T> get(String url, HttpHeaders headers, Class<T> responseType) {
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, requestEntity, responseType);
    }

    // ----------------------------------POST-------------------------------------------------------

    /**
     * POST请求调用
     *
     * @param url          请求URL
     * @param responseType 返回对象类型
     * @return 响应对象封装类
     */
    public <T> ResponseEntity<T> post(String url, Class<T> responseType) {
        return restTemplate.postForEntity(url, HttpEntity.EMPTY, responseType);
    }

    /**
     * POST请求调用（携带请求体）
     *
     * @param url          请求URL
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @return 响应对象封装类
     */
    public <T> ResponseEntity<T> post(String url, Object requestBody, Class<T> responseType) {
        return restTemplate.postForEntity(url, requestBody, responseType);
    }

    /**
     * POST请求调用（携带请求头）
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param responseType 返回对象类型
     * @return 响应对象封装类
     */
    public <T> ResponseEntity<T> post(String url, HttpHeaders headers, Class<T> responseType) {
        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, responseType);
    }

    /**
     * POST请求调用（携带请求头和请求体）
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @return 响应对象封装类
     */
    public <T> ResponseEntity<T> post(String url, HttpHeaders headers, Object requestBody, Class<T> responseType) {
        HttpEntity<Object> requestEntity = new HttpEntity<>(requestBody, headers);
        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, responseType);
    }


    /**
     * POST请求调用（表单模式）
     *
     * @param url          请求URL
     * @param entity      请求参数体
     * @return 响应对象封装类
     */
    public String postForFormUrlencoded(String url, HttpEntity<MultiValueMap<String, String>> entity) {
        return restTemplate.postForObject(url, entity, String.class);
    }
}
