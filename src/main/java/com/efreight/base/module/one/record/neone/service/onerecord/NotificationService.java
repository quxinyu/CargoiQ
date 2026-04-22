// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.service.onerecord;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.efreight.base.common.core.utils.SpringUtils;
import com.efreight.base.module.one.record.neone.client.RestClientBuilderProducer;
import com.efreight.base.module.one.record.neone.config.NotificationServiceConfig;
import com.efreight.base.module.one.record.neone.controller.onerecord.NotificationMessage;
import com.efreight.base.module.one.record.neone.exception.MissingMetadataException;
import com.efreight.base.module.one.record.neone.model.entity.NeOneServerCompanyHolder;
import com.efreight.base.module.one.record.neone.model.entity.NeOneSubscriptions;
import com.efreight.base.module.one.record.neone.service.SubscriptionsService;
import com.efreight.base.module.one.record.neone.model.onerecord.*;
import com.efreight.base.module.one.record.neone.repository.*;
import com.efreight.base.module.one.record.neone.service.NeOneCompanyService;
import com.efreight.base.module.one.record.neone.utils.HttpClientUtil;
import com.efreight.base.module.one.record.neone.utils.IriUtils;
import com.efreight.base.module.one.record.neone.utils.KeyLockTokenUtils;
import com.efreight.base.module.one.record.neone.utils.UrlFormatUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.cache.annotation.CacheKey;
import java.net.URI;
import java.time.Instant;
import java.util.*;

/**
 * 通知服务
 * <p>
 * 处理事件通知的发送和管理
 *
 * @author quxinyu
 * @since 2025-01-09
 */
@Slf4j
@Component
public class NotificationService {

    private final NotificationServiceConfig config;
    private final NeoneEventRepository neoneEventRepository;
    private final NotificationRepository notificationRepository;
    private final SubscriptionMetadataRepository subscriptionMetadataRepository;
    private final LogisticsObjectRepository logisticsObjectRepository;
    private final RepositoryTransaction transaction;
    private final RestClientBuilderProducer restClientBuilderProducer;
    private final NotificationMetadataRepository notificationMetadataRepository;
    private final IdProvider idProvider;
    private final SubscriptionsService subscriptionsService;

    /**
     * 构造函数
     *
     * @param config                         通知服务配置
     * @param neoneEventRepository           事件仓储
     * @param notificationRepository         通知仓储
     * @param logisticsObjectRepository      物流对象仓储
     * @param transaction                    事务管理器
     * @param idProvider                     ID 提供者
     * @param notificationMetadataRepository 通知元数据仓储
     * @param subscriptionMetadataRepository 订阅元数据仓储
     * @param restClientBuilderProducer      REST 客户端构建器
     */
    public NotificationService(NotificationServiceConfig config,
                               NeoneEventRepository neoneEventRepository,
                               NotificationRepository notificationRepository,
                               LogisticsObjectRepository logisticsObjectRepository,
                               RepositoryTransaction transaction,
                               IdProvider idProvider,
                               NotificationMetadataRepository notificationMetadataRepository,
                               SubscriptionMetadataRepository subscriptionMetadataRepository,
                               RestClientBuilderProducer restClientBuilderProducer,
                               SubscriptionsService subscriptionsService) {

        this.config = config;
        this.neoneEventRepository = neoneEventRepository;
        this.notificationRepository = notificationRepository;
        this.logisticsObjectRepository = logisticsObjectRepository;
        this.transaction = transaction;
        this.notificationMetadataRepository = notificationMetadataRepository;
        this.subscriptionMetadataRepository = subscriptionMetadataRepository;
        this.restClientBuilderProducer = restClientBuilderProducer;
        this.idProvider = idProvider;
        this.subscriptionsService = subscriptionsService;
    }

