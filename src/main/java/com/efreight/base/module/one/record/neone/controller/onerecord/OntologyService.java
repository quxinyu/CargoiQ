// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.controller.onerecord;

import com.efreight.base.module.one.record.neone.exception.NeoneStartupException;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.StreamDocumentSource;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.util.SimpleIRIMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Objects;

public abstract class OntologyService {

    private static final Logger log = LoggerFactory.getLogger(OntologyService.class);

    private final OWLOntology ontology;

    private final OWLDataFactory dataFactory;

    private final OWLReasoner reasoner;

    //
    public OntologyService(String ontologyFile) {
        log.info("Loading ontology [{}]", ontologyFile);
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        IRI coreCodeListsIri = IRI.create("https://onerecord.iata.org/ns/coreCodeLists/1.0.0");
        OWLOntologyLoaderConfiguration cfg = new OWLOntologyLoaderConfiguration()
                .addIgnoredImport(coreCodeListsIri);
        manager.setOntologyLoaderConfiguration(cfg);
        try {
            URL ontologyUrl = Objects.requireNonNull(
                    OntologyService.class.getClassLoader().getResource(ontologyFile)
            );
            URL coreCodeListsUrl = Objects.requireNonNull(
                    OntologyService.class.getClassLoader().getResource("IATA-1R-CCL-Ontology.ttl")
            );
            manager.addIRIMapper(new SimpleIRIMapper(coreCodeListsIri, IRI.create(coreCodeListsUrl)));

            // Read ontology file into byte array to keep it in memory
            // This prevents "Stream closed" errors when reasoner needs to re-read the ontology
            java.io.InputStream inputStream = Objects.requireNonNull(
                    OntologyService.class.getClassLoader().getResourceAsStream(ontologyFile)
            );
            byte[] ontologyBytes = readAllBytes(inputStream);
            inputStream.close();

            java.io.ByteArrayInputStream byteArrayInputStream = new java.io.ByteArrayInputStream(ontologyBytes);

            this.ontology = manager.loadOntologyFromOntologyDocument(
                    new StreamDocumentSource(byteArrayInputStream, IRI.create(ontologyUrl))
            );
            this.dataFactory = manager.getOWLDataFactory();
            OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
            this.reasoner = reasonerFactory.createReasoner(ontology);
            reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);

        } catch (OWLOntologyCreationException | java.io.IOException e) {
            throw new NeoneStartupException(e);
        }
    }

    /**
     * Read all bytes from an input stream (Java 8 compatible version)
     */
    private static byte[] readAllBytes(java.io.InputStream inputStream) throws java.io.IOException {
        java.io.ByteArrayOutputStream buffer = new java.io.ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[16384];
        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        return buffer.toByteArray();
    }

    protected OWLOntology getOntology() {
        return ontology;
    }

    protected OWLDataFactory getDataFactory() {
        return dataFactory;
    }

    protected OWLReasoner getReasoner() {
        return reasoner;
    }
}
