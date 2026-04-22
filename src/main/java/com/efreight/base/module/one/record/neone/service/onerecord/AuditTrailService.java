// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.service.onerecord;

import com.efreight.base.module.one.record.neone.exception.RevisionMissingException;
import com.efreight.base.module.one.record.neone.exception.SubjectNotFoundException;
import com.efreight.base.module.one.record.neone.handler.AuditTrailHandler;
import com.efreight.base.module.one.record.neone.iata.onerecord.API;
import com.efreight.base.module.one.record.neone.model.onerecord.AuditTrail;
import com.efreight.base.module.one.record.neone.model.onerecord.ChangeRequest;
import com.efreight.base.module.one.record.neone.model.onerecord.IdProvider;
import com.efreight.base.module.one.record.neone.model.onerecord.RequestStatus;
import com.efreight.base.module.one.record.neone.repository.AuditTrailRepository;
import com.efreight.base.module.one.record.neone.repository.RepositoryTransaction;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.util.Models;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.springframework.stereotype.Component;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Instant;
import java.util.ArrayList;


@Slf4j
@Component
@ApplicationScoped
public class AuditTrailService {

    public static final int INITIAL_REVISION = 1;
    private final AuditTrailRepository repository;
    private final RepositoryTransaction transaction;
    private final AuditTrailHandler auditTrailHandler;
    private final IdProvider idProvider;
    private final LogisticsObjectMetadataService loMetaService;

    @Inject
    public AuditTrailService(AuditTrailRepository auditTrailRepository,
                             RepositoryTransaction transaction,
                             AuditTrailHandler auditTrailHandler,
                             LogisticsObjectMetadataService loMetaService,
                             IdProvider idProvider) {
        this.repository = auditTrailRepository;
        this.transaction = transaction;
        this.auditTrailHandler = auditTrailHandler;
        this.idProvider = idProvider;
        this.loMetaService = loMetaService;
    }

    public AuditTrail getAuditTrailByLoId(String loId,
                                          Instant from, Instant until) {
        NeoneId auditTrailIri = idProvider.getAuditTrailIriFromLoId(loId);
        return transaction.transactionallyGet(connection ->
                repository.getAuditTrail(auditTrailIri.getIri(), from, until, connection)
                        .orElseThrow(() -> new SubjectNotFoundException("Subject Not Found  " +auditTrailIri.getIri().stringValue()))
        );
    }

    public void createAuditTrail(IRI loIri) {
        transaction.transactionallyDo(connection -> createAuditTrail(loIri, connection));
    }

    public void createAuditTrail(IRI loIri,
                                 RepositoryConnection connection) {
        IRI auditTrailIri = idProvider.getAuditTrailIriFromLoIri(loIri).getIri();
        AuditTrail auditTrail = new AuditTrail(auditTrailIri, new ArrayList<>(), INITIAL_REVISION);
        repository.persist(auditTrail, connection);
        log.info(">>>>>>>>>审计数据 保存完成:{}<<<<<<<<", auditTrail);
    }

    public void updateAuditTrail(IRI loIri,
                                 ChangeRequest changeRequest,
                                 RepositoryConnection connection) {
        IRI auditTrailIri = idProvider.getAuditTrailIriFromLoIri(loIri).getIri();
        AuditTrail auditTrail = repository.findGraphByIri(auditTrailIri, connection)
                .orElseThrow(() -> new SubjectNotFoundException("Subject Not Found  " +auditTrailIri.stringValue()));

        // It's assumed that the change request itself has already been persisted,
        // so that only the link between audit trail and change request must be established.
        ModelBuilder builder = new ModelBuilder().add(auditTrail.iri(), API.hasChangeRequest, changeRequest.iri());
        repository.persist(builder.build(), connection);

        if (changeRequest.requestStatus().equals(RequestStatus.REQUEST_ACCEPTED)) {
            increaseRevisionNumber(auditTrail, connection);
        }
    }

    private void increaseRevisionNumber(AuditTrail auditTrail, RepositoryConnection connection) {
        Model model = auditTrailHandler.fromJava(auditTrail);
        String latestRevision = Models.objectString(model.filter(null, API.hasLatestRevision, null))
                .orElseThrow(() -> new RevisionMissingException(auditTrail.iri()));
        repository.delete(auditTrail.iri(), API.hasLatestRevision, connection);
        repository.add(auditTrail.iri(), API.hasLatestRevision,
                Values.literal(Integer.parseInt(latestRevision) + 1),
                connection
        );
    }
}
