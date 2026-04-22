// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.repository;

import com.efreight.base.module.one.record.neone.handler.AuditTrailHandler;
import com.efreight.base.module.one.record.neone.iata.onerecord.API;
import com.efreight.base.module.one.record.neone.model.onerecord.AuditTrail;
import com.efreight.base.module.one.record.neone.model.onerecord.IdProvider;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.model.vocabulary.XMLSchema;
import org.eclipse.rdf4j.query.GraphQuery;
import org.eclipse.rdf4j.query.GraphQueryResult;
import org.eclipse.rdf4j.query.QueryResults;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.sparqlbuilder.constraint.Expression;
import org.eclipse.rdf4j.sparqlbuilder.constraint.Expressions;
import org.eclipse.rdf4j.sparqlbuilder.core.Orderable;
import org.eclipse.rdf4j.sparqlbuilder.core.SparqlBuilder;
import org.eclipse.rdf4j.sparqlbuilder.core.Variable;
import org.springframework.stereotype.Component;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Instant;
import java.util.Optional;

@Component
@ApplicationScoped
public class AuditTrailRepository extends ModelRepository<AuditTrail> {

    @Inject
    public AuditTrailRepository(Repository repository, AuditTrailHandler handler, IdProvider idProvider) {
        super(repository, handler, idProvider);
    }

    public Optional<AuditTrail> getAuditTrail(IRI auditTrailIri,
                                              Instant changesFrom,
                                              Instant changesUntil,
                                              RepositoryConnection connection) {

        Variable auditTrail = SparqlBuilder.var("auditTrail");
        Variable from = SparqlBuilder.var("from");
        Variable until = SparqlBuilder.var("until");
        String queryString = constructAuditTrailQuery(auditTrail, from, until,
                Optional.ofNullable(changesFrom), Optional.ofNullable(changesUntil),
                Optional.empty(), Optional.empty());

        GraphQuery query = connection.prepareGraphQuery(queryString);
        query.setBinding("auditTrail", auditTrailIri);
        if (changesFrom != null) {
            query.setBinding("from", Values.literal(changesFrom.toString(), XMLSchema.DATETIME));
        }
        if (changesUntil != null) {
            query.setBinding("until", Values.literal(changesUntil.toString(), XMLSchema.DATETIME));
        }

        try (GraphQueryResult result = query.evaluate()) {
            if (result.hasNext()) {
                Model model = QueryResults.asModel(result);
                AuditTrail at = modelHandler.fromModel(auditTrailIri, model);
                return Optional.of(at);
            } else {
                return Optional.empty();
            }
        }
    }

