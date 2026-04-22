package com.efreight.base.module.one.record.neone.notify;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.efreight.base.common.core.exception.OneRecordServerException;
import com.efreight.base.common.core.utils.SpringUtils;
import com.efreight.base.common.core.utils.StringUtils;
import com.efreight.base.module.one.record.neone.enums.NotifyEventType;
import com.efreight.base.module.one.record.neone.enums.TopicType;
import com.efreight.base.module.one.record.neone.ex.SubscribeNotifyException;
import com.efreight.base.module.one.record.neone.model.entity.NeOneLogisticsObjects;
import com.efreight.base.module.one.record.neone.model.entity.NeOneServerCompanyHolder;
import com.efreight.base.module.one.record.neone.model.entity.NeOneSubscriptions;
import com.efreight.base.module.one.record.neone.service.NeOneCompanyService;
import com.efreight.base.module.one.record.neone.service.SubscriptionsService;
import com.efreight.base.module.one.record.neone.service.impl.SubscriptionsServiceImpl;
import com.efreight.base.module.one.record.neone.utils.HttpClientUtil;
import com.efreight.base.module.one.record.neone.utils.IriUtils;
import com.efreight.base.module.one.record.neone.utils.KeyLockTokenUtils;
import com.efreight.base.module.one.record.neone.utils.UrlFormatUtils;
import com.google.common.collect.ImmutableSet;
import com.jayway.jsonpath.JsonPath;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author fu yuan hui
 * @since 2024-09-11 13:42:33 星期三
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LogisticsObjectNotifyHandler extends AbstractNotifyHandler<NeOneLogisticsObjects> {

    private final HttpClientUtil httpClientUtil;

    private final Set<NotifyEventType> notifyEventTypes = ImmutableSet.of(NotifyEventType.LOGISTICS_OBJECT_CREATED, NotifyEventType.LOGISTICS_OBJECT_UPDATED);

    private String loCreateNotifyTemplate;

    /**
     * 异步通知线程池
     * 核心线程数：2
     * 最大线程数：5
     * 队列容量：100
     * 线程存活时间：60秒
     * 拒绝策略：CallerRunsPolicy（保证任务不丢失）
     */
    private ExecutorService notifyExecutor;

    @PostConstruct
    public void init() {
        // 初始化模板
        initTemplate();

        // 初始化线程池
        notifyExecutor = new ThreadPoolExecutor(
                2, // 核心线程数
                5, // 最大线程数
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(100),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
        log.info(">>>>>>>>>>>>>>>>>>>>物流对象通知处理器初始化完成，线程池创建成功");
    }

    @PreDestroy
    public void destroy() {
        // 优雅关闭线程池
        if (notifyExecutor != null && !notifyExecutor.isShutdown()) {
            log.info(">>>>>>>>>>>>>>>>>>>>开始关闭物流对象通知处理器线程池...");
            notifyExecutor.shutdown();
            try {
                if (!notifyExecutor.awaitTermination(30, TimeUnit.SECONDS)) {
                    notifyExecutor.shutdownNow();
                    log.warn(">>>>>>>>>>>>>>>>>>>>线程池强制关闭");
                }
            } catch (InterruptedException e) {
                notifyExecutor.shutdownNow();
                Thread.currentThread().interrupt();
                log.error(">>>>>>>>>>>>>>>>>>>>线程池关闭异常", e);
            }
            log.info(">>>>>>>>>>>>>>>>>>>>物流对象通知处理器线程池已关闭");
        }
    }

    @Override
    protected boolean isSupport(NotifyEventType eventType) {
        return notifyEventTypes.contains(eventType);
    }

    @Override
    protected void doNotify(NeOneLogisticsObjects lo) {

        notifyLogisticsObjectCreated(lo);

    }

    @SuppressWarnings("all")
    private void notifyLogisticsObjectCreated(NeOneLogisticsObjects lo) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>> 准备发送物流对象 '{}' 的事件通知.", lo.getNotifyEventType().name());

        /**
         * @see TopicType.LoType
         */
        String contextType = lo.getContextType();
        List<NeOneSubscriptions> subscriptions = getSubscriptions(lo);
        if (CollectionUtils.isEmpty(subscriptions)) {
            log.info(">>>>>>>>>>>>>>>>>>>>没有订阅者, 当前物流对象无需发送通知. 物流对象id: {}", lo.getIri());
            return;
        }

        log.info(">>>>>>>> 物流对象事件 : {} , 收到订阅个数：{}<<<<<<<", lo.getNotifyEventType().name(), subscriptions.size());

        // 报文内容基础信息
        String loIri = lo.getIri();

        // 异步通知所有订阅者
        List<NotifyResult> results = notifySubscribersAsync(subscriptions, lo, loIri, contextType);

        // 统计结果
        long successCount = results.stream().filter(NotifyResult::isSuccess).count();
        long failCount = results.size() - successCount;

        log.info(">>>>>>>>>>>>>>>>>>>>物流对象通知完成, 总订阅数:{}, 成功:{}, 失败:{}",
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
     * @param lo 物流对象
     * @param loIri 物流对象 IRI
     * @param contextType 上下文类型
     * @return 通知结果列表
     */
    private List<NotifyResult> notifySubscribersAsync(
            List<NeOneSubscriptions> subscriptions,
            NeOneLogisticsObjects lo,
            String loIri,
            String contextType) {

        long startTime = System.currentTimeMillis();
        log.info(">>>>>>>>>>>>>>>>>>>>开始异步通知订阅者, 订阅者数量:{}", subscriptions.size());

        // 创建异步任务列表
        List<CompletableFuture<NotifyResult>> futures = subscriptions.stream()
                .map(sub -> CompletableFuture.supplyAsync(
                        () -> notifySubscriber(sub, lo, loIri, contextType),
                        notifyExecutor))
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
     * @param lo 物流对象
     * @param loIri 物流对象 IRI
     * @param contextType 上下文类型
     * @return 通知结果
     */
    private NotifyResult notifySubscriber(
            NeOneSubscriptions subscription,
            NeOneLogisticsObjects lo,
            String loIri,
            String contextType) {

        String actionRequestIri = subscription.getActionRequestIri();
        String subscriptionCompanyUrl = subscription.getSubscriptionCompanyUrl();
        String companyId = IriUtils.extractLastVariableFromUrl(subscriptionCompanyUrl);

        // 参数校验：公司ID
        if (org.apache.commons.lang3.StringUtils.isBlank(companyId)) {
            log.warn(">>>>>>>>>>>>>>>>>发送LO对象通知 订阅信息公司id为空: {}", subscription);
            return new NotifyResult("未知", "未知", false, "订阅信息公司ID为空");
        }

        // 查询公司信息
        NeOneCompanyService neOneCompanyService = SpringUtils.getBean("neOneCompanyServiceImpl");
        LambdaQueryWrapper<NeOneServerCompanyHolder> schWrapper = new LambdaQueryWrapper<>();
        schWrapper.eq(NeOneServerCompanyHolder::getCompanyId, companyId);
        NeOneServerCompanyHolder serverCompanyHolder = neOneCompanyService.getOne(schWrapper);

        if (ObjectUtils.isEmpty(serverCompanyHolder)) {
            log.warn(">>>>>>>>>>>>>>>>>发送LO对象通知 订阅信息公司为空 id: {}", companyId);
            return new NotifyResult("未知", "未知", false, "订阅信息公司不存在, companyId: " + companyId);
        }

        // 构建通知参数
        String notifyUrl = UrlFormatUtils.resolveUrl(serverCompanyHolder.getHost() + serverCompanyHolder.getCallbackUrl());
        String subscribeToken = KeyLockTokenUtils.getCompanyToekn(serverCompanyHolder);
        String bodyText = configNotifyBody(loIri, actionRequestIri, contextType);

        String companyName = serverCompanyHolder.getCompanyName();

        try {
            log.info(">>>>>>>>>>>>>>>>>>>>开始通知订阅者, 公司:{}, URL:{}", companyName, notifyUrl);
            sendNotify(subscribeToken, bodyText, notifyUrl);
            log.info(">>>>>>>>>>>>>>>>>>>>订阅者通知成功, 公司:{}, URL:{}", companyName, notifyUrl);
            return new NotifyResult(companyName, notifyUrl, true, null);
        } catch (Exception e) {
            log.error(">>>>>>>>>>>>>>>>>>>>订阅者通知失败, 公司:{}, URL:{}, 错误:{}",
                    companyName, notifyUrl, e.getMessage(), e);
            return new NotifyResult(companyName, notifyUrl, false, e.getMessage());
        }
    }

    private void sendNotify(String subscribeToken, String bodyText, String notifyUrl) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/ld+json; version=2.1.0");
        headers.add("Accept", "application/ld+json; version=2.1.0");
        headers.add("Authorization", "Bearer " + subscribeToken);
        log.info(">>>>>>>>>>>>>>>>>>>>> 发送LO对象通知消息体打印: {}, 地址打印: {}, token打印: {}", bodyText, notifyUrl, subscribeToken);
        try {
            ResponseEntity<String> response = httpClientUtil.post(notifyUrl, headers, bodyText, String.class);
            log.info(">>>>>>>>>>>>>>>>>>>>> 发送LO对象通知 response 打印: {}, ", response);
            if (ObjectUtils.isEmpty(response)) {
                log.error(">>>>>>>>>>>>>>>>>>>>> 发送LO对象通知 response 为空!");
                throw new SubscribeNotifyException("发送LO对象通知 response 为空!");
            }
            log.info(">>>>>>>>>>>>>>>>>>>>> 发送LO对象通知 response body: {},response headers: {},response status: {}, ",
                    response.getBody(), response.getHeaders(), response.getStatusCode());
            if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
                log.info("----发送LO对象通知成功，请求结果：{}", com.alibaba.fastjson.JSON.toJSONString(response));
            } else {
                log.error("----发送LO对象通知失败，请求结果：{}", com.alibaba.fastjson.JSON.toJSONString(response));
                throw new SubscribeNotifyException("发送LO对象通知失败：" + response.getBody() != null ? response.getBody() :
                        "错误码：" + response.getStatusCode() + ", 错误原因：" + response.getBody() + ", 请求的URL:" + notifyUrl);
            }
        } catch (Throwable e) {
            log.error("----发送LO对象通知失败，原因：{}", e.getMessage());
            throw new SubscribeNotifyException("发送LO对象通知失败：" + e.getMessage());
        }
    }

    /**
     * 获取订阅信息
     *
     * @return
     */
    private List<NeOneSubscriptions> getSubscriptions(NeOneLogisticsObjects neOneLogisticsObjects) {
        SubscriptionsService subscriptionsService = SpringUtils.getBean("subscriptionsServiceImpl");
        return subscriptionsService.getSubscriptions(neOneLogisticsObjects);
    }


    private String configNotifyBody(String loIir, String actionRequestIri, String contextType) {
        return JsonPath.parse(this.loCreateNotifyTemplate)
                .set("$.api:hasLogisticsObject.@id", loIir)
                .set("$.api:hasLogisticsObjectType.@value", "https://onerecord.iata.org/ns/cargo#" + contextType)
                .set("$.api:isTriggeredBy.@id", actionRequestIri)
                .jsonString();
    }

    /**
     * 初始化模板
     */
    private void initTemplate() {
        try {
            this.loCreateNotifyTemplate = IOUtils.resourceToString("onerecord/logistics-object-create-notify.json", StandardCharsets.UTF_8, SubscriptionsServiceImpl.class.getClassLoader());
        } catch (IOException e) {
            throw new OneRecordServerException("JSON 文件初始化失败", e);
        }
    }

    /**
     * CAAC 转换工具包：对于FWB, FHL, FFR, FSU 直接从报文中获取到主单号
     * <a href="https://efreight.feishu.cn/sheets/FvI5sjcmlhwuVntyr3xcphwgnCh?sheet=m0nxwG">报文</a>
     *
     * @param bodyText
     */
    private String resolveLoNumberFromLoBody(String bodyText) {
        String prefix = JsonPath.using(CONF).parse(bodyText).read("$.waybillPrefix");
        if (StringUtils.isNotBlank(prefix)) {
            String number = JsonPath.using(CONF).parse(bodyText).read("$.waybillNumber");
            return prefix + "-" + number;
        }
        String fsuAwbNumberPrefix = JsonPath.using(CONF).parse(bodyText).read("$.eventFor.waybillPrefix");
        return fsuAwbNumberPrefix + "-" + JsonPath.using(CONF).parse(bodyText).read("$.eventFor.waybillNumber");
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
