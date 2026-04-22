// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.handler;

import com.efreight.base.module.one.record.neone.iata.onerecord.API;
import com.efreight.base.module.one.record.neone.model.onerecord.Change;
import com.efreight.base.module.one.record.neone.model.onerecord.Operation;
import com.efreight.base.module.one.record.neone.model.onerecord.OperationHandler;
import com.efreight.base.module.one.record.neone.repository.ModelHandler;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.springframework.stereotype.Component;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@ApplicationScoped
public class ChangeHandler extends ModelHandler<Change> {

    private final OperationHandler operationHandler;

    @Inject
    public ChangeHandler(OperationHandler operationHandler) {
        this.operationHandler = operationHandler;
    }

    @Override
    public Change fromModel(IRI subject, Model model) {
        int revision = getObjectLiteral(subject, API.hasRevision, model).intValue();
        IRI logisticsObject = getObject(subject, API.hasLogisticsObject, model);
        Optional<String> description = findObjectString(subject, API.hasDescription, model);
        Set<IRI> operationSubjects = getObjects(subject, API.hasOperation, model);
        Set<Operation> operations = operationSubjects.stream()
                .map(iri -> operationHandler.fromModel(iri, model))
                .collect(Collectors.toSet());
        Optional<Boolean> notifyRequestStatusChange = findObjectLiteral(subject, API.notifyRequestStatusChange, model)
                .map(Literal::booleanValue);

        return new Change(subject, description, operations, revision, logisticsObject, notifyRequestStatusChange);
    }

    @Override
    public Model fromJava(Change entity) {
        ModelBuilder modelBuilder = new ModelBuilder()
                .subject(entity.iri())
                .add(RDF.TYPE, API.Change)
                .add(API.hasRevision, entity.revision())
                .add(API.hasLogisticsObject, entity.hasLogisticsObject())
                .add(API.notifyRequestStatusChange, entity.notifyRequestStatusChange().orElse(false));
        entity.operations().forEach(operation -> modelBuilder.add(API.hasOperation, operation.iri()));
        entity.description().ifPresent(description -> modelBuilder.add(API.hasDescription, description));
        Model[] operationModels = entity.operations().stream()
                .map(operationHandler::fromJava)
                .toArray(Model[]::new);

        return merge(modelBuilder.build(), operationModels);
    }
}
