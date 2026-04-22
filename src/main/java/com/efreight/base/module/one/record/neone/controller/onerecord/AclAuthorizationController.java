//  Copyright Open Logistics Foundation
//
//  Licensed under the Open Logistics Foundation License 1.3.
//  For details on the licensing terms, see the LICENSE file.
//  SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.controller.onerecord;

import com.efreight.base.module.one.record.neone.exception.InvalidApiRequestException;
import com.efreight.base.module.one.record.neone.model.onerecord.AclAuthorization;
import com.efreight.base.module.one.record.neone.model.onerecord.IdProvider;
import com.efreight.base.module.one.record.neone.service.onerecord.AclAuthorizationService;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.util.Values;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 供内部用户管理ACL的端点
 */
@RestController
@RequestMapping(value = "/" + AclAuthorizationController.RESOURCE_NAME,
        consumes = {"application/ld+json", "application/x-turtle", "text/turtle"},
        produces = {"application/ld+json", "application/x-turtle", "text/turtle"})
public class AclAuthorizationController {

    public static final String RESOURCE_NAME = "v2/internal/acls";
    private final AclAuthorizationService aclService;
    private final IdProvider idProvider;

    public AclAuthorizationController(AclAuthorizationService aclService, IdProvider idProvider) {
        this.aclService = aclService;
        this.idProvider = idProvider;
    }

    @GetMapping
    public List<AclAuthorization> findAclAuthorization(@RequestParam(name = "agent", required = false) String agent,
                                                       @RequestParam(name = "accessTo", required = false) String accessTo) {
        Optional<IRI> agentIri = Optional.ofNullable(agent).filter(s -> !s.trim().isEmpty()).map(Values::iri);
        Optional<IRI> accessToIri = Optional.ofNullable(accessTo).filter(s -> !s.trim().isEmpty()).map(Values::iri);
        if (!agentIri.isPresent() && !accessToIri.isPresent()) {
            throw new InvalidApiRequestException("Incomplete parameters. agent: " + agent + ", accessTo: " + accessTo);
        }
        List<AclAuthorization> aclAuthorizationList = aclService.findAclAuthorizations(agentIri, accessToIri);
        return aclAuthorizationList;
    }

    @GetMapping("/{id}")
    public AclAuthorization getAclAuthorization(@PathVariable("id") String id) {
        return aclService.getAclAuthorization(idProvider.getAclAuthorizationId(id));
    }

    @PostMapping
    public ResponseEntity<?> createAclAuthorization(@RequestBody AclAuthorization aclAuthorization) {
        aclService.createAclAuthorization(aclAuthorization);
        URI uri = URI.create(aclAuthorization.iri().stringValue());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uri);
        return ResponseEntity.created(uri).headers(headers).build();
    }

    @PostMapping(value = "/grant", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> grantAccess(@RequestParam("agent") String agent,
                                         @RequestParam("accessTo") String accessTo,
                                         @RequestParam("modes") Set<String> modes) {
        IRI aclIri = aclService.authorize(
                Values.iri(agent),
                Values.iri(accessTo),
                modes.stream().map(Values::iri).collect(Collectors.toSet())
        );
        URI uri = URI.create(aclIri.stringValue());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uri);
        return ResponseEntity.created(uri).headers(headers).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateAclAuthorization(@PathVariable("id") String id,
                                                    @RequestBody AclAuthorization aclAuthorization) {
        if (id.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        aclAuthorization = aclAuthorization.withIri(idProvider.getAclAuthorizationId(id).getIri());
        aclService.updateAclAuthorization(aclAuthorization);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAclAuthorization(@PathVariable("id") String id) {
        if (id.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        aclService.deleteAclAuthorization(idProvider.getAclAuthorizationId(id).getIri());
        return ResponseEntity.ok().build();
    }
}
