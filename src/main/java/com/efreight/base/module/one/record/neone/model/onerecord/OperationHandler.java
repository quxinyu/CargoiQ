// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.model.onerecord;

import com.efreight.base.module.one.record.neone.handler.OperationObjectHandler;
import com.efreight.base.module.one.record.neone.iata.onerecord.API;
import com.efreight.base.module.one.record.neone.repository.ModelHandler;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.util.Values;
import org.springframework.stereotype.Component;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Component
@ApplicationScoped
public class OperationHandler extends ModelHandler<Operation> {

    private final OperationObjectHandler operationObjectHandler;

    @Inject
    public OperationHandler(OperationObjectHandler operationObjectHandler) {
        this.operationObjectHandler = operationObjectHandler;
    }

    @Override
    public Operation fromModel(IRI subject, Model model) {
        IRI operationObjectSubject = getObject(subject, API.o, model);
        OperationObject operationObject = operationObjectHandler.fromModel(operationObjectSubject, model);

        PatchOperation patchOperation = PatchOperation.from(getObject(subject, API.op, model));
        IRI predicate = Values.iri(getObjectLiteral(subject, API.p, model).stringValue());
        String targetSubject = getObjectLiteral(subject, API.s, model).stringValue();

        return new Operation(subject, operationObject, patchOperation, predicate, targetSubject);
    }

    @Override
    public Model fromJava(Operation entity) {
        Model operationObjectModel = operationObjectHandler.fromJava(entity.o());
        Model model = new ModelBuilder()
                .subject(entity.iri())
                .add(API.op, entity.op().iri())
                .add(API.p, entity.p().stringValue())
                .add(API.s, entity.s())
                .add(API.o, entity.o().iri())
                .build();


        return merge(operationObjectModel, model);
    }
}
