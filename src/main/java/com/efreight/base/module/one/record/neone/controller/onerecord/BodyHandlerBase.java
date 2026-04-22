// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.controller.onerecord;

import com.efreight.base.module.one.record.neone.exception.InvalidApiRequestException;
import com.efreight.base.module.one.record.neone.iata.onerecord.NEONE;
import com.efreight.base.module.one.record.neone.model.onerecord.IdProvider;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.impl.DynamicModelFactory;
import org.eclipse.rdf4j.model.util.Models;
import org.eclipse.rdf4j.model.util.Statements;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.rio.*;
import org.eclipse.rdf4j.rio.helpers.AbstractRDFHandler;

import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

public abstract class BodyHandlerBase {

    public static final String APPLICATION_LD_JSON = "application/ld+json";
    public static final String APPLICATION_X_TURTLE = "application/x-turtle";
    public static final String TEXT_TURTLE = "text/turtle";

    public static final String TYPE_HEADER = "Type";

    private final WriterConfig writerConfig;

    private final CargoOntologyService cargoOntologyService;

    public BodyHandlerBase(WriterConfig writerConfig, CargoOntologyService cargoOntologyService) {
        this.writerConfig = writerConfig;
        this.cargoOntologyService = cargoOntologyService;
    }

    protected String rootNamespace() {
        return null;
    }

    protected List<Namespace> additionalNamespaces() {
        return new ArrayList<>();
    }

    public RDFFormat getRdfFormat(MediaType mediaType) {
        String mimeType = mediaType.getType() + "/" + mediaType.getSubtype();
        switch (mimeType) {
            case APPLICATION_LD_JSON:
                return RDFFormat.JSONLD;
            case APPLICATION_X_TURTLE:
            case TEXT_TURTLE:
                return RDFFormat.TURTLE;
            default:
                throw new UnsupportedMediaTypeStatusException("Unsupported media type [" + mediaType + "]");
        }
    }

    public Set<IRI> getRootIris(Model model) {
        // set of all subject IRIs
        Set<IRI> subjectIris = Models.subjectIRIs(model);
        // set of all object IRIs
        Set<IRI> objectIris = Models.objectIRIs(model);

        // subtract set of objects from set of subjects
        subjectIris.removeAll(objectIris);

        return subjectIris;
    }

    public IRI getRootIri(Model model) {
        Set<IRI> subjectIris = getRootIris(model);
        if (subjectIris.size() != 1) {
            throw new InvalidApiRequestException("Invalid number of IRIs found, expected 1 got: " + subjectIris.stream()
                    .map(IRI::stringValue)
                    .collect(Collectors.joining(",", "[", "]"))
            );
        }
        return subjectIris.iterator().next();
    }

    /**
     * Parses an RDF stream into a {@link Model}, replacing every {@link BNode} with an internal {@link IRI}.
     * <p>Every blank node in the dataset is replaced by an internal IRI, links are left intact.
     *
     * @param entityStream the stream to parse
     * @param mediaType    the media type of the RDF dataset
     * @param idProvider   the {@link IdProvider} for creating the internal IRI's
     * @return the processed model with every blank node replaced by an internal IRI
     * @throws IOException when reading the stream fails with an I/O error
     */
    public Model processModel(InputStream entityStream,
                              MediaType mediaType,
                              IdProvider idProvider) throws IOException {

        return processModelAndMetadata(entityStream, mediaType, idProvider).model();
    }

