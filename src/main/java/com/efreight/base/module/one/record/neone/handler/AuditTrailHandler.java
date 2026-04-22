//  Copyright Open Logistics Foundation
//
//  Licensed under the Open Logistics Foundation License 1.3.
//  For details on the licensing terms, see the LICENSE file.
//  SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.handler;

import com.efreight.base.module.one.record.neone.iata.onerecord.API;
import com.efreight.base.module.one.record.neone.model.onerecord.AuditTrail;
import com.efreight.base.module.one.record.neone.model.onerecord.ChangeRequest;
import com.efreight.base.module.one.record.neone.repository.ModelHandler;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.springframework.stereotype.Component;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@ApplicationScoped
public class AuditTrailHandler extends ModelHandler<AuditTrail> {

    private final ChangeRequestHandler crHandler;

    @Inject
    public AuditTrailHandler(ChangeRequestHandler crHandler) {
        this.crHandler = crHandler;
    }

    @Override
    public AuditTrail fromModel(IRI iri, Model model) {
        int latestRevision = getObjectLiteral(iri, API.hasLatestRevision, model).intValue();
        Set<IRI> crIris = findObjects(iri, API.hasChangeRequest, model);
        List<ChangeRequest> changeRequests = crIris.stream().map(crIri -> crHandler.fromModel(crIri, model)).collect(Collectors.toList());
        return new AuditTrail(iri, changeRequests, latestRevision);
    }

    @Override
    public Model fromJava(AuditTrail entity) {
        ModelBuilder builder = new ModelBuilder()
                .subject(entity.iri())
                .add(RDF.TYPE, API.AuditTrail)
                .add(API.hasLatestRevision, entity.latestRevision());

        List<ChangeRequest> crs = entity.changeRequests();
        crs.forEach(cr -> builder.add(API.hasChangeRequest, cr.iri()));

        Model[] crModels = crs.stream().map(crHandler::fromJava).toArray(Model[]::new);
        return merge(builder.build(), crModels);
    }
}
