package com.efreight.base.module.one.record.neone.controller.api;

import com.efreight.base.common.core.model.Result;
import com.efreight.base.module.one.record.neone.controller.onerecord.BodyHandlerBase;
import com.efreight.base.module.one.record.neone.controller.onerecord.CargoOntologyService;
import com.efreight.base.module.one.record.neone.controller.onerecord.LogisticsObjectList;
import com.efreight.base.module.one.record.neone.model.onerecord.IdProvider;
import com.efreight.base.module.one.record.neone.service.onerecord.LogisticsObjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.DynamicModelFactory;
import org.eclipse.rdf4j.model.util.Models;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * NeOne 物流对象 API V2
 *
 * @author quxinyu
 * @since 2025-11-7
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/api/logistics-objects")
public class NeOneLogisticsObjectsV2Api {
    private final LogisticsObjectService logisticsObjectService;
    private final CargoOntologyService cargoOntologyService;
    private final IdProvider idProvider;


    /**
     * 接受 String 参数，内部转换为 LogisticsObjectList 后调用服务层
     * 返回 Result 类型以兼容 Feign 客户端
     * 注意：此方法仅用于内部服务调用，不应暴露给外部
     */
    @PostMapping(
            consumes = {"application/ld+json"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Result<String> createLogisticsObjectInternal(@RequestBody String oneRecordBody) {
        try {
            // 将 String 转换为 LogisticsObjectList
            LogisticsObjectList logisticsObjects = parseLogisticsObjectList(oneRecordBody);
            logisticsObjectService.createLogisticsObjects(logisticsObjects);
            return Result.ok(logisticsObjects.getRootIRI().stringValue());
        } catch (Exception e) {
            log.error("创建物流对象失败", e);
            return Result.fail("创建物流对象失败: " + e.getMessage());
        }
    }

    /**
     * 将 JSON-LD 字符串解析为 LogisticsObjectList
     * 复用 LogisticsObjectListHttpMessageConverter 的解析逻辑
     */
    private LogisticsObjectList parseLogisticsObjectList(String jsonLd) throws IOException {
        log.info("========== V2Api 开始解析 JSON-LD ==========");
        log.info("JSON-LD 长度: {} 字符", jsonLd.length());

        BodyHandlerBase handler = new BodyHandlerImpl(cargoOntologyService);

        try (InputStream inputStream = new ByteArrayInputStream(jsonLd.getBytes(StandardCharsets.UTF_8))) {
            BodyHandlerBase.ModelAndMetadata modelAndMetadata = handler.processModelAndMetadata(
                    inputStream,
                    new MediaType("application", "ld+json"),
                    idProvider
            );

            // 验证根类型是 LogisticsObject
            if (!rootTypeIsLo(modelAndMetadata.rootIriTypes())) {
                throw new IllegalArgumentException("Root type is not a LogisticsObject");
            }

            // 转换 Model 为 LogisticsObjectList
            Model model = modelAndMetadata.model();
            IRI rootIri = modelAndMetadata.rootIri();
            Set<IRI> rootTypes = modelAndMetadata.rootIriTypes();

            log.info("根 IRI: {}", rootIri != null ? rootIri.stringValue() : "null");
            log.info("根类型数量: {}", rootTypes.size());
            rootTypes.forEach(type -> log.info("  - {}", type.stringValue()));

            // 将 model 拆分为单独的 logistics objects
            Set<IRI> iris = Models.subjectIRIs(model);
            log.info("Model 中总的 subjects 数量: {}", iris.size());

            // 统计不同类型的 IRI
            Map<String, Integer> typeCount = new HashMap<>();
            iris.forEach(iri -> {
                String iriStr = iri.stringValue();
                String type = iriStr.contains("#") ? iriStr.substring(iriStr.lastIndexOf("#") + 1) : "unknown";
                if (type.length() > 50) {
                    type = type.substring(0, 50) + "...";
                }
                typeCount.merge(type, 1, Integer::sum);
            });
            log.info("按 IRI 类型统计:");
            typeCount.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .forEach(entry -> log.info("  {} : {}", entry.getKey(), entry.getValue()));

            Map<IRI, Model> modelMapping = new HashMap<>();
            int internalCount = 0;
            int externalCount = 0;

            log.info("========== 开始过滤 IRI ==========");
            List<IRI> sortedIris = new ArrayList<>(iris);
            sortedIris.sort((a, b) -> a.stringValue().compareTo(b.stringValue()));

            for (IRI iri : sortedIris) {
                boolean isInternal = idProvider.parse(iri).isInternal();
                if (isInternal) {
                    internalCount++;
                    log.debug("  [内部] IRI: {}", iri.stringValue());
                } else {
                    externalCount++;
                    log.debug("  [外部] IRI: {}", iri.stringValue());
                    Model localModel = new DynamicModelFactory().createEmptyModel();
                    localModel.addAll(model.filter(iri, null, null));
                    embedEmbeddedObjects(localModel, model, new HashSet<>());
                    modelMapping.put(iri, localModel);
                }
            }

            log.info("过滤结果: 内部 IRI = {}, 外部 IRI = {}", internalCount, externalCount);
            log.info("最终 logisticsObjects 数量: {}", modelMapping.size());

            // 创建 logistics objects 列表
            List<com.efreight.base.module.one.record.neone.model.onerecord.LogisticsObject> logisticsObjects = new ArrayList<>();
            modelMapping.forEach((iri, localModel) ->
                    logisticsObjects.add(new com.efreight.base.module.one.record.neone.model.onerecord.LogisticsObject(iri, localModel))
            );

            // 打印所有 logistics objects 的 IRI
            log.info("========== 最终 logisticsObjects 列表 ==========");
            logisticsObjects.stream()
                    .map(lo -> lo.iri().stringValue())
                    .sorted()
                    .forEach(iri -> log.info("  - {}", iri));

            log.info("========== V2Api 解析完成 ==========");

            // 创建并返回 LogisticsObjectList
            return new LogisticsObjectList(logisticsObjects, rootIri, rootTypes);
        }
    }

    /**
     * 检查根类型是否为 LogisticsObject 类型
     */
    private boolean rootTypeIsLo(Set<IRI> rootTypes) {
        return cargoOntologyService.isLogisticsObject(
                rootTypes.stream()
                        .map(IRI::stringValue)
                        .toArray(String[]::new)
        );
    }

    /**
     * 递归嵌入内部对象到 model 中
     */
    private void embedEmbeddedObjects(final Model model, final Model baseModel, Set<IRI> visited) {
        Set<IRI> iris = Models.objectIRIs(model).stream()
                .filter(iri -> idProvider.parse(iri).isInternal() && !visited.contains(iri))
                .collect(java.util.stream.Collectors.toSet());

        iris.forEach(iri -> {
            model.addAll(baseModel.filter(iri, null, null));
            visited.add(iri);
            embedEmbeddedObjects(model, baseModel, visited);
        });
    }

    /**
     * 简单的 BodyHandlerBase 实现用于消息转换
     */
    private static class BodyHandlerImpl extends BodyHandlerBase {
        public BodyHandlerImpl(CargoOntologyService cargoOntologyService) {
            super(new org.eclipse.rdf4j.rio.WriterConfig(), cargoOntologyService);
        }
    }

}
