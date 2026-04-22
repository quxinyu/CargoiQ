package com.efreight.base.module.one.record.neone.utils;

import com.efreight.base.module.one.record.neone.model.onerecord.LogisticsEventFSU;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LogisticsEventUtils {

    // ObjectMapper 是线程安全的，建议作为静态单例使用
    private static final ObjectMapper MAPPER = new ObjectMapper()
            // 如果 JSON 中出现实体类没有定义的字段，不要抛出异常
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    /**
     * 将 JSON 字符串解析为 LogisticsEvent 对象
     */
    public static LogisticsEventFSU fromJson(String json) {
        try {
            return MAPPER.readValue(json, LogisticsEventFSU.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("解析 JSON 到 LogisticsEvent 失败", e);
        }
    }

    /**
     * 将 LogisticsEvent 对象转换为 JSON 字符串
     * @param event 对象
     * @param prettyPrint 是否格式化输出（带缩进和换行）
     */
    public static String toJson(LogisticsEventFSU event, boolean prettyPrint) {
        try {
            if (prettyPrint) {
                return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(event);
            }
            return MAPPER.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("转换 LogisticsEvent 到 JSON 失败", e);
        }
    }
}