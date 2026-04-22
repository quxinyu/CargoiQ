//  Copyright Open Logistics Foundation
//
//  Licensed under the Open Logistics Foundation License 1.3.
//  For details on the licensing terms, see the LICENSE file.
//  SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.listener;

import com.efreight.base.module.one.record.neone.model.onerecord.NeoneEvent;
import com.efreight.base.module.one.record.neone.model.onerecord.Subscription;
import com.efreight.base.module.one.record.neone.repository.RepositoryTransaction;
import com.efreight.base.module.one.record.neone.repository.SubscriptionRepository;
import com.efreight.base.module.one.record.neone.service.onerecord.NotificationService;
import com.google.common.eventbus.Subscribe;
import org.apache.commons.collections4.CollectionUtils;
import org.eclipse.rdf4j.model.IRI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Neone事件监听器
 * 监听 NeoneEvent 事件并处理订阅通知
 *
 * @author quxinyu
 * @since 2025-01-19
 */
@Component
public class NeoneEventsListener {

    private static final Logger log = LoggerFactory.getLogger(NeoneEventsListener.class);

    private final NotificationService notificationService;

    private final SubscriptionRepository subscriptionRepository;

    private final RepositoryTransaction transaction;

    /**
     * 构造函数
     *
     * @param notificationService    通知服务
     * @param subscriptionRepository 订阅仓储
     * @param transaction            事务管理器
     */
    public NeoneEventsListener(NotificationService notificationService,
                               SubscriptionRepository subscriptionRepository,
                               RepositoryTransaction transaction) {
        this.notificationService = notificationService;
        this.subscriptionRepository = subscriptionRepository;
        this.transaction = transaction;
    }

    /**
     * 监听 Neone 事件并处理通知
     *
     * @param event Neone 事件对象
     */
    @Subscribe
    public void onLogisticsObjectChanged(NeoneEvent event) {
        log.debug("Handling [{}] for LogisticsObject [{}]", event.notificationEventType(), event.loId().stringValue());
        // 1. 获取需要通知的公司ID列表
        List<String> companyIds = notificationService.getCompanyIdsToNotify(event.loId());
        // 2. 检索相关的订阅信息
        Set<Subscription> subscriptions = retrieveSubscriptions(companyIds, event.loTypes(), event.loId());
        log.info("查询获取订阅信息条数:{} ", subscriptions.size());
        log.info("查询获取订阅信息内容:{} ", subscriptions);
        // 3. 将事件加入通知队列
        if (CollectionUtils.isNotEmpty(subscriptions)) {
            notificationService.enqueue(event, subscriptions);
        }
    }

    /**
     * 检索订阅信息
     *
     * @param companyIds 公司ID列表
     * @param loTypes    物流对象类型集合
     * @param loIri      物流对象IRI
     * @return 订阅集合
     */
    private Set<Subscription> retrieveSubscriptions(List<String> companyIds, Collection<IRI> loTypes, IRI loIri) {
        List<String> types = loTypes.stream().map(IRI::stringValue).collect(Collectors.toList());
        return transaction.transactionallyGet(connection -> {
            // 按物流对象类型订阅
            List<Subscription> s1 = subscriptionRepository.getAcceptedSubscriptions(
                    companyIds,
                    Subscription.TopicType.LOGISTICS_OBJECT_TYPE,
                    types,
                    connection
            );
            // 按物流对象标识符订阅
            List<Subscription> s2 = subscriptionRepository.getAcceptedSubscriptions(
                    companyIds,
                    Subscription.TopicType.LOGISTICS_OBJECT_IDENTIFIER,
                    Arrays.asList(loIri.stringValue()),
                    connection
            );
            // 合并订阅集合
            Set<Subscription> subscriptions = new HashSet<>(s1);
            subscriptions.addAll(s2);
            return subscriptions;
        });
    }
}
