// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.client;

import com.efreight.base.module.one.record.neone.controller.onerecord.NotificationMessage;
import com.efreight.base.module.one.record.neone.model.onerecord.Notification;
import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.rdf4j.model.IRI;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Set;

@Slf4j
@Component
public class RestClientBuilderProducer {

    private final RestTemplate restTemplate;
    private final Gson notificationMessageSerializer;

    public RestClientBuilderProducer() {
        this.restTemplate = new RestTemplate();
        this.notificationMessageSerializer = createNotificationMessageSerializer();
    }

    /**
     * 创建专门用于 NotificationMessage 的 Gson 序列化器
     * 符合 ONE Record API 规范的 JSON-LD 格式
     */
    private Gson createNotificationMessageSerializer() {
        return new GsonBuilder()
                .registerTypeAdapter(NotificationMessage.class, new NotificationMessageSerializer())
                .create();
    }

    /**
     * NotificationMessage 自定义序列化器
     * 生成符合 ONE Record API 规范的 JSON-LD 格式
     */
    private static class NotificationMessageSerializer implements JsonSerializer<NotificationMessage> {
        @Override
        public JsonElement serialize(NotificationMessage message, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject json = new JsonObject();

            // @context
            JsonObject contextObj = new JsonObject();
            contextObj.addProperty("api", "https://onerecord.iata.org/ns/api#");
            json.add("@context", contextObj);

            // @type
            json.addProperty("@type", "api:Notification");

            // 从 notification 对象中获取信息
            Notification notification = message.getNotification();
            if (notification != null) {
                // api:hasEventType
                JsonObject eventType = new JsonObject();
                eventType.addProperty("@id", notification.eventType().stringValue());
                json.add("api:hasEventType", eventType);

                // api:hasLogisticsObject
                notification.hasLogisticsObject().ifPresent(loIri -> {
                    JsonObject loObj = new JsonObject();
                    loObj.addProperty("@id", loIri.stringValue());
                    json.add("api:hasLogisticsObject", loObj);
                });

                // api:hasLogisticsObjectType
                notification.hasLogisticsObjectType().ifPresent(loType -> {
                    JsonObject loTypeObj = new JsonObject();
                    loTypeObj.addProperty("@type", "http://www.w3.org/2001/XMLSchema#anyURI");
                    loTypeObj.addProperty("@value", loType);
                    json.add("api:hasLogisticsObjectType", loTypeObj);
                });

                // api:isTriggeredBy
                notification.triggeredBy().ifPresent(triggered -> {
                    JsonObject triggeredObj = new JsonObject();
                    triggeredObj.addProperty("@id", triggered.stringValue());
                    json.add("api:isTriggeredBy", triggeredObj);
                });

                // api:hasChangedProperties (可选)
                Set<IRI> changedProperties = notification.changedProperties();
                if (changedProperties != null && !changedProperties.isEmpty()) {
                    JsonArray changedPropsArray = new JsonArray();
                    changedProperties.forEach(propIri -> {
                        JsonObject propObj = new JsonObject();
                        propObj.addProperty("@id", propIri.stringValue());
                        changedPropsArray.add(propObj);
                    });
                    json.add("api:hasChangedProperties", changedPropsArray);
                }
            }

            return json;
        }
    }

    /**
     * 发送通知消息
     *
     * @param callbackUrl     回调 URL
     * @param notificationMsg 通知消息
     * @param token           token
     * @param connectTimeout  连接超时时间（毫秒）
     * @param readTimeout     读取超时时间（毫秒）
     * @return 响应内容
     */
    public String sendNotification(String callbackUrl, NotificationMessage notificationMsg, String token, long connectTimeout, long readTimeout) {
        try {
            String jsonBody;
            if (notificationMsg instanceof NotificationMessage) {
                // 使用专用的 NotificationMessage 序列化器
                jsonBody = notificationMessageSerializer.toJson(notificationMsg);
            } else {
                // 使用默认序列化器
                jsonBody = new GsonBuilder().create().toJson(notificationMsg);
            }
            log.debug("Sending notification to [{}]: {}", callbackUrl, jsonBody);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("Authorization", "Bearer " + token);
            HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    callbackUrl,
                    HttpMethod.POST,
                    entity,
                    String.class
            );
            log.info("Notification sent successfully. Status: {}", response.getStatusCode());
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                throw new RuntimeException("Unexpected response status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Failed to send notification to [{}]", callbackUrl, e);
            throw new RuntimeException("Failed to send notification to " + callbackUrl, e);
        }
    }

    /**
     * 发送通知消息（使用默认超时时间）
     *
     * @param callbackUrl     回调 URL
     * @param notificationMsg 通知消息
     * @return 响应内容
     */
    public String sendNotification(String callbackUrl, NotificationMessage notificationMsg, String token) {
        return sendNotification(callbackUrl, notificationMsg, token, 5000, 30000);
    }

    /**
     * 获取 RestTemplate 实例，用于自定义配置
     */
    public RestTemplate getRestTemplate() {
        return this.restTemplate;
    }

    /**
     * 获取 NotificationMessage 序列化器（主要用于测试）
     */
    public Gson getNotificationMessageSerializer() {
        return this.notificationMessageSerializer;
    }

}
