package com.efreight.base.module.one.record.neone.listener;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.efreight.base.api.feign.IStudioApiService;
import com.efreight.base.api.vo.NeoneNotifyEbaseVO;
import com.efreight.base.common.core.exception.OneRecordServerException;
import com.efreight.base.common.core.utils.SpringUtils;
import com.efreight.base.module.one.record.neone.model.entity.NeOneLogisticsObjects;
import com.efreight.base.module.one.record.neone.model.entity.NeOneServerCompanyHolder;
import com.efreight.base.module.one.record.neone.model.entity.NeOneSubscriptions;
import com.efreight.base.module.one.record.neone.service.NeOneCompanyService;
import com.efreight.base.module.one.record.neone.service.OneRecordResolvedLogisticsObjectService;
import com.efreight.base.module.one.record.neone.service.SubscriptionsService;
import com.efreight.base.module.one.record.neone.utils.HttpClientUtil;
import com.efreight.base.module.one.record.neone.utils.IriUtils;
import com.efreight.base.module.one.record.neone.utils.KeyLockTokenUtils;
import com.efreight.base.module.one.record.neone.utils.UrlFormatUtils;
import com.google.common.eventbus.Subscribe;
import com.jayway.jsonpath.JsonPath;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * 物流对象监听器 - 异步通知版本
 *
 * @author fu yuan hui
 * @since 2024-09-18 14:45:13 星期三
 */
@SuppressWarnings("unused")
@Slf4j
@RequiredArgsConstructor
public class LogisticsObjectsListener {

    private final OneRecordResolvedLogisticsObjectService resolvedDataService;

    private final IStudioApiService iStudioApiService;

    private String loCreateNotifyTemplate;

