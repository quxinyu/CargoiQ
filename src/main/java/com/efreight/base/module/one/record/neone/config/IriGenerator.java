package com.efreight.base.module.one.record.neone.config;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.util.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

//@Component
//public class IriGenerator {

//    private static final Logger log = LoggerFactory.getLogger(IriGenerator.class);
//
//    private final OneRecordProperties properties;
//
//    public IriGenerator(OneRecordProperties properties) {
//        this.properties = properties;
//    }
//
//    /**
//     * 生成唯一的物流对象 IRI
//     * 格式: http://localhost:8080/logistics-objects/{uuid}
//     */
//    public IRI createLogisticsObjectIri() {
//        String baseUri = getBaseUri();
//        String randomId = generateRandomId();
//        String iriString = baseUri + "/logistics-objects/" + randomId;
//        return Values.iri(iriString);
//    }
//
//    /**
//     * 根据ID生成物流对象 IRI
//     */
//    public IRI getLogisticsObjectIri(String id) {
//        String baseUri = getBaseUri();
//        return Values.iri(baseUri + "/logistics-objects/" + id);
//    }
//
//    /**
//     * 生成物流事件 IRI
//     * 格式: http://localhost:8080/logistics-objects/{loId}/events/{eventId}
//     */
//    public IRI createLogisticsEventIri(IRI logisticsObjectIri) {
//        String randomId = generateRandomId();
//        String iriString = logisticsObjectIri.stringValue() + "/events/" + randomId;
//        return Values.iri(iriString);
//    }
//
//    /**
//     * 生成内部 IRI（用于系统内部引用）
//     * 格式: neone:{uuid}
//     */
//    public IRI createInternalIri() {
//        String randomId = generateRandomId();
//        String scheme = properties.getIri().getScheme();
//        return Values.iri(scheme + ":" + randomId);
//    }
//
//    /**
//     * 生成随机ID
//     */
//    public String generateRandomId() {
//        // 1. 获取随机ID策略并统一转小写（和原逻辑一致）
//        String randomIdStrategy = properties.getIri().getRandomIdStrategy().toLowerCase();
//
//        // 2. 改用Java 1.8支持的传统switch语句
//        switch (randomIdStrategy) {
//            case "nanoid":
//                return NanoIdUtils.randomNanoId();
//            default:
//                return UUID.randomUUID().toString();
//        }
//    }
//
//    /**
//     * 获取基础 URI
//     */
//    public String getBaseUri() {
//        OneRecordProperties.Server server = properties.getServer();
//        return String.format("%s://%s:%s%s",
//                server.getScheme(),
//                server.getHost(),
//                server.getPort(),
//                server.getRootPath()
//        );
//    }
//
//    /**
//     * 判断 IRI 是否为本地
//     */
//    public boolean isLocal(IRI iri) {
//        return iri.stringValue().startsWith(getBaseUri());
//    }
//
//    /**
//     * 判断 IRI 是否为内部ID
//     */
//    public boolean isInternalId(IRI iri) {
//        String scheme = properties.getIri().getScheme();
//        return iri.stringValue().startsWith(scheme + ":");
//    }
//}
