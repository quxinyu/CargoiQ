// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.repository;

import com.efreight.base.module.one.record.neone.iata.onerecord.NEONE;
import com.efreight.base.module.one.record.neone.model.onerecord.IdProvider;
import com.efreight.base.module.one.record.neone.model.onerecord.Metadata;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.util.Models;
import org.eclipse.rdf4j.query.GraphQuery;
import org.eclipse.rdf4j.query.GraphQueryResult;
import org.eclipse.rdf4j.query.QueryResults;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;

import java.util.Optional;

public abstract class MetadataRepository<T extends Metadata> extends ModelRepository<T> {

    MetadataRepository() {
        // CDI dummy constructor
    }

    public MetadataRepository(Repository repository, ModelHandler<T> modelHandler, IdProvider idProvider) {
        super(repository, modelHandler, idProvider);
    }

    public Optional<T> getMetadataOfSubject(IRI subject, RepositoryConnection connection) {
        GraphQuery query = connection.prepareGraphQuery(Q.getMetadataThatDescribes());
        query.setBinding("desc", subject);
        Optional<T> metadata;
        try (GraphQueryResult result = query.evaluate()) {
            Model resultModel = QueryResults.asModel(result);
            metadata = Models.subjectIRI(resultModel)
                    .map(iri -> modelHandler.fromModel(iri, resultModel));
        }
        return metadata;
    }

    public interface Q {
        static String getMetadataThatDescribes() {
            return String.format(
                    "DESCRIBE ?s\n" +
                            "WHERE {\n" +
                            "  ?s <%s> ?desc\n" +
                            "}", NEONE.describes.stringValue());
        }
    }



}
