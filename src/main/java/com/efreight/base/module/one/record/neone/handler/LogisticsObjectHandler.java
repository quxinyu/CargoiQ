// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.handler;

import com.efreight.base.module.one.record.neone.iata.onerecord.CARGO;
import com.efreight.base.module.one.record.neone.model.onerecord.LogisticsObject;
import com.efreight.base.module.one.record.neone.repository.ModelHandler;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.DynamicModelFactory;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.springframework.stereotype.Component;

import javax.enterprise.context.ApplicationScoped;

@Component
@ApplicationScoped
public class LogisticsObjectHandler extends ModelHandler<LogisticsObject> {

    @Override
    public LogisticsObject fromModel(IRI subject, Model model) {
        return new LogisticsObject(subject, model);
    }

    @Override
    public Model fromJava(LogisticsObject entity) {
        // add LogisticsObject type to model
        Model model;
        if (!entity.model().contains(entity.iri(), RDF.TYPE, CARGO.LogisticsObject)) {
            model = new DynamicModelFactory().createEmptyModel();
            model.addAll(entity.model());
            model.add(entity.iri(), RDF.TYPE, CARGO.LogisticsObject);
        } else {
            model = entity.model();
        }
        return model;
    }
}
