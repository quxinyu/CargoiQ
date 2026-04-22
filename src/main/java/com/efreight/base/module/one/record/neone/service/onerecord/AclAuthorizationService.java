//  Copyright Open Logistics Foundation
//
//  Licensed under the Open Logistics Foundation License 1.3.
//  For details on the licensing terms, see the LICENSE file.
//  SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.service.onerecord;

import com.efreight.base.module.one.record.neone.exception.AlreadyExistsException;
import com.efreight.base.module.one.record.neone.exception.SubjectNotFoundException;
import com.efreight.base.module.one.record.neone.model.onerecord.AclAuthorization;
import com.efreight.base.module.one.record.neone.repository.AclAuthorizationRepository;
import com.efreight.base.module.one.record.neone.repository.RepositoryTransaction;
import org.eclipse.rdf4j.model.IRI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@ApplicationScoped
public class AclAuthorizationService {

    private static final Logger log = LoggerFactory.getLogger(AclAuthorizationService.class);

    private final AclAuthorizationRepository aclAuthorizationRepository;

    private final RepositoryTransaction transaction;

    @Inject
    AclAuthorizationService(AclAuthorizationRepository aclAuthorizationRepository,
                            RepositoryTransaction transaction) {
        this.aclAuthorizationRepository = aclAuthorizationRepository;
        this.transaction = transaction;
    }

    public AclAuthorization getAclAuthorization(NeoneId id) {
        return transaction.transactionallyGet(connection ->
                aclAuthorizationRepository.findByIri(id.getIri(), connection)
        ).orElseThrow(() -> new SubjectNotFoundException("Subject Not Found  " + id.getIri().stringValue()));
    }

    public List<AclAuthorization> findAclAuthorizations(Optional<IRI> agent, Optional<IRI> accessTo) {
        return transaction.transactionallyGet(connection ->
                aclAuthorizationRepository.findAclAuthorizations(agent, accessTo, connection)
        );
    }

    public void createAclAuthorization(AclAuthorization aclAuthorization) {
        log.info("Persisting acl [{}]", aclAuthorization.iri());
        transaction.transactionallyDo(connection -> {
            AclAuthorization acl = aclAuthorizationRepository
                    .getAclAuthorization(aclAuthorization.agent(), aclAuthorization.accessTo(), connection);
            if (acl != null) {
                throw new AlreadyExistsException(acl.iri().stringValue());
            }
            aclAuthorizationRepository.persist(aclAuthorization, connection);
        });
    }

    public void updateAclAuthorization(AclAuthorization aclAuthorization) {
        IRI iri = aclAuthorization.iri();
        log.info("Updating acl [{}]", iri);
        transaction.transactionallyDo(connection -> {
            if (!aclAuthorizationRepository.exists(iri, connection)) {
                throw new SubjectNotFoundException("Subject Not Found  " +iri.stringValue());
            }
            aclAuthorizationRepository.deleteAll(iri, connection);
            aclAuthorizationRepository.persist(aclAuthorization, connection);
        });
    }

    public void deleteAclAuthorization(IRI aclAuthorizationIri) {
        log.info("Removing acl [{}]", aclAuthorizationIri);
        transaction.transactionallyDo(connection -> {
            if (!aclAuthorizationRepository.exists(aclAuthorizationIri, connection)) {
                throw new SubjectNotFoundException("Subject Not Found  " +aclAuthorizationIri.stringValue());
            }
            aclAuthorizationRepository.deleteAll(aclAuthorizationIri, connection);
        });
    }

    public boolean isAuthorized(IRI agent, IRI accessTo, IRI mode) {
        return transaction.transactionallyGet(connection -> {
            if (!aclAuthorizationRepository.exists(accessTo, connection)) {
                throw new SubjectNotFoundException("Subject Not Found  " +accessTo.stringValue());
            }
            return aclAuthorizationRepository.aclExists(agent, accessTo, mode, connection);
        });
    }

    public IRI authorize(IRI agent, IRI accessTo, Set<IRI> modes) {
        return transaction.transactionallyGet(connection ->
                aclAuthorizationRepository.grantAccess(agent, accessTo, modes, connection)
        );
    }
}