    private String constructAuditTrailQuery(Variable varAuditTrail,
                                            Variable varFrom,
                                            Variable varUntil,
                                            Optional<Instant> from,
                                            Optional<Instant> until,
                                            Optional<Orderable> orderBy,
                                            Optional<Integer> limit) {
        Variable latestRevision = SparqlBuilder.var("latestRevision");
        Variable changeRequest = SparqlBuilder.var("changeRequest");
        Variable change = SparqlBuilder.var("change");
        Variable operation = SparqlBuilder.var("operation");
        Variable o = SparqlBuilder.var("o");
        Variable op = SparqlBuilder.var("op");
        Variable p = SparqlBuilder.var("p");
        Variable s = SparqlBuilder.var("s");
        Variable error = SparqlBuilder.var("error");
        Variable requestStatus = SparqlBuilder.var("requestStatus");
        Variable requestedAt = SparqlBuilder.var("requestedAt");
        Variable requestedBy = SparqlBuilder.var("requestedBy");
        Variable revokedAt = SparqlBuilder.var("revokedAt");
        Variable revokedBy = SparqlBuilder.var("revokedBy");
        Variable description = SparqlBuilder.var("description");
        Variable revision = SparqlBuilder.var("revision");
        Variable logisticsObject = SparqlBuilder.var("logisticsObject");
        Variable datatype = SparqlBuilder.var("datatype");
        Variable value = SparqlBuilder.var("value");

        // 使用SPARQL字符串避免ConstructBuilder类型问题
        String baseQuery =
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                        "CONSTRUCT {\n" +
                        "  ?auditTrail <" + API.hasLatestRevision + "> ?latestRevision .\n" +
                        "  ?auditTrail <" + API.hasChangeRequest + "> ?changeRequest .\n" +
                        "  ?changeRequest <" + API.isRequestedAt + "> ?requestedAt .\n" +
                        "  ?changeRequest <" + API.isRequestedBy + "> ?requestedBy .\n" +
                        "  ?changeRequest <" + API.hasRequestStatus + "> ?requestStatus .\n" +
                        "  ?changeRequest <" + API.isRevokedAt + "> ?revokedAt .\n" +
                        "  ?changeRequest <" + API.isRevokedBy + "> ?revokedBy .\n" +
                        "  ?changeRequest <" + API.hasError + "> ?error .\n" +
                        "  ?changeRequest <" + API.hasChange + "> ?change .\n" +
                        "  ?change <" + API.hasDescription + "> ?description .\n" +
                        "  ?change <" + API.hasRevision + "> ?revision .\n" +
                        "  ?change <" + API.hasLogisticsObject + "> ?logisticsObject .\n" +
                        "  ?change <" + API.hasOperation + "> ?operation .\n" +
                        "  ?operation <" + API.op + "> ?op .\n" +
                        "  ?operation <" + API.o + "> ?o .\n" +
                        "  ?operation <" + API.p + "> ?p .\n" +
                        "  ?operation <" + API.s + "> ?s .\n" +
                        "  ?o <" + API.hasDatatype + "> ?datatype .\n" +
                        "  ?o <" + API.hasValue + "> ?value .\n" +
                        "}\n" +
                        "WHERE {\n" +
                        "  ?auditTrail a <" + API.AuditTrail + "> ;\n" +
                        "    <" + API.hasLatestRevision + "> ?latestRevision .\n";

        // 添加WHERE子句的其余部分
        String whereClause =
                "  OPTIONAL {\n" +
                        "    ?auditTrail <" + API.hasChangeRequest + "> ?changeRequest .\n" +
                        "    {\n" +
                        "      ?changeRequest a <" + API.ChangeRequest + "> ;\n" +
                        "        <" + API.isRequestedAt + "> ?requestedAt ;\n" +
                        "        <" + API.isRequestedBy + "> ?requestedBy ;\n" +
                        "        <" + API.hasRequestStatus + "> ?requestStatus ;\n" +
                        "        <" + API.hasChange + "> ?change .\n" +
                        "      OPTIONAL { ?changeRequest <" + API.isRevokedAt + "> ?revokedAt . }\n" +
                        "      OPTIONAL { ?changeRequest <" + API.isRevokedBy + "> ?revokedBy . }\n" +
                        "      OPTIONAL { ?changeRequest <" + API.hasError + "> ?error . }\n" +
                        "    }\n" +
                        "    OPTIONAL { ?change <" + API.hasDescription + "> ?description . }\n" +
                        "    ?change <" + API.hasLogisticsObject + "> ?logisticsObject ;\n" +
                        "      <" + API.hasRevision + "> ?revision ;\n" +
                        "      <" + API.hasOperation + "> ?operation .\n" +
                        "    ?operation <" + API.op + "> ?op ;\n" +
                        "      <" + API.s + "> ?s ;\n" +
                        "      <" + API.p + "> ?p ;\n" +
                        "      <" + API.o + "> ?o .\n" +
                        "    ?o <" + API.hasDatatype + "> ?datatype ;\n" +
                        "      <" + API.hasValue + "> ?value .\n" +
                        "  }\n" +
                        "}\n";

        String queryString = baseQuery + whereClause;

        // 添加过滤器
        Optional<Expression> expression = createFilter(requestedAt, varFrom, varUntil, from, until);
        if (expression.isPresent()) {
            // 在WHERE子句中添加FILTER
            String filterStr = "  FILTER(" + expression.get().getQueryString() + ")\n";
            queryString = queryString.replace(
                    "      OPTIONAL { ?changeRequest <" + API.hasError + "> ?error . }\n" +
                            "    }\n",
                    "      OPTIONAL { ?changeRequest <" + API.hasError + "> ?error . }\n" +
                            "    }\n" + filterStr
            );
        }

        // 添加ORDER BY和LIMIT
        if (orderBy.isPresent()) {
            String orderByClause = "  ORDER BY " + orderBy.get().getQueryString() + "\n";
            queryString = queryString.replace("}\n", orderByClause + "}\n");
        }

        if (limit.isPresent()) {
            queryString = queryString.replace("}\n", "  LIMIT " + limit.get() + "\n}\n");
        }

        return queryString;
    }

