package com.efreight.base.module.one.record.neone.listener;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.efreight.base.common.core.exception.OneRecordServerException;
import com.efreight.base.common.core.utils.SpringUtils;
import com.efreight.base.module.one.record.neone.enums.NotifyEventType;
import com.efreight.base.module.one.record.neone.enums.TopicType;
import com.efreight.base.module.one.record.neone.ex.SubscribeNotifyException;
import com.efreight.base.module.one.record.neone.model.entity.NeOneLogisticsEvents;
import com.efreight.base.module.one.record.neone.model.entity.NeOneServerCompanyHolder;
import com.efreight.base.module.one.record.neone.model.entity.NeOneSubscriptions;
import com.efreight.base.module.one.record.neone.service.NeOneCompanyService;
import com.efreight.base.module.one.record.neone.service.SubscriptionsService;
import com.efreight.base.module.one.record.neone.utils.HttpClientUtil;
import com.efreight.base.module.one.record.neone.utils.IriUtils;
import com.efreight.base.module.one.record.neone.utils.KeyLockTokenUtils;
import com.efreight.base.module.one.record.neone.utils.UrlFormatUtils;
import com.google.common.eventbus.Subscribe;
import com.jayway.jsonpath.JsonPath;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author fu yuan hui
 * @since 2024-09-18 14:43:01 星期三
 */
@SuppressWarnings("unused")
@Slf4j
public class LogisticsEventsListener {

    private String loCreateNotifyTemplate;

    /**
     * 异步通知线程池
     * 核心线程数：5
     * 最大线程数：10
     * 队列容量：100
     * 线程存活时间：60秒
     * 拒绝策略：CallerRunsPolicy（保证任务不丢失）
     */
    private ExecutorService notificationExecutor;