    public void enqueue(NeoneEvent event, Collection<Subscription> subscriptions) {
        // 在事务中执行
        transaction.transactionallyDo(connection -> {
            Set<IRI> loTypes = logisticsObjectRepository.getLogisticsObjectTypes(event.loId(), connection);
            // 2. 对每个回调 URL，持久化一个通知并关联事件和回调
            subscriptions.forEach(subscription -> {
                // 首先检查订阅是否已过期:
                Optional<SubscriptionMetadata> subscriptionMetadata = subscriptionMetadataRepository.getMetadataOfSubject(subscription.iri(), connection);
                Boolean hasExpired = subscription.expiresAt().map(Instant.now()::isAfter).orElse(false);
                if (hasExpired) {
                    subscriptionMetadata.ifPresent(sm -> deleteSubscription(sm, connection));
                    return;
                }
                Optional<String> loType = subscription.topicType() == Subscription.TopicType.LOGISTICS_OBJECT_TYPE ?
                        Optional.of(subscription.topic()) :
                        loTypes.stream().map(IRI::stringValue).findFirst();
                Notification notification = new Notification(
                        idProvider.createNotificationId(event.loId()).getIri(),
                        event.notificationEventType(),
                        loType,
                        Optional.of(event.loId()),
                        event.triggeredBy(),
                        event.changedProperties()
                );
                notificationRepository.persist(notification, connection);
                neoneEventRepository.addNotification(event.iri(), notification.iri(), connection);
                // 创建物流对象 createLogisticsObject 触发事件
                NotificationMetadata notificationMetadata = new NotificationMetadata(
                        idProvider.createInternalIri().getIri(),
                        notification.iri(),
                        subscriptionMetadata.orElseThrow(() -> new MissingMetadataException("Missing metadata for " + subscription.iri())).callbackUrl(),
                        subscription.sendLoBody().orElse(false),
                        event.iri(),
                        subscription.subscriber());
                notificationMetadataRepository.persist(notificationMetadata, connection);
            });
            // 3. 将事件设置为待处理状态
            neoneEventRepository.setEventState(event.iri(), NeoneEvent.State.PENDING, connection);
        });
    }


    /**
     * 发送通知
     * <p>
     * 定时任务，从配置文件读取间隔
     */
//    @Scheduled(
//            fixedRateString = "${notification.send-interval:60000}",
//            initialDelayString = "${notification.send-interval:60000}"
//    )
    @Scheduled(cron = "0/10 * * * * ?")
    public void sendNotifications() {
        log.info("获取所有未处理的通知");
        // 获取所有待发送的通知
        List<Notification> notifications = transaction
                .transactionallyGet(notificationRepository::getPendingNotifications);
        log.info("获取未处理的通知条数 : {}", notifications.size());
        notifications.forEach(notification -> transaction.transactionallyDo(connection -> {
            // enqueue放入队列中 notificationMetadataRepository.persist(notificationMetadata, connection);
            NotificationMetadata meta = notificationMetadataRepository.getMetadataOfSubject(notification.iri(), connection)
                    .orElse(null);
//                    .orElseThrow(() -> new MissingMetadataException("Missing metadata for " + notification.iri()));
            if (ObjectUtils.isEmpty(meta)) {
                log.warn("通知元数据为空 " + notification.iri());
                return;
            }
            Optional<LogisticsObject> lo = notification.hasLogisticsObject()
                    .flatMap(iri -> logisticsObjectRepository.findByIri(iri, connection));
            log.info("Sending [{}] to [{}] for event [{}]", notification, meta.callbackUrl(), meta.loEventIri());
            try {
                sendNotification(notification, meta, lo);
                log.debug("Notification sent successfully");
                // 如果成功则删除通知和回调
                neoneEventRepository.deleteNotification(meta.loEventIri(), notification.iri(), connection);
                notificationRepository.delete(notification.iri(), null, connection);
                notificationMetadataRepository.delete(meta.iri(), null, connection);
            } catch (Exception exc) {
                log.warn("Unable to send notification [{}] to [{}]",
                        notification.iri().stringValue(), meta.callbackUrl());
                log.debug("Exception while sending notification [{}] to [{}]", notification.iri().stringValue(), meta.callbackUrl(), exc);
            }

        }));
        cleanUp();
    }

