// Copyright Open Logistics Foundation
//
// Licensed under Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.config;

import com.efreight.base.module.one.record.neone.handler.SubscriptionHandler;
import com.efreight.base.module.one.record.neone.model.onerecord.IdProvider;
import com.efreight.base.module.one.record.neone.model.onerecord.Subscription;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
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

/**
 * Subscription 对象的自定义 HttpMessageConverter
 * 使用 RDF4J 将 JSON-LD/Turtle 格式转换为 Subscription 对象
 * 参考 SubscriptionBodyReaderWriter 实现
 *
 * @author quxinyu
 * @since 2026-01-14
 */
@Component
public class SubscriptionHttpMessageConverter extends AbstractHttpMessageConverter<Subscription> {

    public static final String APPLICATION_LD_JSON = "application/ld+json";
    public static final String APPLICATION_X_TURTLE = "application/x-turtle";
    public static final String TEXT_TURTLE = "text/turtle";

    private final SubscriptionHandler subscriptionHandler;
    private final IdProvider idProvider;

    public SubscriptionHttpMessageConverter(SubscriptionHandler subscriptionHandler,
                                            IdProvider idProvider) {
        super(
                new MediaType("application", "ld+json"),
                new MediaType("application", "x-turtle"),
                new MediaType("text", "turtle")
        );
        this.subscriptionHandler = subscriptionHandler;
        this.idProvider = idProvider;
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return Subscription.class.isAssignableFrom(clazz);
    }

    @Override
    protected Subscription readInternal(Class<? extends Subscription> clazz,
                                        HttpInputMessage inputMessage) throws IOException {
        try {
            // 获取内容类型
            MediaType mediaType = inputMessage.getHeaders().getContentType();
            if (mediaType == null) {
                mediaType = new MediaType("application", "ld+json");
            }

            // 获取 RDF 格式
            RDFFormat rdfFormat = getRdfFormat(mediaType);

            // 直接解析输入流为 Model
            InputStream inputStream = inputMessage.getBody();
            Model model = Rio.parse(inputStream, rdfFormat);
            IRI subscriptionIri = idProvider.createUniqueSubscriptionUri().getIri();

            // 使用 SubscriptionHandler 从 Model 转换为 Subscription 对象
            return subscriptionHandler.fromModel(subscriptionIri, model);
        } catch (IOException | NoSuchElementException e) {
            throw new HttpMessageNotReadableException(
                    "无效的 Subscription 结构: " + e.getMessage(), inputMessage
            );
        } catch (Exception e) {
            throw new HttpMessageNotReadableException(
                    "从 JSON-LD/Turtle 解析 Subscription 失败: " + e.getMessage(), inputMessage
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
                throw new IllegalArgumentException("不支持的媒体类型: " + mimeType);
        }
    }

    @Override
    protected void writeInternal(Subscription subscription,
                                 HttpOutputMessage outputMessage) throws IOException {
        try {
            // 1. 将 Subscription 对象转换为 Model
            Model model = subscriptionHandler.fromJava(subscription);

            // 2. 获取响应的媒体类型
            MediaType mediaType = outputMessage.getHeaders().getContentType();
            if (mediaType == null) {
                mediaType = new MediaType("application", "ld+json");
            }

            // 3. 获取对应的 RDFFormat
            RDFFormat rdfFormat = getRdfFormat(mediaType);

            // 4. 写入响应流
            Rio.write(model, outputMessage.getBody(), rdfFormat);
        } catch (Exception e) {
            throw new IOException("序列化 Subscription 失败: " + e.getMessage(), e);
        }
    }
}
