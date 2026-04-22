//  Copyright Open Logistics Foundation
//
//  Licensed under the Open Logistics Foundation License 1.3.
//  For details on the licensing terms, see the LICENSE file.
//  SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.handler;

import com.efreight.base.module.one.record.neone.iata.onerecord.ACL;
import com.efreight.base.module.one.record.neone.model.onerecord.AclAuthorization;
import com.efreight.base.module.one.record.neone.repository.ModelHandler;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.springframework.stereotype.Component;

import javax.enterprise.context.ApplicationScoped;
import java.util.Set;

@Component
@ApplicationScoped
public class AclAuthorizationHandler extends ModelHandler<AclAuthorization> {

    @Override
    public AclAuthorization fromModel(IRI subject, Model model) {
        IRI accessTo = getObject(subject, ACL.accessTo, model);
        IRI agent = getObject(subject, ACL.agent, model);
        Set<IRI> modes = getObjects(subject, ACL.mode, model);
        return new AclAuthorization(subject, accessTo, agent, modes);
    }

    @Override
    public Model fromJava(AclAuthorization entity) {
        ModelBuilder builder = new ModelBuilder()
            .subject(entity.iri())
            .add(RDF.TYPE, ACL.Authorization)
            .add(ACL.accessTo, entity.accessTo())
            .add(ACL.agent, entity.agent());
        entity.modes().forEach(p -> builder.add(ACL.mode, p));
        return builder.build();
    }
}
