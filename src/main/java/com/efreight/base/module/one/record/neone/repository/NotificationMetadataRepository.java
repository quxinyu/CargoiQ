//  Copyright Open Logistics Foundation
//
//  Licensed under the Open Logistics Foundation License 1.3.
//  For details on the licensing terms, see the LICENSE file.
//  SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.repository;

import com.efreight.base.module.one.record.neone.handler.NotificationMetadataHandler;
import com.efreight.base.module.one.record.neone.model.onerecord.IdProvider;
import com.efreight.base.module.one.record.neone.model.onerecord.NotificationMetadata;
import org.eclipse.rdf4j.repository.Repository;
import org.springframework.stereotype.Component;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Component
@ApplicationScoped
public class NotificationMetadataRepository extends MetadataRepository<NotificationMetadata> {

    @Inject
    public NotificationMetadataRepository(Repository repository,
                                          NotificationMetadataHandler modelHandler,
                                          IdProvider idProvider) {
        super(repository, modelHandler, idProvider);
    }
}
