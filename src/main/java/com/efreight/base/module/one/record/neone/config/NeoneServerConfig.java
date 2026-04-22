// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.config;

import com.efreight.base.module.one.record.neone.iata.onerecord.API;
import lombok.Data;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.DynamicModelFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * NeOne 服务器配置类
 * <p>
 * Spring Boot 配置属性，从 application.yml 读取配置
 * <p>
 * 配置前缀: neone-server
 * <p>
 * 使用示例:
 * <pre>
 * neone-server:
 *   supported-content-types:
 *     - application/ld+json
 *     - application/json
 *   supported-languages:
 *     - en
 *     - zh
 *   data-holder:
 *     id: test-data-holder
 *     name: Test Data Holder
 * </pre>
 *
 * @author quxinyu
 * @since 2025-01-09
 */
@Data
@Component
@ConfigurationProperties(prefix = "neone-server")
public class NeoneServerConfig {

    /**
     * 支持的内容类型
     * <p>
     * 默认值: application/ld+json, application/json
     */
    private Set<String> supportedContentTypes;

    /**
     * 支持的语言
     * <p>
     * 默认值: en, zh
     */
    private Set<String> supportedLanguages;

    /**
     * 数据持有者配置
     */
    private DataHolder dataHolder;

    /**
     * 获取支持的内容类型列表
     * <p>
     * 如果未配置，返回默认值
     *
     * @return 支持的内容类型集合
     */
    public Set<String> getSupportedContentTypes() {
        if (supportedContentTypes == null || supportedContentTypes.isEmpty()) {
            // 默认支持的内容类型
            supportedContentTypes = new java.util.HashSet<>();
            supportedContentTypes.add("application/ld+json");
            supportedContentTypes.add("application/json");
        }
        return supportedContentTypes;
    }

    /**
     * 获取支持的语言列表
     * <p>
     * 如果未配置，返回默认值
     *
     * @return 支持的语言集合
     */
    public Set<String> getSupportedLanguages() {
        if (supportedLanguages == null || supportedLanguages.isEmpty()) {
            // 默认支持的语言
            supportedLanguages = new java.util.HashSet<>();
            supportedLanguages.add("en");
            supportedLanguages.add("zh");
        }
        return supportedLanguages;
    }

    /**
     * 获取数据持有者 ID
     *
     * @return 数据持有者 ID，如果未配置则返回 null
     */
    public String getDataHolderId() {
        return dataHolder != null ? dataHolder.getId() : null;
    }

    /**
     * 获取数据持有者名称
     *
     * @return 数据持有者名称，如果未配置则返回 null
     */
    public String getDataHolderName() {
        return dataHolder != null ? dataHolder.getName() : null;
    }

    /**
     * 转换为服务器信息模型
     *
     * @param iri 服务器 IRI
     * @param dataHolderIri 数据持有者 IRI
     * @return RDF 模型
     */
    public Model toServerInformationModel(IRI iri, IRI dataHolderIri) {
        Model model = new DynamicModelFactory().createEmptyModel();
        model.add(iri, RDF.TYPE, API.ServerInformation);
        model.add(iri, API.hasServerEndpoint, SimpleValueFactory.getInstance().createLiteral(iri.stringValue()));

        if (supportedContentTypes != null) {
            supportedContentTypes.forEach(contentType ->
                    model.add(iri, API.hasSupportedContentType, SimpleValueFactory.getInstance().createLiteral(contentType))
            );
        }

        if (supportedLanguages != null) {
            supportedLanguages.forEach(lang ->
                    model.add(iri, API.hasSupportedLanguage, SimpleValueFactory.getInstance().createLiteral(lang))
            );
        }

        if (dataHolderIri != null) {
            model.add(iri, API.hasDataHolder, dataHolderIri);
        }

        return model;
    }

    /**
     * 数据持有者配置
     * <p>
     * 嵌套配置类，用于存储数据持有者的相关信息
     */
    @Data
    public static class DataHolder {
        /**
         * 数据持有者 ID
         * <p>
         * 必填项，用于唯一标识数据持有者
         */
        private String id;

        /**
         * 数据持有者名称
         * <p>
         * 可选项，用于显示数据持有者的名称
         */
        private String name;
    }
}
