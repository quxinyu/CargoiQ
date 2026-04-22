// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.service.onerecord;

import com.efreight.base.module.one.record.neone.model.onerecord.IdProvider;
import com.efreight.base.module.one.record.neone.model.onerecord.LogisticsObject;
import com.efreight.base.module.one.record.neone.model.onerecord.LogisticsObjectUtil;
import com.efreight.base.module.one.record.neone.model.onerecord.Snapshot;
import com.efreight.base.module.one.record.neone.repository.RepositoryTransaction;
import com.efreight.base.module.one.record.neone.repository.SnapshotRepository;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Instant;
import java.util.Optional;

@Component
@ApplicationScoped
public class SnapshotService {

    private static final Logger log = LoggerFactory.getLogger(SnapshotService.class);

    private final SnapshotRepository repository;

    private final RepositoryTransaction transaction;

    private final IdProvider idProvider;

    @Inject
    public SnapshotService(SnapshotRepository repository,
                           RepositoryTransaction transaction,
                           IdProvider idProvider) {
        this.repository = repository;
        this.transaction = transaction;
        this.idProvider = idProvider;
    }

    public Snapshot createSnapshot(LogisticsObject logisticsObject,
                                   Integer revision,
                                   RepositoryConnection connection) {
        log.debug("Creating snapshot for LO [{}]", logisticsObject.iri());
        String payload = LogisticsObjectUtil.convertToJsonLd(logisticsObject.model());
        Snapshot snapshot = new Snapshot(
                idProvider.createInternalIri().getIri(),
                logisticsObject.iri(),
                payload,
                Instant.now(),
                revision);
        repository.persist(snapshot, connection);
        log.info(">>>>>>>>>快照数据 保存完成 物流对象:{}, 数据快照:{} <<<<<<<<", logisticsObject, snapshot);
        return snapshot;
    }

    public Optional<Snapshot> getSnapshot(IRI loIri, Instant at) {
        log.debug("Fetching snapshot for LO [{}] at timestamp [{}]", loIri, at);
        return transaction.transactionallyGet(connection -> repository.getSnapshot(loIri, at, connection));
    }

    public Optional<Snapshot> getLatestSnapshot(IRI loIri) {
        log.debug("Fetching latest snapshot for LO [{}]", loIri);
        // No snapshot can be younger than "now".
        return transaction.transactionallyGet(connection -> repository.getSnapshot(loIri, Instant.now(), connection));
    }
}