    /**
     * 异步通知线程池
     * 核心线程数：2
     * 最大线程数：5
     * 队列容量：20
     * 线程存活时间：30秒
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
                30L, // 空闲线程存活时间
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(20), // 队列容量
                new ThreadPoolExecutor.CallerRunsPolicy() // 拒绝策略：由调用线程执行
        );
        log.info(">>>>>>>>>>>>>>>>>>>>物流对象监听器初始化完成，线程池创建成功");
    }

    @PreDestroy
    public void destroy() {
        // 优雅关闭线程池
        if (notificationExecutor != null && !notificationExecutor.isShutdown()) {
            log.info(">>>>>>>>>>>>>>>>>>>>开始关闭物流对象监听器线程池...");
            notificationExecutor.shutdown();
            try {
                // 等待所有任务完成，最多等待30秒
                if (!notificationExecutor.awaitTermination(30, TimeUnit.SECONDS)) {
                    notificationExecutor.shutdownNow();
                    log.warn(">>>>>>>>>>>>>>>>>>>>线程池强制关闭");
                }
            } catch (InterruptedException e) {
                notificationExecutor.shutdownNow();
                Thread.currentThread().interrupt();
                log.error(">>>>>>>>>>>>>>>>>>>>线程池关闭时发生异常", e);
            }
            log.info(">>>>>>>>>>>>>>>>>>>>物流对象监听器线程池已关闭");
        }
    }

    @Subscribe
    public void listenerLogisticsObjectCreated(NeOneLogisticsObjects base) {

        List<NeOneSubscriptions> subscriptions = getSubscriptions(base);
        if (CollectionUtils.isEmpty(subscriptions)) {
            log.warn(">>>>>>>>>>>>>>>>>>>>没有订阅者, 当前物流对象无需发送通知 :{} ", base);
            return;
        }

        long startTime = System.currentTimeMillis();

        // 分离订阅者：有 ebaseFlowId 的和无 ebaseFlowId 的
        List<NeOneSubscriptions> ebaseSubscriptions = subscriptions.stream()
                .filter(bean -> ObjectUtils.isNotEmpty(bean.getEbaseFlowId()))
                .collect(Collectors.toList());

        List<NeOneSubscriptions> externalSubscriptions = subscriptions.stream()
                .filter(bean -> ObjectUtils.isEmpty(bean.getEbaseFlowId()))
                .collect(Collectors.toList());

        // 统一异步通知所有订阅者（ebase + 外部）
        String loIri = base.getIri();
        String contextType = base.getContextType();
        List<UnifiedNotifyResult> results = notifyAllSubscribersAsync(ebaseSubscriptions, externalSubscriptions, base, loIri, contextType);

        // 统计结果
        long totalSuccessCount = results.stream().filter(UnifiedNotifyResult::isSuccess).count();
        long totalFailCount = results.size() - totalSuccessCount;
        long ebaseSuccessCount = results.stream()
                .filter(r -> r.isEbase() && r.isSuccess())
                .count();
        long externalSuccessCount = results.stream()
                .filter(r -> !r.isEbase() && r.isSuccess())
                .count();

        long duration = System.currentTimeMillis() - startTime;

        log.info(">>>>>>>>>>>>>>>>>>>>物流对象通知完成, loId:{}, 总订阅数:{}, ebase订阅数:{}, 外部订阅数:{}, 成功:{}, 失败:{}, ebase订阅成功数:{}, 外部订阅成功数:{}, 耗时:{}ms",
                base.getLoId(), subscriptions.size(), ebaseSubscriptions.size(), externalSubscriptions.size(),
                totalSuccessCount, totalFailCount, ebaseSuccessCount, externalSuccessCount, duration);

        // 记录失败的订阅者
        List<UnifiedNotifyResult> failedResults = results.stream()
                .filter(r -> !r.isSuccess())
                .collect(Collectors.toList());

        if (!failedResults.isEmpty()) {
            log.error(">>>>>>>>>>>>>>>>>>>>以下订阅者通知失败:");
            for (UnifiedNotifyResult result : failedResults) {
                if (result.isEbase()) {
                    log.error("  - [ebase订阅] flowId: {}, 错误: {}", result.getIdentifier(), result.getErrorMsg());
                } else {
                    log.error("  - [外部订阅] 公司: {}, URL: {}, 错误: {}",
                            result.getCompanyName(), result.getNotifyUrl(), result.getErrorMsg());
                }
            }
        }
    }

    /**
     * 统一异步通知所有订阅者（ebase + 外部）
     *
     * @param ebaseSubscriptions    ebase 订阅者列表
     * @param externalSubscriptions 外部订阅者列表
     * @param lo                    物流对象
     * @param loIri                 物流对象 IRI
     * @param contextType           上下文类型
     * @return 统一的通知结果列表
     */
    private List<UnifiedNotifyResult> notifyAllSubscribersAsync(
            List<NeOneSubscriptions> ebaseSubscriptions,
            List<NeOneSubscriptions> externalSubscriptions,
            NeOneLogisticsObjects lo,
            String loIri,
            String contextType) {

        long startTime = System.currentTimeMillis();
        int totalSubscriptions = ebaseSubscriptions.size() + externalSubscriptions.size();
        log.info(">>>>>>>>>>>>>>>>>>>>开始异步通知订阅者, loId:{}, 总订阅数:{}, ebase订阅数:{}, 外部订阅数:{}",
                lo.getLoId(), totalSubscriptions, ebaseSubscriptions.size(), externalSubscriptions.size());

        // 创建异步任务列表
        List<CompletableFuture<UnifiedNotifyResult>> futures = new ArrayList<>();

        // 添加 ebase 订阅者的异步任务
        for (NeOneSubscriptions sub : ebaseSubscriptions) {
            CompletableFuture<UnifiedNotifyResult> future = CompletableFuture.supplyAsync(
                    () -> notifyEbaseSubscriber(lo, sub), notificationExecutor);
            futures.add(future);
        }

        // 添加外部订阅者的异步任务
        for (NeOneSubscriptions sub : externalSubscriptions) {
            CompletableFuture<UnifiedNotifyResult> future = CompletableFuture.supplyAsync(
                    () -> notifyExternalSubscriber(sub, lo, loIri, contextType), notificationExecutor);
            futures.add(future);
        }

        // 等待所有任务完成
        CompletableFuture<Void> allOf = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0]));

        try {
            // 设置超时时间为30秒
            allOf.get(30, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            log.error(">>>>>>>>>>>>>>>>>>>>异步通知超时, loId:{}, 超时时间:30秒", lo.getLoId());
        } catch (InterruptedException e) {
            log.error(">>>>>>>>>>>>>>>>>>>>异步通知被中断, loId:{}", lo.getLoId(), e);
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            log.error(">>>>>>>>>>>>>>>>>>>>异步通知执行异常, loId:{}", lo.getLoId(), e);
        }

        // 收集结果
        List<UnifiedNotifyResult> results = new ArrayList<>();
        for (CompletableFuture<UnifiedNotifyResult> future : futures) {
            try {
                if (future.isDone() && !future.isCompletedExceptionally()) {
                    results.add(future.get());
                } else {
                    // 任务异常或超时，创建失败结果
                    results.add(new UnifiedNotifyResult(true, "未知", null, false, "任务执行异常或超时"));
                }
            } catch (Exception e) {
                log.error(">>>>>>>>>>>>>>>>>>>>获取异步任务结果失败", e);
                results.add(new UnifiedNotifyResult(true, "未知", null, false, "获取结果失败: " + e.getMessage()));
            }
        }

        long duration = System.currentTimeMillis() - startTime;
        log.info(">>>>>>>>>>>>>>>>>>>>异步通知耗时: {}ms", duration);

        return results;
    }

    /**
     * 通知单个 ebase 订阅者
     *
     * @param lo  物流对象
     * @param sub 订阅者
     * @return 统一通知结果
     */
    private UnifiedNotifyResult notifyEbaseSubscriber(NeOneLogisticsObjects lo, NeOneSubscriptions sub) {
        String flowId = sub.getEbaseFlowId();
        String loId = lo.getLoId();

        try {
            log.debug(">>>>>>>>>>>>>>>>>>>>开始通知 ebase 订阅者, flowId:{}, loId:{}", flowId, loId);
            NeoneNotifyEbaseVO neoneNotifyEbaseVO = new NeoneNotifyEbaseVO();
            neoneNotifyEbaseVO.setFlowId(flowId);
            neoneNotifyEbaseVO.setContent(lo.getBodyText());
            iStudioApiService.neoneNotifyEbase(neoneNotifyEbaseVO);
            log.info(">>>>>>>>>>>>>>>>>>>>ebase 订阅者通知成功, flowId:{}, loId:{}", flowId, loId);
            return new UnifiedNotifyResult(true, flowId, null, true, null);
        } catch (Exception e) {
            log.error(">>>>>>>>>>>>>>>>>>>>ebase 订阅者通知失败, flowId:{}, loId:{}, 错误信息:{}",
                    flowId, loId, e.getMessage(), e);
            return new UnifiedNotifyResult(true, flowId, null, false, e.getMessage());
        }
    }


    /**
     * 获取订阅信息
     *
     * @param neOneLogisticsObjects 物流对象
     * @return 订阅者列表
     */
    private List<NeOneSubscriptions> getSubscriptions(NeOneLogisticsObjects neOneLogisticsObjects) {
        SubscriptionsService subscriptionsService = SpringUtils.getBean("subscriptionsServiceImpl");
        return subscriptionsService.getSubscriptions(neOneLogisticsObjects);
    }

    /**
     * 通知单个外部订阅者
     *
     * @param subscription 订阅信息
     * @param lo           物流对象
     * @param loIri        物流对象 IRI
     * @param contextType  上下文类型
     * @return 统一通知结果
     */
    private UnifiedNotifyResult notifyExternalSubscriber(
            NeOneSubscriptions subscription,
            NeOneLogisticsObjects lo,
            String loIri,
            String contextType) {

        // 使用 SpringUtils.getBean() 获取依赖，避免循环依赖
        HttpClientUtil httpClientUtil = SpringUtils.getBean("httpClientUtil");
        NeOneCompanyService neOneCompanyService = SpringUtils.getBean("neOneCompanyServiceImpl");

        String actionRequestIri = subscription.getActionRequestIri();
        String subscriptionCompanyUrl = subscription.getSubscriptionCompanyUrl();
        String companyId = IriUtils.extractLastVariableFromUrl(subscriptionCompanyUrl);

        // 参数校验：公司ID
        if (org.apache.commons.lang3.StringUtils.isBlank(companyId)) {
            log.warn(">>>>>>>>>>>>>>>>>发送LO对象外部通知 订阅信息公司id为空: {}", subscription);
            return new UnifiedNotifyResult(false, companyId, null, false, "订阅信息公司ID为空");
        }

        // 查询公司信息
        LambdaQueryWrapper<NeOneServerCompanyHolder> schWrapper = new LambdaQueryWrapper<>();
        schWrapper.eq(NeOneServerCompanyHolder::getCompanyId, companyId);
        NeOneServerCompanyHolder serverCompanyHolder = neOneCompanyService.getOne(schWrapper);

        if (ObjectUtils.isEmpty(serverCompanyHolder)) {
            log.warn(">>>>>>>>>>>>>>>>>发送LO对象外部通知 订阅信息公司为空 id: {}", companyId);
            return new UnifiedNotifyResult(false, companyId, null, false, "订阅信息公司不存在, companyId: " + companyId);
        }

        // 构建通知参数
        String notifyUrl = UrlFormatUtils.resolveUrl(serverCompanyHolder.getHost() + serverCompanyHolder.getCallbackUrl());
        String subscribeToken = KeyLockTokenUtils.getCompanyToekn(serverCompanyHolder);
        String bodyText = configNotifyBody(loIri, actionRequestIri, contextType);

        String companyName = serverCompanyHolder.getCompanyName();

        try {
            log.info(">>>>>>>>>>>>>>>>>>>>开始通知外部订阅者, 公司:{}, URL:{}", companyName, notifyUrl);
            sendNotifyExternal(httpClientUtil, subscribeToken, bodyText, notifyUrl);
            log.info(">>>>>>>>>>>>>>>>>>>>外部订阅者通知成功, 公司:{}, URL:{}", companyName, notifyUrl);
            return new UnifiedNotifyResult(false, companyId, companyName, true, null, notifyUrl);
        } catch (Exception e) {
            log.error(">>>>>>>>>>>>>>>>>>>>外部订阅者通知失败, 公司:{}, URL:{}, 错误:{}",
                    companyName, notifyUrl, e.getMessage(), e);
            return new UnifiedNotifyResult(false, companyId, companyName, false, e.getMessage(), notifyUrl);
        }
    }

    private void sendNotifyExternal(HttpClientUtil httpClientUtil, String subscribeToken, String bodyText, String notifyUrl) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/ld+json; version=2.1.0");
        headers.add("Accept", "application/ld+json; version=2.1.0");
        headers.add("Authorization", "Bearer " + subscribeToken);
        log.info(">>>>>>>>>>>>>>>>>>>>> 发送LO对象外部通知消息体打印: {}, 地址打印: {}, token打印: {}", bodyText, notifyUrl, subscribeToken);
        try {
            ResponseEntity<String> response = httpClientUtil.post(notifyUrl, headers, bodyText, String.class);
            log.info(">>>>>>>>>>>>>>>>>>>>> 发送LO对象外部通知 response 打印: {}, ", response);
            if (ObjectUtils.isEmpty(response)) {
                log.error(">>>>>>>>>>>>>>>>>>>>> 发送LO对象外部通知 response 为空!");
                throw new com.efreight.base.module.one.record.neone.ex.SubscribeNotifyException("发送LO对象外部通知 response 为空!");
            }
            log.info(">>>>>>>>>>>>>>>>>>>>> 发送LO对象外部通知 response body: {},response headers: {},response status: {}, ",
                    response.getBody(), response.getHeaders(), response.getStatusCode());
            if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
                log.info("----发送LO对象外部通知成功，请求结果：{}", com.alibaba.fastjson.JSON.toJSONString(response));
            } else {
                log.error("----发送LO对象外部通知失败，请求结果：{}", com.alibaba.fastjson.JSON.toJSONString(response));
                throw new com.efreight.base.module.one.record.neone.ex.SubscribeNotifyException("发送LO对象外部通知失败：" + (response.getBody() != null ? response.getBody() :
                        "错误码：" + response.getStatusCode() + ", 错误原因：" + response.getBody() + ", 请求的URL:" + notifyUrl));
            }
        } catch (Throwable e) {
            log.error("----发送LO对象外部通知失败，原因：{}", e.getMessage());
            throw new com.efreight.base.module.one.record.neone.ex.SubscribeNotifyException("发送LO对象外部通知失败：" + e.getMessage());
        }
    }

    private String configNotifyBody(String loIri, String actionRequestIri, String contextType) {
        return JsonPath.parse(this.loCreateNotifyTemplate)
                .set("$.api:hasLogisticsObject.@id", loIri)
                .set("$.api:hasLogisticsObjectType.@value", "https://onerecord.iata.org/ns/cargo#" + contextType)
                .set("$.api:isTriggeredBy.@id", actionRequestIri)
                .jsonString();
    }

    /**
     * 初始化模板
     */
    private void initTemplate() {
        try {
            this.loCreateNotifyTemplate = IOUtils.resourceToString("onerecord/logistics-object-create-notify.json", StandardCharsets.UTF_8, SubscriptionsService.class.getClassLoader());
        } catch (IOException e) {
            throw new OneRecordServerException("JSON 文件初始化失败", e);
        }
    }


    /**
     * 统一通知结果内部类 - 支持 ebase 订阅和外部订阅
     */
    private static class UnifiedNotifyResult {
        private final boolean isEbase;           // 是否为 ebase 订阅
        private final String identifier;          // ebase: flowId, 外部: companyId
        private final String companyName;        // 外部订阅的公司名称（ebase 为 null）
        private final boolean success;            // 是否成功
        private final String errorMsg;            // 错误信息
        private final String notifyUrl;           // 外部订阅的通知 URL（ebase 为 null）

        // ebase 订阅构造器
        public UnifiedNotifyResult(boolean isEbase, String identifier, String companyName,
                                   boolean success, String errorMsg) {
            this(isEbase, identifier, companyName, success, errorMsg, null);
        }

        // 外部订阅构造器
        public UnifiedNotifyResult(boolean isEbase, String identifier, String companyName,
                                   boolean success, String errorMsg, String notifyUrl) {
            this.isEbase = isEbase;
            this.identifier = identifier;
            this.companyName = companyName;
            this.success = success;
            this.errorMsg = errorMsg;
            this.notifyUrl = notifyUrl;
        }

        public boolean isEbase() {
            return isEbase;
        }

        public String getIdentifier() {
            return identifier;
        }

        public String getCompanyName() {
            return companyName;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getErrorMsg() {
            return errorMsg;
        }

        public String getNotifyUrl() {
            return notifyUrl;
        }
    }
}