    private Optional<Expression> createFilter(Variable requestedAt, Variable varFrom, Variable varUntil,
                                              Optional<Instant> from, Optional<Instant> until) {
        Optional<Expression> filterExpression = Optional.empty();
        if (from.isPresent() && until.isPresent()) {
            filterExpression = Optional.of(Expressions.and(Expressions.gte(requestedAt, varFrom), Expressions.lte(requestedAt, varUntil)));
        } else if (from.isPresent()) {
            filterExpression = Optional.of(Expressions.gte(requestedAt, varFrom));
        } else if (until.isPresent()) {
            filterExpression = Optional.of(Expressions.lte(requestedAt, varUntil));
        }
        return filterExpression;
    }
}
/*
PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
CONSTRUCT {
    ?auditTrail <https://onerecord.iata.org/ns/api#hasLatestRevision> ?latestRevision .
    ?auditTrail <https://onerecord.iata.org/ns/api#hasChangeRequest> ?changeRequest .
    ?changeRequest <https://onerecord.iata.org/ns/api#isRequestedAt> ?requestedAt .
    ?changeRequest <https://onerecord.iata.org/ns/api#isRequestedBy> ?requestedBy .
    ?changeRequest <https://onerecord.iata.org/ns/api#hasRequestStatus> ?requestStatus .
    ?changeRequest <https://onerecord.iata.org/ns/api#isRevokedAt> ?revokedAt .
    ?changeRequest <https://onerecord.iata.org/ns/api#isRevokedBy> ?revokedBy .
    ?changeRequest <https://onerecord.iata.org/ns/api#hasError> ?error .
    ?changeRequest <https://onerecord.iata.org/ns/api#hasChange> ?change .
    ?change <https://onerecord.iata.org/ns/api#hasDescription> ?description .
    ?change <https://onerecord.iata.org/ns/api#hasRevision> ?revision .
    ?change <https://onerecord.iata.org/ns/api#hasLogisticsObject> ?logisticsObject .
    ?change <https://onerecord.iata.org/ns/api#hasOperation> ?operation .
    ?operation <https://onerecord.iata.org/ns/api#op> ?op .
    ?operation <https://onerecord.iata.org/ns/api#o> ?o .
    ?operation <https://onerecord.iata.org/ns/api#p> ?p .
    ?operation <https://onerecord.iata.org/ns/api#s> ?s .
    ?o <https://onerecord.iata.org/ns/api#hasDatatype> ?datatype .
    ?o <https://onerecord.iata.org/ns/api#hasValue> ?value .
}
WHERE {
    ?auditTrail a <https://onerecord.iata.org/ns/api#AuditTrail> ;
        <https://onerecord.iata.org/ns/api#hasLatestRevision> ?latestRevision .
    OPTIONAL {
        ?auditTrail <https://onerecord.iata.org/ns/api#hasChangeRequest> ?changeRequest .
        {
            ?changeRequest a <https://onerecord.iata.org/ns/api#ChangeRequest> ;
                <https://onerecord.iata.org/ns/api#isRequestedAt> ?requestedAt ;
                <https://onerecord.iata.org/ns/api#isRequestedBy> ?requestedBy ;
                <https://onerecord.iata.org/ns/api#hasRequestStatus> ?requestStatus ;
                <https://onerecord.iata.org/ns/api#hasChange> ?change .
                OPTIONAL { ?changeRequest <https://onerecord.iata.org/ns/api#isRevokedAt> ?revokedAt . }
                OPTIONAL { ?changeRequest <https://onerecord.iata.org/ns/api#isRevokedBy> ?revokedBy . }
                OPTIONAL { ?changeRequest <https://onerecord.iata.org/ns/api#hasError> ?error . }
        }
        OPTIONAL { ?change <https://onerecord.iata.org/ns/api#hasDescription> ?description . }
        ?change <https://onerecord.iata.org/ns/api#hasLogisticsObject> ?logisticsObject ;
            <https://onerecord.iata.org/ns/api#hasRevision> ?revision ;
            <https://onerecord.iata.org/ns/api#hasOperation> ?operation .
        ?operation <https://onerecord.iata.org/ns/api#op> ?op ;
            <https://onerecord.iata.org/ns/api#s> ?s ;
            <https://onerecord.iata.org/ns/api#p> ?p ;
            <https://onerecord.iata.org/ns/api#o> ?o .
        ?o <https://onerecord.iata.org/ns/api#hasDatatype> ?datatype ;
            <https://onerecord.iata.org/ns/api#hasValue> ?value .
    }
}
*/
