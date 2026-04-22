// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.config;

import com.efreight.base.module.one.record.neone.controller.onerecord.BodyHandlerBase;
import com.efreight.base.module.one.record.neone.controller.onerecord.CargoOntologyService;
import com.efreight.base.module.one.record.neone.controller.onerecord.LogisticsObjectList;
import com.efreight.base.module.one.record.neone.model.onerecord.IdProvider;
import com.efreight.base.module.one.record.neone.model.onerecord.LogisticsObject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.DynamicModelFactory;
import org.eclipse.rdf4j.model.util.Models;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Custom HttpMessageConverter for LogisticsObjectList
 * Converts JSON-LD/Turtle formats to LogisticsObjectList using RDF4J
 *
 * @author quxinyu
 * @since 2026-01-13
 */
@Slf4j
@Component
public class LogisticsObjectListHttpMessageConverter extends AbstractHttpMessageConverter<LogisticsObjectList> {

    public static final String APPLICATION_LD_JSON = "application/ld+json";
    public static final String APPLICATION_X_TURTLE = "application/x-turtle";
    public static final String TEXT_TURTLE = "text/turtle";

    private final CargoOntologyService cargoOntologyService;
    private final IdProvider idProvider;

    public LogisticsObjectListHttpMessageConverter(CargoOntologyService cargoOntologyService,
                                                   IdProvider idProvider) {
        super(
                new MediaType("application", "ld+json"),
                new MediaType("application", "x-turtle"),
                new MediaType("text", "turtle")
        );
        this.cargoOntologyService = cargoOntologyService;
        this.idProvider = idProvider;
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return LogisticsObjectList.class.isAssignableFrom(clazz);
    }

    @Override
    protected LogisticsObjectList readInternal(Class<? extends LogisticsObjectList> clazz,
                                               HttpInputMessage inputMessage) throws IOException {
        try {
            log.info("========== HttpMessageConverter 开始解析 ==========");
            MediaType mediaType = inputMessage.getHeaders().getContentType();
            if (mediaType == null) {
                mediaType = new MediaType("application", "ld+json");
            }
            log.info("Content-Type: {}", mediaType);

            // Create BodyHandlerBase to parse the input
            BodyHandlerBase handler = new BodyHandlerImpl(cargoOntologyService);

            // Parse the input stream
            InputStream inputStream = inputMessage.getBody();
            BodyHandlerBase.ModelAndMetadata modelAndMetadata = handler.processModelAndMetadata(
                    inputStream,
                    mediaType,
                    idProvider
            );

            // Verify root type is a LogisticsObject
            if (!rootTypeIsLo(modelAndMetadata.rootIriTypes())) {
                throw new IllegalArgumentException("Root type is not a LogisticsObject");
            }

            // Convert Model to LogisticsObjectList
            Model model = modelAndMetadata.model();
            IRI rootIri = modelAndMetadata.rootIri();
            Set<IRI> rootTypes = modelAndMetadata.rootIriTypes();

            log.info("根 IRI: {}", rootIri != null ? rootIri.stringValue() : "null");
            log.info("根类型数量: {}", rootTypes.size());
            rootTypes.forEach(type -> log.info("  - {}", type.stringValue()));

            // Split model into separate logistics objects
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

            // Create list of logistics objects
            List<LogisticsObject> logisticsObjects = new ArrayList<>();
            modelMapping.forEach((iri, localModel) ->
                    logisticsObjects.add(new LogisticsObject(iri, localModel))
            );

            // 打印所有 logistics objects 的 IRI
            log.info("========== 最终 logisticsObjects 列表 ==========");
            logisticsObjects.stream()
                    .map(lo -> lo.iri().stringValue())
                    .sorted()
                    .forEach(iri -> log.info("  - {}", iri));

            log.info("========== HttpMessageConverter 解析完成 ==========");

            // Create and return LogisticsObjectList
            return new LogisticsObjectList(logisticsObjects, rootIri, rootTypes);

        } catch (Exception e) {
            throw new HttpMessageNotReadableException(
                    "Failed to parse LogisticsObjectList from JSON-LD/Turtle: " + e.getMessage(),
                    inputMessage
            );
        }
    }

    /**
     * Check if the root types are LogisticsObject types
     */
    private boolean rootTypeIsLo(Set<IRI> rootTypes) {
        return cargoOntologyService.isLogisticsObject(
                rootTypes.stream()
                        .map(IRI::stringValue)
                        .toArray(String[]::new)
        );
    }

    /**
     * Recursively embed internal objects into the model
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

    @Override
    protected void writeInternal(LogisticsObjectList logisticsObjectList,
                                 HttpOutputMessage outputMessage) throws IOException {
        // Output writing is handled by the controller's response
        throw new UnsupportedOperationException(
                "Writing LogisticsObjectList is not supported. Use controller's response handling."
        );
    }

    /**
     * Simple BodyHandlerBase implementation for message conversion
     */
    private static class BodyHandlerImpl extends BodyHandlerBase {
        public BodyHandlerImpl(CargoOntologyService cargoOntologyService) {
            super(new org.eclipse.rdf4j.rio.WriterConfig(), cargoOntologyService);
        }
    }
}
