//  Copyright Open Logistics Foundation
//
//  Licensed under the Open Logistics Foundation License 1.3.
//  For details on the licensing terms, see the LICENSE file.
//  SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.handler;

import com.efreight.base.module.one.record.neone.exception.NeoneException;
import com.efreight.base.module.one.record.neone.iata.onerecord.API;
import com.efreight.base.module.one.record.neone.iata.onerecord.NEONE;
import com.efreight.base.module.one.record.neone.model.onerecord.NotificationMetadata;
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
public class NotificationMetadataHandler extends ModelHandler<NotificationMetadata> {

    @Override
    public NotificationMetadata fromModel(IRI subject, Model model) {
        try {
            IRI notificationIri = getObject(subject, NEONE.describes, model);
            URL callbackUrl = new URI(getObjectString(subject, NEONE.callbackUrl, model)).toURL();
            Boolean includeLoBody = findObjectString(subject, NEONE.includeLogisticsObject, model).map(Boolean::valueOf).orElse(false);
            IRI eventIri = getObject(subject, NEONE.hasEvent, model);
            IRI companyId = findObject(subject, API.hasSubscriber, model).orElse(null);
            return new NotificationMetadata(subject, notificationIri, callbackUrl, includeLoBody, eventIri, companyId);
        } catch (MalformedURLException | URISyntaxException e) {
            throw new NeoneException("Unable to deserialize Notification metadata model", e);
        }
    }

    @Override
    public Model fromJava(NotificationMetadata entity) {
        ModelBuilder modelBuilder = new ModelBuilder()
                .subject(entity.iri())
                .add(RDF.TYPE, NEONE.NotificationMetadata)
                .add(NEONE.describes, entity.describes())
                .add(NEONE.callbackUrl, entity.callbackUrl())
                .add(NEONE.includeLogisticsObject, entity.includeLoBody())
                .add(NEONE.hasEvent, entity.loEventIri());
        if (entity.companyId() != null) {
            modelBuilder.add(API.hasSubscriber, entity.companyId());
        }
        return modelBuilder.build();
    }
}
