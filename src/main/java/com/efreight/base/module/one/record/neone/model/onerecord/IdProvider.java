// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.model.onerecord;

import com.efreight.base.common.core.exception.EftException;
import com.efreight.base.module.one.record.neone.config.LogisticsObjectIdConfig;
import com.efreight.base.module.one.record.neone.config.NeoneServerConfig;
import com.efreight.base.module.one.record.neone.controller.NeOneLogisticsEventsController;
import com.efreight.base.module.one.record.neone.controller.onerecord.*;
import com.efreight.base.module.one.record.neone.service.onerecord.NeoneId;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.util.Values;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.net.URI;

@Component
@ApplicationScoped
public class IdProvider {

    private final LogisticsObjectIdConfig loIdConfig;

    private final NeoneServerConfig neoneServerConfig;

    @Inject
    public IdProvider(LogisticsObjectIdConfig loIdConfig,
                      NeoneServerConfig neoneServerConfig) {
        this.loIdConfig = loIdConfig;
        this.neoneServerConfig = neoneServerConfig;
    }

    public NeoneId createUniqueLoUri() {
        return NeoneId.fromUri(getLoUriBase().path(loIdConfig.getRandomIdStrategy().randomId()).build().toUri());
    }

    public NeoneId getUriForLoId(String uuid) {
        return NeoneId.fromUri(getLoUriBase().path(uuid).build().toUri());
    }

    public NeoneId getDataHolderId() {
        return getUriForLoId(neoneServerConfig.getDataHolder().getId());
    }

    private UriComponentsBuilder getLoUriBase() {
        return baseUri().path(LogisticsObjectsController.RESOURCE_NAME + "/");
    }

    public NeoneId createLoEventUri(NeoneId loUri) {
        return createLoEventUri(loUri.getIri());
    }

    public NeoneId createLoEventUri(IRI loIri) {
        return NeoneId.fromUri(UriComponentsBuilder.fromUri(URI.create(loIri.stringValue()))
                .path("/" + NeOneLogisticsEventsController.LOGISTICS_EVENT_RESOURCE_NAME + "/")
                .path(loIdConfig.getRandomIdStrategy().randomId())
                .build().toUri());
    }

    public NeoneId createNotificationId(IRI loIri) {
        return NeoneId.fromUri(UriComponentsBuilder.fromUri(URI.create(loIri.stringValue()))
                .path(NotificationController.RESOURCE_NAME + "/")
                .path(loIdConfig.getRandomIdStrategy().randomId())
                .build().toUri());
    }

    public NeoneId getUriForEventId(String loUuid, String eventId) {
        return NeoneId.fromUri(UriComponentsBuilder.fromUri(getUriForLoId(loUuid).getUri())
                .path("/" + NeOneLogisticsEventsController.LOGISTICS_EVENT_RESOURCE_NAME + "/")
                .path(eventId)
                .build().toUri());
    }

    public NeoneId getAuditTrailIriFromLoId(String uuid) {
        return NeoneId.fromUri(UriComponentsBuilder.fromUri(getUriForLoId(uuid).getUri())
                .path(AuditTrailController.AUDIT_TRAIL_RESOURCE_NAME)
                .build().toUri());
    }

    public NeoneId getActionRequestId(String uuid) {
        return NeoneId.fromUri(baseUri().path(ActionRequestController.ACTION_REQUEST_RESOURCE_NAME).path(uuid).build().toUri());
    }

    public NeoneId createAclAuthorizationId() {
        return NeoneId.fromUri(baseUri().path(AclAuthorizationController.RESOURCE_NAME + "/").path(loIdConfig.getRandomIdStrategy().randomId())
                .build().toUri());
    }

    public NeoneId getAclAuthorizationId(String id) {
        return NeoneId.fromUri(baseUri().path(AclAuthorizationController.RESOURCE_NAME + "/").path(id)
                .build().toUri());
    }

    public NeoneId createActionRequestId() {
        return getActionRequestId(loIdConfig.getRandomIdStrategy().randomId());
    }

    //
    public NeoneId getAuditTrailIriFromLoIri(IRI loIRI) {
        return NeoneId.fromIri(Values.iri(loIRI.stringValue() + "/" + AuditTrailController.AUDIT_TRAIL_RESOURCE_NAME));
    }

    public NeoneId createUniqueSubscriptionRequestUri() {
        return NeoneId.fromUri(getSubscriptionRequestUriBase().path(loIdConfig.getRandomIdStrategy().randomId()).build().toUri());
    }

    public NeoneId createUniqueAccessDelegationRequestUri() {
        return NeoneId.fromUri(baseUri().path(ActionRequestController.ACTION_REQUEST_RESOURCE_NAME).path(loIdConfig.getRandomIdStrategy().randomId()).build().toUri());
    }

    private UriComponentsBuilder getSubscriptionRequestUriBase() {
        return baseUri().path(ActionRequestController.ACTION_REQUEST_RESOURCE_NAME + "/");
    }

    public NeoneId createUniqueSubscriptionUri() {
        String randomId = loIdConfig.getRandomIdStrategy().randomId();
        UriComponentsBuilder base = getSubscriptionUriBase();
        URI uri = base.path(randomId).build().toUri();
        return NeoneId.fromUri(uri);
    }

    private UriComponentsBuilder getSubscriptionUriBase() {
        return baseUri().path(SubscriptionController.RESOURCE_NAME + "/");
    }

    public NeoneId createInternalIri() {
        String schema = loIdConfig.getInternalIriScheme();
        return NeoneId.fromIri(
                Values.iri(schema + ":" + loIdConfig.getRandomIdStrategy().randomId()), true, true
        );
    }

    public NeoneId getBaseId() {
        return NeoneId.fromUri(baseUri().build().toUri());
    }

    private UriComponentsBuilder baseUri() {
        return UriComponentsBuilder.fromUriString(loIdConfig.getScheme() + "://" + loIdConfig.getHost()
                + loIdConfig.getPort().map(port -> ":" + port).orElse("")
                + loIdConfig.getRootPath());
    }

    public IRI getLogisticsObjectBaseIri() {
        return Values.iri(this.getLoUriBase().build().toUriString());
    }

    public NeoneId parse(String iriString) {
        try {
            IRI iri = Values.iri(iriString);
            return NeoneId.fromString(iriString, isInternalId(iri), isLocal(iri));
        } catch (IllegalArgumentException ex) {
            throw new EftException(iriString);
        }
    }

    public NeoneId parse(IRI iri) {
        try {
            return NeoneId.fromString(iri.stringValue(), isInternalId(iri), isLocal(iri));
        } catch (IllegalArgumentException ex) {
            throw new EftException(iri.stringValue());
        }
    }

    private boolean isLocal(IRI iri) {
        return iri.stringValue().startsWith(baseUri().build().toUriString());
    }

    private boolean isInternalId(IRI iri) {
        return iri.stringValue().startsWith(loIdConfig.getInternalIriScheme() + ":");
    }
}
