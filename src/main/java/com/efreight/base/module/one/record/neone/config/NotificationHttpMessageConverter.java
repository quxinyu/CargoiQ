// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.config;

import com.efreight.base.module.one.record.neone.controller.onerecord.NotificationMessage;
import com.efreight.base.module.one.record.neone.handler.LogisticsObjectHandler;
import com.efreight.base.module.one.record.neone.handler.NotificationHandler;
import com.efreight.base.module.one.record.neone.model.onerecord.IdProvider;
import com.efreight.base.module.one.record.neone.model.onerecord.LogisticsObject;
import com.efreight.base.module.one.record.neone.model.onerecord.Notification;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.util.Models;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * NotificationMessage 对象的自定义 HttpMessageConverter
 * 使用 RDF4J 将 JSON-LD/Turtle 格式转换为 NotificationMessage 对象
 * 参考 NotificationMessageBodyReader (ne-one 项目) 实现
 *
 * @author quxinyu
 * @since 2026-01-15
 */
@Component
public class NotificationHttpMessageConverter extends AbstractHttpMessageConverter<NotificationMessage> {

    public static final String APPLICATION_LD_JSON = "application/ld+json";
    public static final String APPLICATION_X_TURTLE = "application/x-turtle";
    public static final String TEXT_TURTLE = "text/turtle";

    private final NotificationHandler notificationHandler;
    private final LogisticsObjectHandler logisticsObjectHandler;
    private final IdProvider idProvider;

    public NotificationHttpMessageConverter(NotificationHandler notificationHandler,
                                            LogisticsObjectHandler logisticsObjectHandler,
                                            IdProvider idProvider) {
        super(
                new MediaType("application", "ld+json"),
                new MediaType("application", "x-turtle"),
                new MediaType("text", "turtle")
        );
        this.notificationHandler = notificationHandler;
        this.logisticsObjectHandler = logisticsObjectHandler;
        this.idProvider = idProvider;
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return NotificationMessage.class.isAssignableFrom(clazz);
    }

    @Override
    protected NotificationMessage readInternal(Class<? extends NotificationMessage> clazz,
                                               HttpInputMessage inputMessage) throws IOException {
        try {
            // 获取内容类型
            MediaType mediaType = inputMessage.getHeaders().getContentType();
            if (mediaType == null) {
                mediaType = new MediaType("application", "ld+json");
            }

            // 获取 RDF 格式
            RDFFormat rdfFormat = getRdfFormat(mediaType);

            // 解析输入流为 Model
            InputStream inputStream = inputMessage.getBody();
            Model model = Rio.parse(inputStream, rdfFormat);

            // 参考 NotificationMessageBodyReader 实现：
            // 使用 Models.subjectIRI() 查找类型为 Notification 的 IRI
            // 即使没有 @id，也会找到正确的 subject（可能是 BNode）
            Optional<IRI> notificationIri = Models.subjectIRI(model.filter(null, RDF.TYPE,
                    com.efreight.base.module.one.record.neone.iata.onerecord.API.Notification));

            // 查找 LogisticsObject 的 IRI 引用
            Optional<IRI> logisticsObjectIri = Models.objectIRI(
                    model.filter(null, com.efreight.base.module.one.record.neone.iata.onerecord.API.hasLogisticsObject, null));

            // 使用 NotificationHandler 从 Model 中提取 Notification
            // 允许 notificationIri 为 null（当 Notification 没有 @id 时）
            Notification notification = notificationHandler.fromModel(notificationIri.orElse(null), model);

            // 验证：至少需要 notification 或 logisticsObject 之一
            if (!notificationIri.isPresent() && !logisticsObjectIri.isPresent()) {
                throw new HttpMessageNotReadableException(
                        "Invalid Notification Message: Neither IRI of notification nor IRI of logistics object specified.");
            }

            // 创建 NotificationMessage
            NotificationMessage notificationMessage = new NotificationMessage()
                    .withNotification(notification);

            // 如果存在 LogisticsObject，使用 LogisticsObjectHandler 正确解析
            if (logisticsObjectIri.isPresent()) {
                LogisticsObject logisticsObject = logisticsObjectHandler.fromModel(logisticsObjectIri.get(), model);
                notificationMessage.withLogisticsObject(logisticsObject);
            }

            return notificationMessage;

        } catch (HttpMessageNotReadableException e) {
            // 重新抛出已经包装的异常
            throw e;
        } catch (NoSuchElementException e) {
            throw new HttpMessageNotReadableException(
                    "Invalid Notification Message structure: Missing required property (api:hasEventType).", e);
        } catch (IOException e) {
            throw new HttpMessageNotReadableException(
                    "Invalid Notification Message structure: " + e.getMessage(), e
            );
        } catch (Exception e) {
            throw new HttpMessageNotReadableException(
                    "Failed to parse NotificationMessage from JSON-LD/Turtle: " + e.getMessage(), e
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
                return org.eclipse.rdf4j.rio.RDFFormat.JSONLD;
            case APPLICATION_X_TURTLE:
            case TEXT_TURTLE:
                return org.eclipse.rdf4j.rio.RDFFormat.TURTLE;
            default:
                throw new IllegalArgumentException("Unsupported media type: " + mimeType);
        }
    }

    @Override
    protected void writeInternal(NotificationMessage notificationMessage,
                                 HttpOutputMessage outputMessage) throws IOException {
        try {
            // 1. 将 Notification 对象转换为 Model
            Model notificationModel = notificationHandler.fromJava(notificationMessage.getNotification());

            // 2. 如果有 LogisticsObject，将其 Model 合并
            if (notificationMessage.getLogisticsObject().isPresent()) {
                Model logisticsObjectModel = logisticsObjectHandler.fromJava(notificationMessage.getLogisticsObject().get());
                notificationModel.addAll(logisticsObjectModel);
            }

            // 3. 获取响应的媒体类型
            MediaType mediaType = outputMessage.getHeaders().getContentType();
            if (mediaType == null) {
                mediaType = new MediaType("application", "ld+json");
            }

            // 4. 获取对应的 RDFFormat
            RDFFormat rdfFormat = getRdfFormat(mediaType);

            // 5. 写入响应流
            Rio.write(notificationModel, outputMessage.getBody(), rdfFormat);

        } catch (Exception e) {
            throw new IOException("Failed to serialize NotificationMessage: " + e.getMessage(), e);
        }
    }
}
