// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.config;

import com.efreight.base.module.one.record.neone.controller.onerecord.CargoOntologyService;
import com.efreight.base.module.one.record.neone.controller.onerecord.LogisticsObjectsController;
import com.efreight.base.module.one.record.neone.model.onerecord.LogisticsObject;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Custom HttpMessageConverter for GetLogisticsObjectResponse
 * Converts LogisticsObject to JSON-LD/Turtle formats using RDF4J
 *
 * @author quxinyu
 * @since 2026-01-13
 */
@Component
public class LogisticsObjectHttpMessageConverter extends AbstractHttpMessageConverter<LogisticsObjectsController.GetLogisticsObjectResponse> {

    public static final String APPLICATION_LD_JSON = "application/ld+json";
    public static final String APPLICATION_X_TURTLE = "application/x-turtle";
    public static final String TEXT_TURTLE = "text/turtle";

    private final CargoOntologyService cargoOntologyService;

    public LogisticsObjectHttpMessageConverter(CargoOntologyService cargoOntologyService) {
        super(
                new MediaType("application", "ld+json"),
                new MediaType("application", "x-turtle"),
                new MediaType("text", "turtle")
        );
        this.cargoOntologyService = cargoOntologyService;
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return LogisticsObjectsController.GetLogisticsObjectResponse.class.isAssignableFrom(clazz);
    }

    @Override
    protected LogisticsObjectsController.GetLogisticsObjectResponse readInternal(
            Class<? extends LogisticsObjectsController.GetLogisticsObjectResponse> clazz,
            HttpInputMessage inputMessage) throws IOException {
        // Reading is not supported for this response type
        throw new UnsupportedOperationException(
                "Reading GetLogisticsObjectResponse is not supported."
        );
    }

    @Override
    protected void writeInternal(LogisticsObjectsController.GetLogisticsObjectResponse response,
                                 HttpOutputMessage outputMessage) throws IOException {
        try {
            // Get content type
            MediaType mediaType = outputMessage.getHeaders().getContentType();
            if (mediaType == null) {
                mediaType = new MediaType("application", "ld+json");
            }

            // Determine RDF format based on media type
            RDFFormat rdfFormat = getRdfFormat(mediaType);

            // Set headers
            response.getHeaders().forEach((key, values) -> {
                if (values != null) {
                    values.forEach(value -> {
                        if (value != null) {
                            outputMessage.getHeaders().add(key, value.toString());
                        }
                    });
                }
            });

            // Get logistics object and model
            LogisticsObject logisticsObject = response.getLogisticsObject();
            if (logisticsObject == null) {
                throw new HttpMessageNotWritableException("LogisticsObject is null");
            }

            Model model = logisticsObject.model();

            // Write model to output stream using Rio
            OutputStream outputStream = outputMessage.getBody();
            Rio.write(model, outputStream, rdfFormat);

        } catch (Exception e) {
            throw new HttpMessageNotWritableException(
                    "Failed to write LogisticsObject to JSON-LD/Turtle: " + e.getMessage(),
                    e
            );
        }
    }

    /**
     * Determine RDF format based on media type
     */
    private RDFFormat getRdfFormat(MediaType mediaType) {
        String subtype = mediaType.getSubtype();

        if ("ld+json".equals(subtype)) {
            return RDFFormat.JSONLD;
        } else if ("x-turtle".equals(subtype) || "turtle".equals(subtype)) {
            return RDFFormat.TURTLE;
        }

        // Default to JSON-LD
        return RDFFormat.JSONLD;
    }
}