    private void sendNotification(Notification notification, NotificationMetadata notificationMetadata, Optional<LogisticsObject> lo) {
        String callbackUrl = notificationMetadata.callbackUrl().toString();
        IRI company = notificationMetadata.companyId();
        if (ObjectUtils.isEmpty(company) || StringUtils.isBlank(company.getLocalName())) {
            return;
        }
        String companyId = company.getLocalName();
        HttpClientUtil httpClientUtil = SpringUtils.getBean("httpClientUtil");
        NeOneCompanyService neOneCompanyService = SpringUtils.getBean("neOneCompanyServiceImpl");
        // 查询公司信息
        LambdaQueryWrapper<NeOneServerCompanyHolder> schWrapper = new LambdaQueryWrapper<>();
        schWrapper.eq(NeOneServerCompanyHolder::getCompanyId, companyId);
        schWrapper.last("LIMIT 1");
        NeOneServerCompanyHolder serverCompanyHolder = neOneCompanyService.getOne(schWrapper);
        String notifyUrl = UrlFormatUtils.resolveUrl(serverCompanyHolder.getHost() + serverCompanyHolder.getCallbackUrl());
        String token = KeyLockTokenUtils.getCompanyToekn(serverCompanyHolder);
        try {
            NotificationMessage notificationMessage = new NotificationMessage()
                    .withNotification(notification)
                    .withLogisticsObject(lo.orElse(null));
            log.info("Sending notification to [{}]: {}", notificationMetadata.callbackUrl(), notification);
            String response = restClientBuilderProducer.sendNotification(
                    notifyUrl,
                    notificationMessage,
                    token,
                    config.getConnectTimeoutSeconds() * 1000L,
                    config.getReadTimeoutSeconds() * 1000L
            );
            log.info("Notification sent successfully. Response: {}", response);
        } catch (Exception e) {
            log.error("Failed to send notification to [{}]: {}", callbackUrl, e.getMessage(), e);
            throw e;
        }
    }

    private void cleanUp() {
        transaction.transactionallyDo(connection -> {
            List<IRI> events = neoneEventRepository.getPendingEventsWithoutNotifications(connection);
            events.forEach(eventId -> {
                if (ObjectUtils.isEmpty(eventId) || StringUtils.isBlank(eventId.stringValue())) {
                    return;
                }
                log.info("Event [{}] is in [PENDING] state but has no active notifications, setting it to [PROCESSED]", eventId.stringValue());
                neoneEventRepository.setEventState(eventId, NeoneEvent.State.PROCESSED, connection);

            });
        });
    }

    public List<String> getCompanyIdsToNotify(IRI logisticsObjectId) {
        List<NeOneSubscriptions> subscriptionsList = subscriptionsService.list();
        Set<String> companyIds = new HashSet<>();
        for (NeOneSubscriptions subscription : subscriptionsList) {
            String subscriptionCompanyUrl = subscription.getSubscriptionCompanyUrl();
            // 如果 subscriptionCompanyUrl 不为空，使用 IriUtils 提取最后变量
            if (StringUtils.isBlank(subscriptionCompanyUrl)) {
                continue;
            }
            String companyId = IriUtils.extractLastVariableFromUrl(subscriptionCompanyUrl);
            if (StringUtils.isNotBlank(companyId)) {
                companyIds.add(companyId);
            }
        }
        return new ArrayList<>(companyIds);
    }

    /**
     * 删除订阅
     *
     * @param subscriptionMeta 订阅元数据
     * @param connection       数据库连接
     */
    private void deleteSubscription(SubscriptionMetadata subscriptionMeta, RepositoryConnection connection) {
        // Removes the subscription as well:
        subscriptionMetadataRepository.deleteAll(subscriptionMeta.iri(), connection);
    }

    /**
     * 转发通知消息
     *
     * @param notification 通知消息
     */
    public void forward(NotificationMessage notification) {
        URI uri = URI.create(config.getClientUrl());
//        var client = restClientBuilderProducer.restClientBuilder()
//            .baseUri(uri)
//            .connectTimeout(config.getConnectTimeoutSeconds(), TimeUnit.SECONDS)
//            .readTimeout(config.getReadTimeoutSeconds(), TimeUnit.SECONDS)
//            .build(NotificationClient.class);
//        client.notify(notification);
    }

    public void invalidateCachedLogisticsObject(NotificationMessage notificationMsg) {
        Optional<IRI> loId = notificationMsg.getNotification().hasLogisticsObject();
        loId.ifPresent(this::invalidateCache);
    }

    // NOTE: Cache enabled function must not be private.
//    @CacheInvalidate(cacheName = "remote-lo-cache")
    void invalidateCache(@CacheKey IRI iri) {
        // No implementation required.
    }
}