    /**
     * Parses an RDF stream into a {@link Model}, replacing every {@link BNode} with an internal {@link IRI}.
     * <p>Every blank node in the dataset is replaced by an internal IRI, links are left intact.
     *
     * @param entityStream the stream to parse
     * @param mediaType    the media type of the RDF dataset
     * @param idProvider   the {@link IdProvider} for creating the internal IRI's
     * @return the processed model with every blank node replaced by an internal IRI, the root IRI and the root IRIs
     * defined types
     * @throws IOException when reading the stream fails with an I/O error
     */
    public ModelAndMetadata processModelAndMetadata(InputStream entityStream,
                                                    MediaType mediaType,
                                                    IdProvider idProvider) throws IOException {

        RDFParser parser = Rio.createParser(getRdfFormat(mediaType));
        BnodeReplacingStatementHandler statementHandler =
                new BnodeReplacingStatementHandler(idProvider, cargoOntologyService);
        parser.setRDFHandler(statementHandler);
        parser.parse(entityStream);

        return new ModelAndMetadata(statementHandler.getModel(),
                statementHandler.getRootIri(), statementHandler.getRootTypes());
    }

    public void writeRdfResponse(Model model, MediaType mediaType,
                                 MultiValueMap<String, Object> httpHeaders, OutputStream entityStream) {

        // extract rdf type
        Set<IRI> rootIris = getRootIris(model);
        if (rootIris.size() == 1) {
            Models.objectIRIs(model.filter(rootIris.iterator().next(), RDF.TYPE, null))
                    .forEach(iri -> httpHeaders.add(TYPE_HEADER, iri.stringValue()));
        }
        if (rootNamespace() != null) {
            model.setNamespace("", rootNamespace());
        }
        additionalNamespaces().forEach(model::setNamespace);
        Rio.write(model, entityStream, getRdfFormat(mediaType), writerConfig);
    }

    public CargoOntologyService getCargoOntologyService() {
        return cargoOntologyService;
    }

    /**
     * {@link org.eclipse.rdf4j.rio.RDFHandler} that replaces every blank node given in an RDF dataset.
     */
    private static final class BnodeReplacingStatementHandler extends AbstractRDFHandler {

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
            iris.forEach(iri -> {
                // get RDF.TYPE
                String[] types = Models.objectIRIs(firstPass.filter(iri, RDF.TYPE, null)).stream()
                        .map(IRI::stringValue)
                        .toArray(String[]::new);
                if (cargoOntologyService.isLogisticsObject(types)) {
                    if (idProvider.parse(iri).isInternal()) {
                        // add the newly created IRI to the mapping table
                        mappedIris.put(iri, idProvider.createUniqueLoUri().getIri());
                    } else {
                        // mark the subject as a subject with a predefined IRI
                        firstPass.add(iri, NEONE.hasPredefinedIri, Values.literal(true));
                    }
                }

            });
            final Set<IRI> subjects = new HashSet<>();
            final Set<IRI> objects = new HashSet<>();
            firstPass.forEach(statement -> {
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
                firstPass.getNamespaces().forEach(result::setNamespace);
            });

            // check for exactly one root LO
            subjects.removeAll(objects);
            if (subjects.size() != 1) {
                throw new InvalidApiRequestException("Not exactly one root Logistics Object present");
            }
            // the root IRI ist the only IRI left in the subjects map
            this.rootIri = subjects.stream().findFirst().get();

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

    public static class ModelAndMetadata {
        private final Model model;
        private final IRI rootIri;
        private final Set<IRI> rootIriTypes;

        public ModelAndMetadata(Model model, IRI rootIri, Set<IRI> rootIriTypes) {
            this.model = model;
            this.rootIri = rootIri;
            this.rootIriTypes = rootIriTypes;
        }

        public Model model() {
            return model;
        }

        public IRI rootIri() {
            return rootIri;
        }

        public Set<IRI> rootIriTypes() {
            return rootIriTypes;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ModelAndMetadata that = (ModelAndMetadata) o;
            return Objects.equals(model, that.model) &&
                    Objects.equals(rootIri, that.rootIri) &&
                    Objects.equals(rootIriTypes, that.rootIriTypes);
        }

        @Override
        public int hashCode() {
            return Objects.hash(model, rootIri, rootIriTypes);
        }

        @Override
        public String toString() {
            return "ModelAndMetadata{" +
                    "model=" + model +
                    ", rootIri=" + rootIri +
                    ", rootIriTypes=" + rootIriTypes +
                    '}';
        }
    }

}
