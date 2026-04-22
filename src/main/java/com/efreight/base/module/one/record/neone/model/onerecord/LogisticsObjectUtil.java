// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.model.onerecord;

import com.efreight.base.module.one.record.neone.exception.NeoneException;
import com.efreight.base.module.one.record.neone.iata.onerecord.NEONE;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.model.vocabulary.XMLSchema;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.springframework.stereotype.Component;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.time.Instant;

@Component
@ApplicationScoped
public class LogisticsObjectUtil {

    public final static Integer INITIAL_REVISION = 1;

    public IRI generateResourceIri(URI uri) {
        return SimpleValueFactory.getInstance().createIRI(uri.toString());
    }

    public Model addMetadata(Model model, IRI id) {
        Model m = addRevision(model, id, INITIAL_REVISION);
        addCreatedAt(m, id);
        return m;
    }

    private Model addRevision(Model model, IRI id, int version) {
        SimpleValueFactory factory = SimpleValueFactory.getInstance();
        model.add(factory.createStatement(id, NEONE.hasRevision, Values.literal(version)));
        return model;
    }

    private void addCreatedAt(Model model, IRI id) {
        SimpleValueFactory factory = SimpleValueFactory.getInstance();
        model.add(factory.createStatement(id, NEONE.isCreatedAt,
                Values.literal(Instant.now().toString(), XMLSchema.DATETIME)));
    }

    public static String convertToJsonLd(Model logisticObject) {
        try (StringWriter sw = new StringWriter()) {
            Rio.write(logisticObject.getStatements(null, null, null), sw, RDFFormat.JSONLD);
            return sw.toString();
        } catch (IOException e) {
            throw new NeoneException("Cannot convert logistics object model to JsonLD.", e);
        }
    }
}
