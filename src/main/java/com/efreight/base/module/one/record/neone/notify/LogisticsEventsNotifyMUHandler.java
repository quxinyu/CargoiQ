package com.efreight.base.module.one.record.neone.notify;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.efreight.base.module.one.record.neone.enums.NotifyEventType;
import com.efreight.base.module.one.record.neone.helper.IriGenerator;
import com.efreight.base.module.one.record.neone.model.entity.NeOneLogisticsEvents;
import com.efreight.base.module.one.record.neone.utils.HttpClientUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

/**
 * todo 不确定是联调还是生产使用  暂注释掉
 *
 * 物流事件通知处理
 *
 * @author fu yuan hui
 * @since 2024-09-18 11:43:57 星期三
 */
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class LogisticsEventsNotifyMUHandler extends AbstractNotifyHandler<NeOneLogisticsEvents> {
//
//    // 运维配置了网络策略，访问http://oner.mu.com:8081/，对应http://111.28.143.100:8080/
//    // 域名有问题，改成访问nginx服务器的ip和端口
//    private final String MU_1R_HOST = "http://10.126.58.249:8081/";
//
//    private final HttpClientUtil httpClientUtil;
//
//    private final IriGenerator iriGenerator;
//
//
//    @Override
//    protected boolean isSupport(NotifyEventType eventType) {
//        return NotifyEventType.LOGISTICS_OBJECT_CREATED == eventType ||  NotifyEventType.LOGISTICS_EVENT_RECEIVED_MU == eventType ||  NotifyEventType.LOGISTICS_EVENT_RECEIVED == eventType;
//    }
//
//    @Override
//    protected void doNotify(NeOneLogisticsEvents neOneLogisticsObjectsEvent) {
//        log.info("----需要通知处理的事件：{}----", JSON.toJSONString(neOneLogisticsObjectsEvent));
//        NotifyEventType notifyEventType = neOneLogisticsObjectsEvent.getNotifyEventType();
//        if (notifyEventType == NotifyEventType.LOGISTICS_EVENT_RECEIVED_MU) {
//            String loId = neOneLogisticsObjectsEvent.getLoId();
//            if (!StringUtils.hasLength(loId)) {
//                return;
//            }
//            // 发给海航
//            String iri = iriGenerator.generateLogisticsObjectLoId(loId);
//            log.info("----iri：{}----", iri);
//            sendToHU(iri);
//        }
//    }
//
//    /**
//     * 将事件发送给海航
//     *
//     * @param iri 物流对象的获取地址
//     */
//    private void sendToHU(String iri) {
//        // 先获取token
//        String token = getMuToken();
//        if (!StringUtils.hasLength(token)) {
//            log.error("----海航1R服务器TOKEN为空，放弃通知");
//            return;
//        }
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/ld+json; version=2.1.0");
//        headers.add("Accept", "application/ld+json; version=2.1.0");
//        headers.add("Authorization", "Bearer " + token);
//        try {
//            JSONObject body = new JSONObject();
//            JSONObject context = new JSONObject();
//            JSONObject hasEventType = new JSONObject();
//            JSONObject hasLogisticsObject = new JSONObject();
//            JSONObject hasLogisticsObjectType = new JSONObject();
//
//            context.put("api", "https://onerecord.iata.org/ns/api#");
//            hasEventType.put("@id", "api:LOGISTICS_EVENT_RECEIVED");
//            hasLogisticsObject.put("@id", iri);
//            hasLogisticsObjectType.put("@type", "http://www.w3.org/2001/XMLSchema#anyURI");
//            hasLogisticsObjectType.put("@value", "https://onerecord.iata.org/ns/cargo#Waybill");
//
//            body.put("@context", context);
//            body.put("@type", "api:Notification");
//            body.put("api:hasEventType", hasEventType);
//            body.put("api:hasLogisticsObject", hasLogisticsObject);
//            body.put("api:hasLogisticsObjectType", hasLogisticsObjectType);
//            ResponseEntity<String> response = httpClientUtil.post(MU_1R_HOST + "notifications", headers, body.toJSONString(), String.class);
//            if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
//                log.info("----通知海航成功，请求结果：{}", JSON.toJSONString(response));
//            } else {
//                log.error("----通知海航失败，请求结果：{}", JSON.toJSONString(response));
//            }
//        } catch (Throwable e) {
//            log.error("----通知海航失败，原因：{}", e.getMessage());
//            log.error(e.toString(), e);
//        }
//    }
//
//    /**
//     * 获取海航1R服务器token
//     *
//     * @return token
//     */
//    private String getMuToken() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/x-www-form-urlencoded");
//        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
//        formData.add("username", "airport.gz");
//        formData.add("password", "1234.qwe");
//        formData.add("grant_type", "client_credentials");
//        formData.add("client_id", "neone-client");
//        formData.add("client_secret", "lx7ThS5aYggdsMm42BP3wMrVqKm9WpNY");
//        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(formData, headers);
//        String token = null;
//        try {
//            String response = httpClientUtil.postForFormUrlencoded(MU_1R_HOST + "realms/neone/protocol/openid-connect/token", entity);
//            log.info("----获取海航TOKEN请求结果：{}", response);
//            token = JSON.parseObject(response).getString("access_token");
//        } catch (Throwable e) {
//            log.error("----获取海航1R服务器的TOKEN失败，原因：{}", e.getMessage());
//            log.error(e.toString(), e);
//        }
//        return token;
//    }
//}
