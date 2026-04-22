//  Copyright Open Logistics Foundation
//
//  Licensed under the Open Logistics Foundation License 1.3.
//  For details on the licensing terms, see the LICENSE file.
//  SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.repository;

import com.efreight.base.module.one.record.neone.handler.AclAuthorizationHandler;
import com.efreight.base.module.one.record.neone.iata.onerecord.ACL;
import com.efreight.base.module.one.record.neone.model.onerecord.AclAuthorization;
import com.efreight.base.module.one.record.neone.model.onerecord.IdProvider;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.util.Models;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.query.*;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.sparqlbuilder.core.SparqlBuilder;
import org.eclipse.rdf4j.sparqlbuilder.core.Variable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

@Component
@ApplicationScoped
public class AclAuthorizationRepository extends ModelRepository<AclAuthorization> {

    private static final Logger log = LoggerFactory.getLogger(AclAuthorizationRepository.class);

    @Inject
    public AclAuthorizationRepository(Repository repository, AclAuthorizationHandler handler, IdProvider idProvider) {
        super(repository, handler, idProvider);
    }

    /**
     * 获取ACL授权
     *
     * @param agent
     * @param accessTo
     * @param connection
     * @return
     */
    public AclAuthorization getAclAuthorization(IRI agent, IRI accessTo, RepositoryConnection connection) {
        List<AclAuthorization> aclAuthorizations =
                findAclAuthorizations(Optional.of(agent), Optional.of(accessTo), connection);
        if (aclAuthorizations.size() > 1) {
            log.warn("Ambiguous ACl entries found [{}]", aclAuthorizations.stream()
                    .map(AclAuthorization::iri)
                    .map(IRI::stringValue)
                    .collect(Collectors.joining(",")));
        }
        return aclAuthorizations.stream().findFirst().orElse(null);
    }

    public List<AclAuthorization> findAclAuthorizations(Optional<IRI> agent,
                                                        Optional<IRI> accessTo,
                                                        RepositoryConnection connection) {
        log.info("正在为代理 [{}] 和访问目标 [{}] 查找ACL授权", agent, accessTo);
        Variable agentVar = SparqlBuilder.var("agent");
        Variable accessToVar = SparqlBuilder.var("accessTo");
        String queryString = createAclQuery(agentVar, accessToVar);
        log.info(">>>>>>查找ACL授权 SPARQL语句:{} <<<<<<<<", queryString);
        GraphQuery query = connection.prepareGraphQuery(queryString);
        agent.ifPresent(a -> query.setBinding("agent", a));
        accessTo.ifPresent(a -> query.setBinding("accessTo", a));
        try (GraphQueryResult queryResult = query.evaluate()) {
            Model model = QueryResults.asModel(queryResult);
            return Models.subjectIRIs(model).stream()
                    .map(iri -> modelHandler.fromModel(Values.iri(iri.stringValue()), model))
                    .collect(Collectors.toList());
        }
    }

    public boolean aclExists(IRI agent, IRI accessTo, IRI mode, RepositoryConnection connection) {
        Variable agentVar = SparqlBuilder.var("agent");
        Variable accessToVar = SparqlBuilder.var("accessTo");
        Variable modeVar = SparqlBuilder.var("mode");
        TupleQuery query = connection.prepareTupleQuery(aclExistsQuery(agentVar, accessToVar, modeVar));
        query.setBinding("agent", agent);
        query.setBinding("accessTo", accessTo);
        query.setBinding("mode", mode);
        try (TupleQueryResult queryResult = query.evaluate()) {
            return queryResult.hasNext();
        }
    }

    public IRI grantAccess(IRI agent, IRI accessTo, Set<IRI> modes, RepositoryConnection connection) {
        log.info("正在授予 [{}] 对 [{}] 的访问权限，权限模式为 [{}]", agent, accessTo, modes);
        AclAuthorization acl = getAclAuthorization(agent, accessTo, connection);
        if (acl != null) {
            IRI aclIri = acl.iri();
            Set<IRI> currentModes = acl.modes();
            modes.forEach(iri -> {
                if (!currentModes.contains(iri)) {
                    log.debug("ACL已存在，主体为 [{}] 访问 [{}]，正在添加权限模式 [{}]",
                            agent, accessTo, iri);
                    connection.add(aclIri, ACL.mode, iri);
                }
            });
        } else {
            acl = new AclAuthorization(
                    idProvider.createAclAuthorizationId().getIri(),
                    accessTo,
                    agent,
                    modes
            );
            log.debug("没有找到现有的ACL授权记录，正在创建一个新的ACL条目");
            persist(acl, connection);
        }
        return acl.iri();
    }

    private String createAclQuery(Variable agentVar,
                                  Variable accessToVar) {

        // 使用字符串拼接构建SPARQL查询，兼容RDF4J 3.7.4
        return "CONSTRUCT {\n" +
                "  ?acl a <" + ACL.Authorization + "> .\n" +
                "  ?acl <" + ACL.agent + "> ?agent .\n" +
                "  ?acl <" + ACL.accessTo + "> ?accessTo .\n" +
                "  ?acl <" + ACL.mode + "> ?mode .\n" +
                "} WHERE {\n" +
                "  ?acl a <" + ACL.Authorization + "> ;\n" +
                "       <" + ACL.agent + "> ?agent ;\n" +
                "       <" + ACL.accessTo + "> ?accessTo ;\n" +
                "       <" + ACL.mode + "> ?mode .\n" +
                "}";
    }

    private String aclExistsQuery(Variable agentVar, Variable accessToVar, Variable modeVar) {
        // 使用字符串拼接构建SPARQL查询，兼容RDF4J 3.7.4
        return "SELECT ?acl WHERE {\n" +
                "  ?acl a <" + ACL.Authorization + "> ;\n" +
                "       <" + ACL.mode + "> ?mode ;\n" +
                "       <" + ACL.accessTo + "> ?accessTo .\n" +
                "  { ?acl <" + ACL.agent + "> ?agent } UNION { ?acl <" + ACL.agent + "> <" + ACL.AuthenticatedAgent + "> } .\n" +
                "} LIMIT 1";
    }

    /**
     * 1) 授予数据持有者对指定目标对象的读/写权限。
     * 2) 如果需要，授予创建者对指定目标对象的读取权限。
     * 3) 如果需要，授予已认证代理(公共用户)对目标对象的读取权限。
     *
     * @param targetIri    目标对象的IRI（例如Lo、Event、ActionRequest等）
     * @param creatorIri   如果设置，则创建者获得对目标对象的读取权限
     * @param publicAccess 如果为true，则公众获得对目标对象的读取权限
     * @param connection   database connection
     */
    public void grantDefaultAccess(IRI targetIri,
                                   IRI creatorIri,
                                   boolean publicAccess,
                                   RepositoryConnection connection) {
        // 为数据持有者添加ACL(读取,写入)权限
        grantAccess(idProvider.getDataHolderId().getIri(), targetIri, new HashSet<>(Arrays.asList(ACL.Read, ACL.Write)), connection);

        // 为公共访问添加ACL(读取)权限
        if (publicAccess) {
            grantAccess(ACL.AuthenticatedAgent, targetIri, new HashSet<>(Arrays.asList(ACL.Read)), connection);
        }
        // 为创建者添加ACL(读取)权限（如果需要）
        if (creatorIri != null && !creatorIri.equals(idProvider.getDataHolderId().getIri())) {
            grantAccess(creatorIri, targetIri, new HashSet<>(Arrays.asList(ACL.Read)), connection);
        }
    }
}
