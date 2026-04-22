// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.config;

import com.efreight.base.module.one.record.neone.controller.onerecord.CargoOntologyService;
import com.efreight.base.module.one.record.neone.handler.LogisticsEventHandler;
import com.efreight.base.module.one.record.neone.iata.onerecord.CARGO;
import com.efreight.base.module.one.record.neone.iata.onerecord.NEONE;
import com.efreight.base.module.one.record.neone.model.onerecord.IdProvider;
import com.efreight.base.module.one.record.neone.model.onerecord.LogisticsEvent;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.impl.DynamicModelFactory;
import org.eclipse.rdf4j.model.util.Models;
import org.eclipse.rdf4j.model.util.Statements;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFHandlerException;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.helpers.AbstractRDFHandler;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * LogisticsEvent 对象的自定义 HttpMessageConverter
 * 将 JSON-LD/Turtle 格式转换为 LogisticsEvent 对象
 *
 * @author quxinyu
 * @since 2026-01-16
 */
@Component
public class LogisticsEventHttpMessageConverter extends AbstractHttpMessageConverter<LogisticsEvent> {

    public static final String APPLICATION_LD_JSON = "application/ld+json";
    public static final String APPLICATION_X_TURTLE = "application/x-turtle";
    public static final String TEXT_TURTLE = "text/turtle";
    public static final String TYPE_HEADER = "Type";

    private final LogisticsEventHandler logisticsEventHandler;
    private final IdProvider idProvider;
    private final CargoOntologyService cargoOntologyService;

    public LogisticsEventHttpMessageConverter(LogisticsEventHandler logisticsEventHandler,
                                              IdProvider idProvider,
                                              CargoOntologyService cargoOntologyService) {
        super(
                new MediaType("application", "ld+json"),
                new MediaType("application", "x-turtle"),
                new MediaType("text", "turtle")
        );
        this.logisticsEventHandler = logisticsEventHandler;
        this.idProvider = idProvider;
        this.cargoOntologyService = cargoOntologyService;
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return LogisticsEvent.class.isAssignableFrom(clazz);
    }

    @Override
    protected LogisticsEvent readInternal(Class<? extends LogisticsEvent> clazz,
                                          HttpInputMessage inputMessage) throws IOException {
        try {
            // 获取内容类型
            MediaType mediaType = inputMessage.getHeaders().getContentType();
            if (mediaType == null) {
                mediaType = new MediaType("application", "ld+json");
            }

            // 处理 RDF Model，替换空白节点为内部 IRI
            InputStream inputStream = inputMessage.getBody();
            ModelAndMetadata modelAndMetadata = processModelAndMetadata(inputStream, mediaType);
            Model model = modelAndMetadata.model;
            IRI rootIri = modelAndMetadata.rootIri;

            // 使用 LogisticsEventHandler 从 Model 转换为 LogisticsEvent 对象
            return logisticsEventHandler.fromModel(rootIri, model);

        } catch (IOException | NoSuchElementException e) {
            throw new HttpMessageNotReadableException(
                    "无效的 LogisticsEvent 结构: " + e.getMessage(), inputMessage
            );
        } catch (Exception e) {
            throw new HttpMessageNotReadableException(
                    "从 JSON-LD/Turtle 解析 LogisticsEvent 失败: " + e.getMessage(), inputMessage
            );
        }
    }

    @Override
    protected void writeInternal(LogisticsEvent logisticsEvent,
                                 HttpOutputMessage outputMessage) throws IOException {
        try {
            // 将 LogisticsEvent 对象转换为 Model
            Model model = logisticsEventHandler.fromJava(logisticsEvent);

            // 获取响应的媒体类型
            MediaType mediaType = outputMessage.getHeaders().getContentType();
            if (mediaType == null) {
                mediaType = new MediaType("application", "ld+json");
            }

            // 获取对应的 RDFFormat
            RDFFormat rdfFormat = getRdfFormat(mediaType);

            // 设置 Type header
            Set<IRI> rootIris = getRootIris(model);
            if (rootIris.size() == 1) {
                Models.objectIRIs(model.filter(rootIris.iterator().next(), RDF.TYPE, null))
                        .forEach(iri -> outputMessage.getHeaders().add(TYPE_HEADER, iri.stringValue()));
            }

            // 设置默认命名空间
            model.setNamespace("", CARGO.NAMESPACE);

            // 写入响应流
            Rio.write(model, outputMessage.getBody(), rdfFormat);

        } catch (Exception e) {
            throw new HttpMessageNotWritableException(
                    "序列化 LogisticsEvent 失败: " + e.getMessage(), e
            );
        }
    }

    /**
     * 将 MediaType 转换为 RDFFormat
     */
    private RDFFormat getRdfFormat(MediaType mediaType) {
        String mimeType = mediaType.getType() + "/" + mediaType.getSubtype();
        switch (mimeType) {
            case APPLICATION_LD_JSON:
                return RDFFormat.JSONLD;
            case APPLICATION_X_TURTLE:
            case TEXT_TURTLE:
                return RDFFormat.TURTLE;
            default:
                throw new IllegalArgumentException("不支持的媒体类型: " + mimeType);
        }
    }

