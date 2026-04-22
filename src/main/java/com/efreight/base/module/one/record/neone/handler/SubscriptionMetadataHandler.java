//  Copyright Open Logistics Foundation
//
//  Licensed under the Open Logistics Foundation License 1.3.
//  For details on the licensing terms, see the LICENSE file.
//  SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.handler;

import com.efreight.base.module.one.record.neone.exception.NeoneException;
import com.efreight.base.module.one.record.neone.iata.onerecord.NEONE;
import com.efreight.base.module.one.record.neone.model.onerecord.SubscriptionMetadata;
import com.efreight.base.module.one.record.neone.repository.ModelHandler;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.springframework.stereotype.Component;

import javax.enterprise.context.ApplicationScoped;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

@Component
@ApplicationScoped
public class SubscriptionMetadataHandler extends ModelHandler<SubscriptionMetadata> {

    @Override
    public SubscriptionMetadata fromModel(IRI subject, Model model) {
        try {
            IRI describes = getObject(subject, NEONE.describes, model);
            URL callbackUrl = new URI(getObjectString(subject, NEONE.callbackUrl, model)).toURL();
            return new SubscriptionMetadata(subject, describes, callbackUrl);
        } catch (MalformedURLException | URISyntaxException e) {
            throw new NeoneException("Unable to deserialize Subscription metadata model", e);
        }
    }

    @Override
    public Model fromJava(SubscriptionMetadata entity) {
        ModelBuilder modelBuilder = new ModelBuilder()
                .subject(entity.iri())
                .add(RDF.TYPE, NEONE.SubscriptionMetadata)
                .add(NEONE.describes, entity.describes())
                .add(NEONE.callbackUrl, entity.callbackUrl());
        return modelBuilder.build();
    }
}
