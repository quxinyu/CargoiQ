// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.handler;

import com.efreight.base.module.one.record.neone.iata.onerecord.NEONE;
import com.efreight.base.module.one.record.neone.model.onerecord.LogisticsObjectMetadata;
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
import java.util.Optional;

@Component
@ApplicationScoped
public class LogisticsObjectMetadataHandler extends ModelHandler<LogisticsObjectMetadata> {

    @Override
    public LogisticsObjectMetadata fromModel(IRI subject, Model model) {
        IRI describes = getObject(subject, NEONE.describes, model);
        Integer revision = getObjectLiteral(subject, NEONE.hasRevision, model).intValue();
        Instant createdAt = getObjectLiteral(subject, NEONE.isCreatedAt, model)
            .calendarValue().toGregorianCalendar().toInstant();
        Optional<Boolean> hasPredefinedIri = findObjectLiteral(subject, NEONE.hasPredefinedIri, model)
            .map(Literal::booleanValue);
        return new LogisticsObjectMetadata(subject, describes, revision, createdAt, hasPredefinedIri);
    }

    @Override
    public Model fromJava(LogisticsObjectMetadata entity) {
        ModelBuilder modelBuilder = new ModelBuilder()
            .subject(entity.getIri())
            .add(RDF.TYPE, NEONE.LogisticsObjectMetadata)
            .add(NEONE.describes, entity.getDescribes())
            .add(NEONE.hasRevision, Values.literal(entity.getRevision()))
            .add(NEONE.isCreatedAt, Values.literal(entity.getCreatedAt().toString(), XMLSchema.DATETIME));
        entity.getHasPredefinedIri().ifPresent(b -> modelBuilder.add(NEONE.hasPredefinedIri, Values.literal(b)));
        return modelBuilder.build();
    }
}
