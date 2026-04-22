// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.service.onerecord;

import com.efreight.base.module.one.record.neone.exception.SubjectNotFoundException;
import com.efreight.base.module.one.record.neone.handler.LogisticsEventHandler;
import com.efreight.base.module.one.record.neone.iata.onerecord.API;
import com.efreight.base.module.one.record.neone.model.onerecord.IdProvider;
import com.efreight.base.module.one.record.neone.model.onerecord.LogisticsEvent;
import com.efreight.base.module.one.record.neone.model.onerecord.LogisticsObject;
import com.efreight.base.module.one.record.neone.model.onerecord.NeoneEvent;
import com.efreight.base.module.one.record.neone.repository.*;
import com.efreight.base.module.one.record.neone.security.InternalAccessSubject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.util.ModelCollector;
import org.springframework.stereotype.Component;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.net.URI;
import java.time.Instant;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
@ApplicationScoped
public class LogisticsEventService {

    @ConfigProperty(name = "query-limit")
    Integer queryLimit;

    @org.springframework.beans.factory.annotation.Value("${authorize-creator:false}")
    boolean authorizeCreator;

    private final LogisticsEventRepository loEventRepository;

    private final LogisticsObjectService loService;

    private final LogisticsEventHandler logisticsEventHandler;

    private final NeoneEventRepository neoneEventRepository;

    private final LogisticsObjectRepository loRepository;

    private final AclAuthorizationRepository aclRepository;

    private final NeoneEventService neoneEventService;

    private final InternalAccessSubject accessSubject;

    private final IdProvider idProvider;

    private final RepositoryTransaction transaction;

    @Inject
    public LogisticsEventService(LogisticsEventRepository loEventRepository,
                                 LogisticsObjectService loService,
                                 RepositoryTransaction transaction,
                                 LogisticsEventHandler logisticsEventHandler,
                                 NeoneEventRepository neoneEventRepository,
                                 LogisticsObjectRepository loRepository,
                                 IdProvider idProvider,
                                 AclAuthorizationRepository aclRepository,
                                 NeoneEventService neoneEventService,
                                 InternalAccessSubject accessSubject) {

        this.loEventRepository = loEventRepository;
        this.loService = loService;
        this.logisticsEventHandler = logisticsEventHandler;
        this.transaction = transaction;
        this.neoneEventRepository = neoneEventRepository;
        this.loRepository = loRepository;
        this.idProvider = idProvider;
        this.aclRepository = aclRepository;
        this.neoneEventService = neoneEventService;
        this.accessSubject = accessSubject;
    }

    public LogisticsEvent getLogisticsEvent(NeoneId loEventId, boolean embedLo, Instant loAt) {
        LogisticsEvent event = fetchEvent(loEventId);
        Optional<LogisticsObject> lo = embedLo
                ? fetchLogisticsObjectLinkedToEvent(event, loAt)
                : Optional.empty();

        return lo.map(LogisticsObject::model)
                .map(event::withEmbeddedLoModel)
                .orElse(event);
    }

    public List<LogisticsEvent> findLogisticsEvents(NeoneId loId,
                                                    String eventType,
                                                    Instant createdFrom,
                                                    Instant createdUntil,
                                                    Instant occurredFrom,
                                                    Instant occurredUntil,
                                                    Integer limit) {

        Integer max = Optional.ofNullable(limit).filter(l -> l > 0 && l <= queryLimit).orElse(queryLimit);
        return transaction.transactionallyGet(connection -> {
                    List<LogisticsEvent> events;
                    if (loRepository.exists(loId.getIri(), connection)) {
                        events = loEventRepository.findEventsOfLogisticsObject(
                                loId.getIri(),
                                Optional.ofNullable(eventType),
                                Optional.ofNullable(createdFrom),
                                Optional.ofNullable(createdUntil),
                                Optional.ofNullable(occurredFrom),
                                Optional.ofNullable(occurredUntil),
                                max,
                                connection);
                    } else {
                        throw new SubjectNotFoundException("Subject Not Found  " +loId.getIri().stringValue());
                    }
                    return events;
                }
        );
    }

    private LogisticsEvent fetchEvent(NeoneId loEventId) {
        return transaction.transactionallyGet(connection ->
                loEventRepository.findByIri(loEventId.getIri(), connection)
        ).orElseThrow(() -> new SubjectNotFoundException("Subject Not Found  " +loEventId.getIri().stringValue()));
    }

    private Optional<LogisticsObject> fetchLogisticsObjectLinkedToEvent(LogisticsEvent event, Instant loAt) {
        Optional<Instant> at = Optional.ofNullable(loAt);
        return event.eventFor().flatMap(loIri ->
                at.isPresent()
                        ? loService.getLogisticsObjectSnapshot(loIri, at.get())
                        : Optional.of(loService.getLogisticsObject(URI.create(loIri.stringValue()), false))
        );
    }

    public LogisticsEvent addLogisticsEvent(NeoneId loId, LogisticsEvent event) {
        // assign event id
        LogisticsEvent eventWithId = event.withId(idProvider.createLoEventUri(loId).getIri());

        transaction.transactionallyDo((connection, hooks) -> {
            if (!loRepository.exists(loId.getIri(), connection)) {
                throw new SubjectNotFoundException("Subject Not Found  " +loId.getIri().stringValue());
            }
            Set<IRI> loTypes = loRepository.getLogisticsObjectTypes(loId.getIri(), connection);
            LogisticsEvent completedEvent = eventWithId
                    .withLinkedObjectAndCreatedAt(loId.getIri(), Instant.now());
            loEventRepository.persist(completedEvent.iri(), completedEvent, connection);

            NeoneEvent loEventReceivedEvent = neoneEventRepository.addEvent(
                    loId.getIri(), API.LOGISTICS_EVENT_RECEIVED, Optional.empty(),
                    Collections.emptySet(), loTypes, connection
            );
            hooks.registerPostCommitHook(() -> neoneEventService.publishEvent(loEventReceivedEvent));

            IRI creatorIri = authorizeCreator ? accessSubject.iri() : null;
            aclRepository.grantDefaultAccess(eventWithId.iri(), creatorIri, false, connection);
        });

        return eventWithId;
    }

    private Model setRootIRI(NeoneId loEventId, Model model) {
        Model root = findRoot(model);

        return model.stream()
                .map(st ->
                        root.stream().collect(Collectors.toList()).contains(st)
                                ? SimpleValueFactory.getInstance().createStatement(loEventId.getIri(), st.getPredicate(), st.getObject())
                                : st
                )
                .collect(ModelCollector.toModel());
    }

    private static Model findRoot(Model model) {
        Predicate<Value> statementWithObjectValueExists =
                object -> model.getStatements(null, null, object).iterator().hasNext();

        Map<Resource, List<Statement>> statementsBySubject =
                model.stream().collect(Collectors.groupingBy(Statement::getSubject));

        return statementsBySubject.keySet().stream()
                .filter(statementWithObjectValueExists.negate()).findAny()
                .map(statementsBySubject::get).orElse(Collections.emptyList())
                .stream().collect(ModelCollector.toModel());
    }
}