    /**
     * 解析 RDF 流并替换空白节点
     */
    private ModelAndMetadata processModelAndMetadata(InputStream inputStream,
                                                     MediaType mediaType) throws IOException {
        RDFParser parser = Rio.createParser(getRdfFormat(mediaType));
        BnodeReplacingStatementHandler statementHandler =
                new BnodeReplacingStatementHandler(idProvider, cargoOntologyService);
        parser.setRDFHandler(statementHandler);
        parser.parse(inputStream);

        return new ModelAndMetadata(statementHandler.getModel(),
                statementHandler.getRootIri(), statementHandler.getRootTypes());
    }

    /**
     * 获取根 IRI
     */
    private Set<IRI> getRootIris(Model model) {
        // set of all subject IRIs
        Set<IRI> subjectIris = Models.subjectIRIs(model);
        // set of all object IRIs
        Set<IRI> objectIris = Models.objectIRIs(model);

        // subtract set of objects from set of subjects
        subjectIris.removeAll(objectIris);

        return subjectIris;
    }

    /**
     * 替换空白节点的 StatementHandler
     */
    private static class BnodeReplacingStatementHandler extends AbstractRDFHandler {

        private final Map<String, IRI> iriMap = new HashMap<>();
        private final Model firstPass = new DynamicModelFactory().createEmptyModel();
        private final Model result = new DynamicModelFactory().createEmptyModel();
        private final IdProvider idProvider;
        private final CargoOntologyService cargoOntologyService;
        private IRI rootIri;
        private Set<IRI> rootTypes;

        public BnodeReplacingStatementHandler(IdProvider idProvider, CargoOntologyService cargoOntologyService) {
            this.idProvider = idProvider;
            this.cargoOntologyService = cargoOntologyService;
        }

        public Model getModel() {
            return result;
        }

        public IRI getRootIri() {
            return rootIri;
        }

        public Set<IRI> getRootTypes() {
            return rootTypes;
        }

        @Override
        public void handleStatement(Statement st) throws RDFHandlerException {
            Resource subject = st.getSubject();
            Value object = st.getObject();

            if (st.getSubject().isBNode()) {
                subject = mapBnode((BNode) subject);
            }

            if (st.getObject().isBNode()) {
                object = mapBnode((BNode) object);
            }

            firstPass.add(subject, st.getPredicate(), object, st.getContext());
        }

        @Override
        public void handleNamespace(String prefix, String uri) throws RDFHandlerException {
            firstPass.setNamespace(prefix, uri);
        }

        @Override
        public void endRDF() throws RDFHandlerException {
            super.endRDF();
            Map<IRI, IRI> mappedIris = new HashMap<>();
            // get all subject IRIs
            Set<IRI> iris = Models.subjectIRIs(firstPass);
            for (IRI iri : iris) {
                // get RDF.TYPE
                Set<IRI> types = Models.objectIRIs(firstPass.filter(iri, RDF.TYPE, null));
                String[] typeArray = new String[types.size()];
                int i = 0;
                for (IRI type : types) {
                    typeArray[i++] = type.stringValue();
                }
                if (cargoOntologyService.isLogisticsObject(typeArray)) {
                    if (idProvider.parse(iri).isInternal()) {
                        // add the newly created IRI to the mapping table
                        mappedIris.put(iri, idProvider.createUniqueLoUri().getIri());
                    } else {
                        // mark the subject as a subject with a predefined IRI
                        firstPass.add(iri, NEONE.hasPredefinedIri, Values.literal(true));
                    }
                }
            }
            final Set<IRI> subjects = new HashSet<>();
            final Set<IRI> objects = new HashSet<>();
            for (Statement statement : firstPass) {
                Resource subject = statement.getSubject();
                Value object = statement.getObject();

                Optional<Resource> mappedSubject = Optional.ofNullable(mappedIris.get(subject));
                Optional<Value> mappedObject = Optional.ofNullable(mappedIris.get(object));
                Resource newSubject = mappedSubject.orElse(subject);
                Value newObject = mappedObject.orElse(object);

                Statement mappedStatement = Statements.statement(
                        newSubject,
                        statement.getPredicate(),
                        newObject,
                        statement.getContext()
                );

                if (newSubject.isIRI()) {
                    subjects.add((IRI) newSubject);
                }
                if (newObject.isIRI()) {
                    objects.add((IRI) newObject);
                }

                // add mapped statements
                result.add(mappedStatement);

                // add namespaces
                for (org.eclipse.rdf4j.model.Namespace ns : firstPass.getNamespaces()) {
                    result.setNamespace(ns.getPrefix(), ns.getName());
                }
            }

            // check for exactly one root LO
            subjects.removeAll(objects);
            if (subjects.size() != 1) {
                throw new IllegalStateException("Not exactly one root Logistics Object present");
            }
            // the root IRI is the only IRI left in the subjects map
            this.rootIri = subjects.iterator().next();

            // set root LO types
            this.rootTypes = Models.objectIRIs(result.filter(this.rootIri, RDF.TYPE, null));

        }

        private IRI mapBnode(BNode bNode) {
            return iriMap.computeIfAbsent(
                    bNode.stringValue(),
                    s -> idProvider.createInternalIri().getIri()
            );
        }
    }

    /**
     * 模型和元数据容器
     */
    private static class ModelAndMetadata {
        final Model model;
        final IRI rootIri;
        final Set<IRI> rootIriTypes;

        ModelAndMetadata(Model model, IRI rootIri, Set<IRI> rootIriTypes) {
            this.model = model;
            this.rootIri = rootIri;
            this.rootIriTypes = rootIriTypes;
        }
    }
}
