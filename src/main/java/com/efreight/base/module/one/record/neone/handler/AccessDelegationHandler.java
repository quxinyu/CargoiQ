//  Copyright Open Logistics Foundation
//
//  Licensed under the Open Logistics Foundation License 1.3.
//  For details on the licensing terms, see the LICENSE file.
//  SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.handler;

import com.efreight.base.module.one.record.neone.iata.onerecord.API;
import com.efreight.base.module.one.record.neone.model.onerecord.AccessDelegation;
import com.efreight.base.module.one.record.neone.repository.ModelHandler;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class AccessDelegationHandler extends ModelHandler<AccessDelegation> {

    @Override
    public AccessDelegation fromModel(IRI subject, Model model) {
        java.util.Optional<String> description = findObjectString(subject, API.hasDescription, model);
        java.util.Set<AccessDelegation.Permission> permissions = findObjects(subject, API.hasPermission, model).stream()
                .map(AccessDelegation.Permission::from).collect(Collectors.toSet());
        java.util.Set<IRI> isRequestedFor = getObjects(subject, API.isRequestedFor, model);
        java.util.Set<IRI> hasLogisticsObject = getObjects(subject, API.hasLogisticsObject, model);
        java.util.Optional<Boolean> notifyRequestStatusChange = findObjectLiteral(subject, API.notifyRequestStatusChange, model)
                .map(Literal::booleanValue);
        return new AccessDelegation(subject, description, permissions, isRequestedFor,
                hasLogisticsObject, notifyRequestStatusChange);
    }

    @Override
    public Model fromJava(AccessDelegation entity) {
        ModelBuilder builder = new ModelBuilder()
                .subject(entity.iri())
                .add(RDF.TYPE, API.AccessDelegation)
                .add(API.notifyRequestStatusChange, entity.getNotifyRequestStatusChange().orElse(false));
        entity.getDescription().ifPresent(description -> builder.add(API.hasDescription, description));
        entity.getIsRequestedFor().forEach(iri -> builder.add(API.isRequestedFor, iri));
        entity.getHasLogisticsObject().forEach(iri -> builder.add(API.hasLogisticsObject, iri));
        entity.getPermissions().stream().map(AccessDelegation.Permission::iri)
                .forEach(iri -> builder.add(API.hasPermission, iri));
        return builder.build();
    }
}
