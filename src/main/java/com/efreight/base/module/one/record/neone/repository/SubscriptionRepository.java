// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.repository;

import com.efreight.base.module.one.record.neone.handler.SubscriptionHandler;
import com.efreight.base.module.one.record.neone.iata.onerecord.API;
import com.efreight.base.module.one.record.neone.iata.onerecord.NEONE;
import com.efreight.base.module.one.record.neone.model.onerecord.IdProvider;
import com.efreight.base.module.one.record.neone.model.onerecord.RequestStatus;
import com.efreight.base.module.one.record.neone.model.onerecord.Subscription;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.model.vocabulary.XMLSchema;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.sparqlbuilder.constraint.Expressions;
import org.eclipse.rdf4j.sparqlbuilder.core.SparqlBuilder;
import org.eclipse.rdf4j.sparqlbuilder.core.Variable;
import org.eclipse.rdf4j.sparqlbuilder.core.query.Queries;
import org.eclipse.rdf4j.sparqlbuilder.graphpattern.GraphPattern;
import org.eclipse.rdf4j.sparqlbuilder.graphpattern.GraphPatternNotTriples;
import org.eclipse.rdf4j.sparqlbuilder.graphpattern.GraphPatterns;
import org.eclipse.rdf4j.sparqlbuilder.graphpattern.TriplePattern;
import org.eclipse.rdf4j.sparqlbuilder.rdf.Iri;
import org.eclipse.rdf4j.sparqlbuilder.rdf.Rdf;
import org.eclipse.rdf4j.sparqlbuilder.rdf.RdfLiteral;
import org.eclipse.rdf4j.sparqlbuilder.rdf.RdfObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

@Component
@ApplicationScoped
public class SubscriptionRepository extends ModelRepository<Subscription> {

    private static final Logger log = LoggerFactory.getLogger(SubscriptionRepository.class);

    @Inject
    public SubscriptionRepository(Repository repository, SubscriptionHandler modelHandler, IdProvider idProvider) {
        super(repository, modelHandler, idProvider);
    }

    public boolean subscriptionForLogisticsObjectsExists(IRI loIri, Set<IRI> loTypes, RepositoryConnection connection) {

        Set<String> types = loTypes.stream().map(IRI::stringValue).collect(Collectors.toSet());

        log.info("Finding out if subscripion for LO [{}] with type(s) [{}] exists", loIri,
                String.join(",", types));

        Variable iriVar = SparqlBuilder.var("loIri");

        String queryString = createSubscriptionExistsQuery(iriVar, types);
        TupleQuery query = connection.prepareTupleQuery(queryString);
        query.setBinding("loIri", Values.literal(loIri.stringValue()));

        try (TupleQueryResult queryResult = query.evaluate()) {
            return queryResult.hasNext();
        }
    }

    private String createSubscriptionExistsQuery(Variable loIriVar, Set<String> loTypes) {
        Variable subscriptionVar = SparqlBuilder.var("subscrIri");
        Variable loTypeVar = SparqlBuilder.var("loType");

        RdfLiteral.StringLiteral[] topicLiterals = loTypes.stream()
                .map(Rdf::literalOf)
                .toArray(RdfLiteral.StringLiteral[]::new);

        // 修复 JDK 1.8 兼容性问题：使用 GraphPatterns.and 替代 .andHas() 链式调用
        GraphPatternNotTriples union =
                GraphPatterns.union(
                        GraphPatterns.and(
                                subscriptionVar.has(API.hasTopicType, API.LOGISTICS_OBJECT_IDENTIFIER),
                                subscriptionVar.has(API.hasTopic, loIriVar)
                        ),
                        GraphPatterns.and(
                                subscriptionVar.has(API.hasTopicType, API.LOGISTICS_OBJECT_TYPE),
                                subscriptionVar.has(API.hasTopic, loTypeVar)
                        ).filter(Expressions.in(loTypeVar, topicLiterals)));

        return Queries.SELECT()
                .select(subscriptionVar)
                .where(subscriptionVar.isA((RdfObject) API.Subscription)
                        .and(union))
                .getQueryString();
    }

    public List<Subscription> getAcceptedSubscriptions(List<String> companyIds, Subscription.TopicType topicType,
                                                       Collection<String> topics, RepositoryConnection connection) {
        log.info("Get accepted subscriptions for companies [{}] with topicType [{}] and topic(s) [{}]",
                companyIds, topicType, String.join(",", topics));

        Variable subscription = SparqlBuilder.var("subscription");
        String query = createSubscriptionsQuery(subscription, companyIds, topicType, topics,
                Collections.singleton(RequestStatus.REQUEST_ACCEPTED));
        TupleQuery tupleQuery = connection.prepareTupleQuery(query);
        try (TupleQueryResult queryResult = tupleQuery.evaluate()) {
            return queryResult.stream().map(bindingSet -> {
                IRI subscriptionIri = (IRI) bindingSet.getValue("subscription");
                return findByIri(subscriptionIri, connection);
            }).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
        }
    }

    private String createSubscriptionsQuery(Variable subscription, List<String> companyIds,
                                            Subscription.TopicType topicType, Collection<String> topics,
                                            Collection<RequestStatus> requestStatuses) {
        Variable topicVar = SparqlBuilder.var("topic");
        // 修复 JDK 1.8 兼容性问题：使用 GraphPatterns.and 替代 .andHas() 链式调用
        GraphPattern whereClause = GraphPatterns.and(
                subscription.isA(Rdf.iri(API.Subscription)),
                subscription.has(API.hasTopicType, Rdf.iri(topicType.getValue())),
                subscription.has(API.hasTopic, topicVar)
        );
        GraphPattern pattern;
        if (requestStatuses != null && !requestStatuses.isEmpty()) {
            Variable subscriptionRequest = SparqlBuilder.var("subscriptionRequest");
            Variable requestStatus = SparqlBuilder.var("requestStatus");
            Iri[] requestStatusIris = requestStatuses.stream().map(rs -> Rdf.iri(rs.iri())).toArray(Iri[]::new);
            pattern = whereClause
                    .and(subscriptionRequest.has(API.hasSubscription, subscription))
                    .and(
                            subscriptionRequest
                                    .has(API.hasRequestStatus, requestStatus)
                                    .filter(Expressions.in(requestStatus, requestStatusIris)));
        } else {
            pattern = whereClause;
        }

        if (companyIds != null) {
            Iri[] companyIris = companyIds.stream().map(Rdf::iri).collect(Collectors.toList()).toArray(new Iri[companyIds.size()]);
            Variable companyId = SparqlBuilder.var("companyId");
            pattern = pattern.and(GraphPatterns.and(
                    subscription.has(API.hasSubscriber, companyId)
            )).filter(Expressions.in(companyId, companyIris));
        }
        RdfLiteral.StringLiteral[] topicLiterals = topics.stream()
                .map(s -> Rdf.literalOfType(s, XMLSchema.ANYURI))
                .toArray(size -> new RdfLiteral.StringLiteral[size]);
        pattern.filter(Expressions.in(topicVar, topicLiterals));
        return Queries.SELECT().select(subscription).where(pattern).getQueryString();
    }
}
