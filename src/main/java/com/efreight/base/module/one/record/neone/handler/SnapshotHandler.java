//  Copyright Open Logistics Foundation
//
//  Licensed under the Open Logistics Foundation License 1.3.
//  For details on the licensing terms, see the LICENSE file.
//  SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.handler;


import com.efreight.base.module.one.record.neone.iata.onerecord.NEONE;
import com.efreight.base.module.one.record.neone.model.onerecord.Snapshot;
import com.efreight.base.module.one.record.neone.repository.ModelHandler;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.XMLSchema;
import org.springframework.stereotype.Component;

import javax.enterprise.context.ApplicationScoped;
import java.time.Instant;

@Component
@ApplicationScoped
public class SnapshotHandler extends ModelHandler<Snapshot> {

    @Override
    public Snapshot fromModel(IRI iri, Model model) {
        IRI loIri = getObject(iri, NEONE.referencesLogisticsObject, model);
        String payload = getObjectString(iri, NEONE.payload, model);
        Instant at = Instant.parse(getObjectString(iri, NEONE.isCreatedAt, model));
        Integer revision = getObjectLiteral(iri, NEONE.hasRevision, model).intValue();

        return new Snapshot(iri, loIri, payload, at, revision);
    }

    @Override
    public Model fromJava(Snapshot entity) {
        Literal ts = Values.literal(entity.at().toString(), XMLSchema.DATETIME);
        return new ModelBuilder()
            .subject(entity.iri())
            .add(RDF.TYPE, NEONE.Snapshot)
            .add(NEONE.referencesLogisticsObject, entity.loIri())
            .add(NEONE.payload, entity.payload())
            .add(NEONE.isCreatedAt, ts)
            .add(NEONE.hasRevision, entity.revision())
            .build();
    }
}
