// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.controller.onerecord;

import com.efreight.base.module.one.record.neone.model.onerecord.IdProvider;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.util.Models;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.WriterConfig;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Handles conversion from and to RDF4J's {@link Model}.
 * Migrated from JAX-RS MessageBodyReader/Writer to Spring HttpMessageConverter.
 */
@Component
public class ModelBodyHandler extends AbstractHttpMessageConverter<Model> {

    private final IdProvider idProvider;
    private final WriterConfig writerConfig;
    private final CargoOntologyService cargoOntologyService;

    public ModelBodyHandler(IdProvider idProvider, WriterConfig writerConfig,
                            CargoOntologyService cargoOntologyService) {
        super(
                new MediaType("application", "ld+json"),
                new MediaType("application", "x-turtle"),
                new MediaType("text", "turtle")
        );
        this.idProvider = idProvider;
        this.writerConfig = writerConfig;
        this.cargoOntologyService = cargoOntologyService;
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return Model.class.isAssignableFrom(clazz);
    }

    @Override
    protected Model readInternal(Class<? extends Model> clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        MediaType mediaType = inputMessage.getHeaders().getContentType();
        if (mediaType == null) {
            mediaType = new MediaType("application", "ld+json");
        }
        try {
            return Rio.parse(inputMessage.getBody(), getRdfFormat(mediaType));
        } catch (Exception e) {
            throw new HttpMessageNotReadableException(
                    "Failed to parse Model: " + e.getMessage()
            );
        }
    }

    @Override
    protected void writeInternal(Model model, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        MediaType mediaType = outputMessage.getHeaders().getContentType();
        if (mediaType == null) {
            mediaType = new MediaType("application", "ld+json");
        }

        // HttpHeaders implements MultiValueMap, so we can pass it directly
        try {
            writeRdfResponse(model, mediaType, outputMessage.getHeaders(), outputMessage.getBody());
        } catch (Exception e) {
            throw new HttpMessageNotWritableException(
                    "Failed to write Model: " + e.getMessage(),
                    e
            );
        }
    }

    // Delegate to BodyHandlerBase methods
    private org.eclipse.rdf4j.rio.RDFFormat getRdfFormat(MediaType mediaType) {
        String mimeType = mediaType.getType() + "/" + mediaType.getSubtype();
        switch (mimeType) {
            case BodyHandlerBase.APPLICATION_LD_JSON:
                return org.eclipse.rdf4j.rio.RDFFormat.JSONLD;
            case BodyHandlerBase.APPLICATION_X_TURTLE:
            case BodyHandlerBase.TEXT_TURTLE:
                return org.eclipse.rdf4j.rio.RDFFormat.TURTLE;
            default:
                throw new HttpMessageNotReadableException(
                        "Unsupported media type [" + mediaType + "]"
                );
        }
    }

    private void writeRdfResponse(Model model, MediaType mediaType,
                                  org.springframework.http.HttpHeaders httpHeaders,
                                  OutputStream entityStream) {
        // extract rdf type
        java.util.Set<org.eclipse.rdf4j.model.IRI> rootIris = getRootIris(model);
        if (rootIris.size() == 1) {
            Models.objectIRIs(model.filter(rootIris.iterator().next(),
                            org.eclipse.rdf4j.model.vocabulary.RDF.TYPE, null))
                    .forEach(iri -> httpHeaders.add(BodyHandlerBase.TYPE_HEADER, iri.stringValue()));
        }
        if (rootNamespace() != null) {
            model.setNamespace("", rootNamespace());
        }
        additionalNamespaces().forEach(model::setNamespace);
        Rio.write(model, entityStream, getRdfFormat(mediaType), writerConfig);
    }

    private java.util.Set<org.eclipse.rdf4j.model.IRI> getRootIris(org.eclipse.rdf4j.model.Model model) {
        // set of all subject IRIs
        java.util.Set<org.eclipse.rdf4j.model.IRI> subjectIris = Models.subjectIRIs(model);
        // set of all object IRIs
        java.util.Set<org.eclipse.rdf4j.model.IRI> objectIris = Models.objectIRIs(model);

        // subtract set of objects from set of subjects
        subjectIris.removeAll(objectIris);

        return subjectIris;
    }

    private String rootNamespace() {
        return "";
    }

    private java.util.List<org.eclipse.rdf4j.model.Namespace> additionalNamespaces() {
        return new java.util.ArrayList<>();
    }
}
