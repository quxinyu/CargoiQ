package com.efreight.base.module.one.record.neone.config;

import com.alibaba.fastjson2.JSON;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * NE ONE 数据模型配置类
 *
 * @author quxinyu
 * @since 2024-12-24
 */
@Data
@Slf4j
@Component
@ConfigurationProperties(prefix = "neone")
public class NeOneDataModelProperties {

    /**
     * 数据模型映射配置
     * key: 公司名称
     * value: 数据模型名称
     *
     * 支持两种配置格式：
     * 1. 标准 YAML 格式（推荐）：
     * neone:
     *   data-model:
     *     海航生产: OneRecord_HU
     *     国航生产: OneRecord_CA
     *
     * 2. JSON 字符串格式（兼容旧配置）：
     * neone:
     *   data-model: '{"海航生产":"OneRecord_HU","国航生产":"OneRecord_CA"}'
     */
    private Map<String, String> dataModel = new HashMap<>(Collections.singletonMap("海航生产", "OneRecord_HU"));

    /**
     * 自定义 Setter 方法，兼容 JSON 字符串格式
     * 支持两种格式：
     * 1. Map 类型：直接使用
     * 2. String 类型：解析为 JSON 后转换为 Map
     */
    public void setDataModel(Object dataModel) {
        if (dataModel == null) {
            this.dataModel = new HashMap<>();
            return;
        }

        // 如果已经是 Map 类型，直接使用
        if (dataModel instanceof Map) {
            this.dataModel = (Map<String, String>) dataModel;
            log.debug("使用 Map 格式的 data-model 配置: {}", this.dataModel);
        }
        // 如果是 String 类型，尝试解析为 JSON
        else if (dataModel instanceof String) {
            try {
                String jsonStr = (String) dataModel;
                // 去除可能的外层引号
                jsonStr = jsonStr.trim();
                if (jsonStr.startsWith("'") && jsonStr.endsWith("'")) {
                    jsonStr = jsonStr.substring(1, jsonStr.length() - 1);
                } else if (jsonStr.startsWith("\"") && jsonStr.endsWith("\"")) {
                    jsonStr = jsonStr.substring(1, jsonStr.length() - 1);
                }

                // 替换单引号为双引号（兼容 JSON 格式）
                jsonStr = jsonStr.replace("'", "\"");

                // 解析 JSON
                this.dataModel = JSON.parseObject(jsonStr, Map.class);
                log.debug("解析 JSON 字符串格式的 data-model 配置: {}", this.dataModel);
            } catch (Exception e) {
                log.warn("解析 data-model JSON 字符串失败: {}, 使用默认配置", dataModel, e);
                this.dataModel = new HashMap<>(Collections.singletonMap("海航生产", "OneRecord_HU"));
            }
        } else {
            log.warn("不支持的 data-model 配置类型: {}, 使用默认配置", dataModel.getClass());
            this.dataModel = new HashMap<>(Collections.singletonMap("海航生产", "OneRecord_HU"));
        }
    }

    /**
     * 默认数据模型名称
     *
     * 配置示例：
     * neone:
     *   default: OneRecord_HU
     */
    private String defaultModel = "OneRecord_HU";

    /**
     * 初始化方法
     * 在 Bean 初始化完成后自动执行，用于打印配置信息
     */
    @PostConstruct
    public void init() {
        log.info("========================================");
        log.info(">>>>>>>>>>>>>>>>>>>> NE ONE 数据模型配置类初始化完成");
        log.info(">>>>>>>>>>>>>>>>>>>> 默认数据模型: {}", defaultModel);
        log.info(">>>>>>>>>>>>>>>>>>>> 数据模型映射配置数量: {}", dataModel != null ? dataModel.size() : 0);
        if (dataModel != null && !dataModel.isEmpty()) {
            log.info(">>>>>>>>>>>>>>>>>>>> 数据模型映射详情:");
            dataModel.forEach((company, model) ->
                log.info(">>>>>>>>>>>>>>>>>>>>   - 公司: [{}] -> 数据模型: [{}]", company, model)
            );
        } else {
            log.warn(">>>>>>>>>>>>>>>>>>>> 警告: 数据模型映射为空，将使用默认模型");
        }
        log.info(">>>>>>>>>>>>>>>>>>>> 配置来源: 本地 application.yaml");
        log.info("========================================");
    }

    /**
     * 根据公司名称获取数据模型名称
     * 如果未找到对应配置，则返回默认模型
     *
     * @param companyName 公司名称
     * @return 数据模型名称
     */
    public String getModelByCompanyName(String companyName) {
        if (StringUtils.isNotBlank(companyName)){
            log.debug(">>>>>>>>企业名称为空，使用默认模型: {} <<<<<<<<<", defaultModel);
            return defaultModel;
        }
        if (dataModel == null || dataModel.isEmpty()) {
            log.debug(">>>>>>>>数据模型配置为空，使用默认模型: {} <<<<<<<<<", defaultModel);
            return defaultModel;
        }

        String model = dataModel.get(companyName);
        if (StringUtils.isBlank(model)) {
            log.debug(">>>>>>>>未找到公司 [{}] 的数据模型配置，使用默认模型: {} <<<<<<<<<", companyName, defaultModel);
            return defaultModel;
        }

        log.debug(">>>>>>>>公司 [{}] 使用数据模型: {} <<<<<<<<<", companyName, model);
        return model;
    }

    /**
     * 兼容旧代码，提供 getDataModelMap 方法
     *
     * @return 数据模型映射
     */
    public Map<String, String> getDataModelMap() {
        return dataModel;
    }
}
