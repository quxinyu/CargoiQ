// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.handler;

import com.efreight.base.module.one.record.neone.iata.onerecord.API;
import com.efreight.base.module.one.record.neone.model.onerecord.OperationObject;
import com.efreight.base.module.one.record.neone.repository.ModelHandler;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.springframework.stereotype.Component;

import javax.enterprise.context.ApplicationScoped;

@Component
@ApplicationScoped
public class OperationObjectHandler extends ModelHandler<OperationObject> {

    @Override
    public OperationObject fromModel(IRI subject, Model model) {
        String datatype = getObjectLiteral(subject, API.hasDatatype, model).stringValue();
        String value = getObjectLiteral(subject, API.hasValue, model).stringValue();
        return new OperationObject(subject, datatype, value);
    }

    @Override
    public Model fromJava(OperationObject entity) {
        return new ModelBuilder()
                .subject(entity.iri())
                .add(API.hasDatatype, entity.datatype())
                .add(API.hasValue, entity.value())
                .build();
    }
}
