// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.service.onerecord;

import com.efreight.base.module.one.record.neone.model.onerecord.LogisticsObjectMetadata;
import com.efreight.base.module.one.record.neone.repository.LogisticsObjectMetadataRepository;
import com.efreight.base.module.one.record.neone.repository.RepositoryTransaction;
import org.eclipse.rdf4j.model.IRI;
import org.springframework.stereotype.Component;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@Component
@ApplicationScoped
public class LogisticsObjectMetadataService {

    private final LogisticsObjectMetadataRepository metadataRepository;

    private final RepositoryTransaction transaction;

    @Inject
    public LogisticsObjectMetadataService(LogisticsObjectMetadataRepository metadataRepository,
                                          RepositoryTransaction transaction) {
        this.metadataRepository = metadataRepository;
        this.transaction = transaction;
    }

    public Optional<LogisticsObjectMetadata> getMetadataOf(IRI subject) {
        return transaction.transactionallyGet(connection ->
                metadataRepository.getMetadataOfSubject(subject, connection)
        );
    }
}