    @PostConstruct
    public void init() {
        // 初始化模板
        initTemplate();

        // 初始化线程池
        notificationExecutor = new ThreadPoolExecutor(
                2, // 核心线程数
                5, // 最大线程数
                30L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(20),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
        log.info(">>>>>>>>>>>>>>>>>>>>物流事件监听器初始化完成，线程池创建成功");
    }

    @PreDestroy
    public void destroy() {
        // 优雅关闭线程池
        if (notificationExecutor != null && !notificationExecutor.isShutdown()) {
            log.info(">>>>>>>>>>>>>>>>>>>>开始关闭物流事件监听器线程池...");
            notificationExecutor.shutdown();
            try {
                if (!notificationExecutor.awaitTermination(30, TimeUnit.SECONDS)) {
                    notificationExecutor.shutdownNow();
                    log.warn(">>>>>>>>>>>>>>>>>>>>线程池强制关闭");
                }
            } catch (InterruptedException e) {
                notificationExecutor.shutdownNow();
                Thread.currentThread().interrupt();
                log.error(">>>>>>>>>>>>>>>>>>>>线程池关闭异常", e);
            }
            log.info(">>>>>>>>>>>>>>>>>>>>物流事件监听器线程池已关闭");
        }
    }

    @Subscribe
    public void listenerLogisticsObjectCreated(NeOneLogisticsEvents base) {
//        String bodyText = base.getBodyText();
//        NeOneResolvedData loRecord = ReverseResolveOneRecordBodyTools.parse(bodyText);
//        NeOneResolvedLogisticsEvents events = NeOneResolvedLogisticsEvents.create(loRecord);
//        events.setEventIri(base.getEventIri());
//        events.setLoId(base.getLoId());
//        events.setLoIri(base.getEventIri().substring(0, base.getEventIri().lastIndexOf("/logistics-events")));
//        events.setEventId(base.getEventId());
//        events.setLoModuleType(LoModuleType.LOGISTICS_EVENT.name());
//        this.logisticsEventsService.save(events);

        log.info("----需要通知处理的事件：{}----", JSON.toJSONString(base));
        List<NeOneSubscriptions> subscriptions = getSubscriptions(base);
        if (CollectionUtils.isEmpty(subscriptions)) {
            log.info(">>>>>>>>>>>>>>>>>>>>没有订阅者, 当前物流对象无需发送通知");
            return;
        }
        log.info(">>>>>>>> 事件 : {} ,收到订阅个数 ：{}<<<<<<<", base, subscriptions.size());
        // 报文内容基础信息
        String loIri = base.getLoIri();
        String eventIri = base.getEventIri();

        // 异步通知所有订阅者
        List<NotifyResult> results = notifySubscribersAsync(subscriptions, loIri, eventIri);

        // 统计结果
        long successCount = results.stream().filter(NotifyResult::isSuccess).count();
        long failCount = results.size() - successCount;
        log.info(">>>>>>>>>>>>>>>>>>>>物流事件通知完成, 总订阅数:{}, 成功:{}, 失败:{}",
                results.size(), successCount, failCount);
        // 记录失败的订阅者
        List<NotifyResult> failedResults = results.stream()
                .filter(r -> !r.isSuccess())
                .collect(Collectors.toList());
        if (!failedResults.isEmpty()) {
            log.error(">>>>>>>>>>>>>>>>>>>>以下订阅者通知失败:");
            for (NotifyResult result : failedResults) {
                log.error("  - 公司: {}, URL: {}, 错误: {}",
                        result.getCompanyName(), result.getNotifyUrl(), result.getErrorMsg());
            }
        }
    }

    /**
     * 异步通知所有订阅者
     *
     * @param subscriptions 订阅者列表
     * @param loIri         物流对象 IRI
     * @param eventIri      事件 IRI
     * @return 通知结果列表
     */
    private List<NotifyResult> notifySubscribersAsync(List<NeOneSubscriptions> subscriptions, String loIri, String eventIri) {
        long startTime = System.currentTimeMillis();
        log.info(">>>>>>>>>>>>>>>>>>>>开始异步通知订阅者, 订阅者数量:{}", subscriptions.size());

        // 创建异步任务列表
        List<CompletableFuture<NotifyResult>> futures = subscriptions.stream()
                .map(sub -> CompletableFuture.supplyAsync(
                        () -> notifySubscriber(sub, loIri, eventIri),
                        notificationExecutor))
                .collect(Collectors.toList());
        // 等待所有任务完成
        CompletableFuture<Void> allOf = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0]));
        try {
            // 设置超时时间为60秒
            allOf.get(60, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            log.error(">>>>>>>>>>>>>>>>>>>>异步通知超时, 超时时间:60秒");
        } catch (InterruptedException e) {
            log.error(">>>>>>>>>>>>>>>>>>>>异步通知被中断", e);
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            log.error(">>>>>>>>>>>>>>>>>>>>异步通知执行异常", e);
        }

        // 收集结果
        List<NotifyResult> results = new ArrayList<>();
        for (CompletableFuture<NotifyResult> future : futures) {
            try {
                if (future.isDone() && !future.isCompletedExceptionally()) {
                    results.add(future.get());
                } else {
                    // 任务异常或超时，创建失败结果
                    results.add(new NotifyResult("未知", "未知", false, "任务执行异常或超时"));
                }
            } catch (Exception e) {
                log.error(">>>>>>>>>>>>>>>>>>>>获取异步任务结果失败", e);
                results.add(new NotifyResult("未知", "未知", false, "获取结果失败: " + e.getMessage()));
            }
        }

        long duration = System.currentTimeMillis() - startTime;
        log.info(">>>>>>>>>>>>>>>>>>>>异步通知耗时: {}ms", duration);

        return results;
    }

    /**
     * 通知单个订阅者
     *
     * @param subscription 订阅信息
     * @param loIri        物流对象 IRI
     * @param eventIri     事件 IRI
     * @return 通知结果
     */
    private NotifyResult notifySubscriber(NeOneSubscriptions subscription, String loIri, String eventIri) {
        HttpClientUtil httpClientUtil = SpringUtils.getBean("httpClientUtil");
        NeOneCompanyService neOneCompanyService = SpringUtils.getBean("neOneCompanyServiceImpl");

        String subscriptionCompanyUrl = subscription.getSubscriptionCompanyUrl();
        String companyId = IriUtils.extractLastVariableFromUrl(subscriptionCompanyUrl);

        // 参数校验：公司ID
        if (StringUtils.isBlank(companyId)) {
            log.warn(">>>>>>>>>>>>>>>>>发送事件通知 订阅信息公司id为空: {}", subscription);
            return new NotifyResult("未知", "未知", false, "订阅信息公司ID为空");
        }

        // 查询公司信息
        LambdaQueryWrapper<NeOneServerCompanyHolder> schWrapper = new LambdaQueryWrapper<>();
        schWrapper.eq(NeOneServerCompanyHolder::getCompanyId, companyId);
        NeOneServerCompanyHolder serverCompanyHolder = neOneCompanyService.getOne(schWrapper);

        if (ObjectUtils.isEmpty(serverCompanyHolder)) {
            log.warn(">>>>>>>>>>>>>>>>>发送事件通知 订阅信息公司为空 id: {}", companyId);
            return new NotifyResult("未知", "未知", false, "订阅信息公司不存在, companyId: " + companyId);
        }

        // 构建通知参数
        String notifyUrl = UrlFormatUtils.resolveUrl(serverCompanyHolder.getHost() + serverCompanyHolder.getCallbackUrl());
        String subscribeToken = KeyLockTokenUtils.getCompanyToekn(serverCompanyHolder);
        String bodyText = configNotifyBody(loIri, eventIri);

        String companyName = serverCompanyHolder.getCompanyName();

        try {
            log.info(">>>>>>>>>>>>>>>>>>>>开始通知订阅者, 公司:{}, URL:{}", companyName, notifyUrl);
            sendNotify(httpClientUtil, subscribeToken, bodyText, notifyUrl);
            log.info(">>>>>>>>>>>>>>>>>>>>订阅者通知成功, 公司:{}, URL:{}", companyName, notifyUrl);
            return new NotifyResult(companyName, notifyUrl, true, null);
        } catch (Exception e) {
            log.error(">>>>>>>>>>>>>>>>>>>>订阅者通知失败, 公司:{}, URL:{}, 错误:{}",
                    companyName, notifyUrl, e.getMessage(), e);
            return new NotifyResult(companyName, notifyUrl, false, e.getMessage());
        }
    }

    private void sendNotify(HttpClientUtil httpClientUtil, String subscribeToken, String bodyText, String notifyUrl) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/ld+json; version=2.1.0");
        headers.add("Accept", "application/ld+json; version=2.1.0");
        headers.add("Authorization", "Bearer " + subscribeToken);
        log.info(">>>>>>>>>>>>>>>>>>>>> 发送事件通知消息体打印: {}, 地址打印: {}, token打印: {}", bodyText, notifyUrl, subscribeToken);
        try {
            ResponseEntity<String> response = httpClientUtil.post(notifyUrl, headers, bodyText, String.class);
            log.info(">>>>>>>>>>>>>>>>>>>>> 发送事件通知 response 打印: {}, ", response);
            if (ObjectUtils.isEmpty(response)) {
                log.error(">>>>>>>>>>>>>>>>>>>>> 发送事件通知 response 为空!");
                throw new SubscribeNotifyException("发送事件通知 response 为空!");
            }
            log.info(">>>>>>>>>>>>>>>>>>>>> 发送事件通知 response body: {},response headers: {},response status: {}, ",
                    response.getBody(), response.getHeaders(), response.getStatusCode());
            if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
                log.info("----发送事件通知成功，请求结果：{}", JSON.toJSONString(response));
            } else {
                log.error("----发送事件通知失败，请求结果：{}", JSON.toJSONString(response));
                throw new SubscribeNotifyException("发送事件通知失败：" + response.getBody() != null ? response.getBody() :
                        "错误码：" + response.getStatusCode() + ", 错误原因：" + response.getBody() + ", 请求的URL:" + notifyUrl);
            }
        } catch (Throwable e) {
            log.error("----发送事件通知失败，原因：{}", e.getMessage());
            throw new SubscribeNotifyException("发送事件通知失败：" + e.getMessage());
        }
    }

    private String configNotifyBody(String loIir, String eventIri) {
        return JsonPath.parse(this.loCreateNotifyTemplate)
                .set("$.api:hasLogisticsObject.@id", loIir)
                .set("$.api:hasLogisticsEvent.@id", eventIri)
                .jsonString();
    }

    /**
     * 初始化模板
     */
    private void initTemplate() {
        try {
            this.loCreateNotifyTemplate = IOUtils.resourceToString("onerecord/logistics-events-create-notify.json",
                    StandardCharsets.UTF_8, SubscriptionsService.class.getClassLoader());
        } catch (IOException e) {
            throw new OneRecordServerException("JSON 文件初始化失败", e);
        }
    }

    /**
     * 获取订阅信息
     *
     * @return
     */
    private List<NeOneSubscriptions> getSubscriptions(NeOneLogisticsEvents neOneLogisticsObjectsEvent) {
        String bodyText = neOneLogisticsObjectsEvent.getBodyText();
        SubscriptionsService subscriptionsService = SpringUtils.getBean("subscriptionsServiceImpl");
        List<NeOneSubscriptions> subscriptions = subscriptionsService.list();
        if (CollectionUtils.isEmpty(subscriptions)) {
            log.info(">>>>>>>>>>>>>>>>>>>>没有订阅者, 当前物流对象无需发送通知");
            return null;
        }
        subscriptions = subscriptions.stream().filter(sub -> {
            String includeSubscriptionType = sub.getIncludeSubscriptionType();
            if (StringUtils.isBlank(includeSubscriptionType)) {
                return false;
            }
            return includeSubscriptionType.contains(NotifyEventType.LOGISTICS_EVENT_RECEIVED.name());
        }).collect(Collectors.toList());

        Map<String, List<NeOneSubscriptions>> map = subscriptions.stream().collect(Collectors.groupingBy(NeOneSubscriptions::getSubType));
        List<NeOneSubscriptions> type = map.get(TopicType.LOGISTICS_OBJECT_TYPE.name());
        // TODO 后面完善  暂只支持按类型订阅
        List<NeOneSubscriptions> identifier = map.get(TopicType.LOGISTICS_OBJECT_IDENTIFIER.name());
        List<NeOneSubscriptions> number = map.get(TopicType.LOGISTICS_OBJECT_LO_NUMBER.name());
        return type;
    }

    /**
     * 通知结果内部类
     */
    @Data
    private static class NotifyResult {
        private final String companyName;
        private final String notifyUrl;
        private final boolean success;
        private final String errorMsg;

        public NotifyResult(String companyName, String notifyUrl, boolean success, String errorMsg) {
            this.companyName = companyName;
            this.notifyUrl = notifyUrl;
            this.success = success;
            this.errorMsg = errorMsg;
        }
    }
}
