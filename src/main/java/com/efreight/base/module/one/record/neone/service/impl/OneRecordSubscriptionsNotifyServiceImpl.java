package com.efreight.base.module.one.record.neone.service.impl;

import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.efreight.base.common.core.exception.OneRecordServerException;
import com.efreight.base.common.core.model.Result;
import com.efreight.base.module.one.record.neone.controller.NeOnePingController;
import com.efreight.base.module.one.record.neone.enums.SubscribeStatus;
import com.efreight.base.module.one.record.neone.enums.SubscribeTypes;
import com.efreight.base.module.one.record.neone.helper.NeOneServerRequestHelper;
import com.efreight.base.module.one.record.neone.mapper.OneRecordSubscriptionsNotifyMapper;
import com.efreight.base.module.one.record.neone.model.entity.NeOneServerCompanyHolder;
import com.efreight.base.module.one.record.neone.model.entity.NeOneSubscriptionRequest;
import com.efreight.base.module.one.record.neone.model.request.SubscriptionRequestBody;
import com.efreight.base.module.one.record.neone.model.vo.SubscriptionRequestVo;
import com.efreight.base.module.one.record.neone.model.vo.SubscriptionResponseVo;
import com.efreight.base.module.one.record.neone.service.NeOneCompanyService;
import com.efreight.base.module.one.record.neone.service.OneRecordSubscriptionsNotifyService;
import com.efreight.base.module.one.record.neone.utils.KeyLockTokenUtils;
import com.efreight.base.module.one.record.neone.utils.UrlFormatUtils;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author fu yuan hui
 * @since 2024-10-09 13:33:45 星期三
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class OneRecordSubscriptionsNotifyServiceImpl extends ServiceImpl<OneRecordSubscriptionsNotifyMapper, NeOneSubscriptionRequest> implements OneRecordSubscriptionsNotifyService, InitializingBean {

    private String subscribeJson;
    private static final Configuration CONFIG = Configuration.defaultConfiguration().addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);
    private final NeOneCompanyService companyService;
    private final NeOneServerRequestHelper requestHelper;


    @Override
    public boolean subscribe(SubscriptionRequestBody request) {
        LambdaQueryWrapper<NeOneServerCompanyHolder> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(NeOneServerCompanyHolder::getCompanyId, request.getTargetCompanyId());
        NeOneServerCompanyHolder companyHolder = companyService.getOne(wrapper);
        String subscribeUrl = companyHolder.getSubscribeUrl();
        String targetHost = companyHolder.getHost();
        String requestJsonBody = resolveSubscriptionsJsonBody(request, targetHost);
        String cause = null;
        SubscriptionResponseVo responseVo = new SubscriptionResponseVo();
        // 公司token
        String subscribeToken = KeyLockTokenUtils.getCompanyToekn(companyHolder);
        try {
            responseVo = this.requestHelper.subscribe(targetHost, subscribeUrl, requestJsonBody, subscribeToken);
        } catch (Exception e) {
            log.error(">>>>>>>>>>>>>>>>>>>>> 请求订阅失败", e);
            cause = e.getMessage();
        }
        NeOneSubscriptionRequest neOneSubscriptionRequest = new NeOneSubscriptionRequest(request);
        neOneSubscriptionRequest.setSubscribeJsonBody(requestJsonBody);
        neOneSubscriptionRequest.setSubscribeStatus(responseVo.isStatus() ? SubscribeStatus.SUBSCRIBE_OK.name() : SubscribeStatus.SUBSCRIBE_FAIL.name());
        neOneSubscriptionRequest.setSubscribeFailCause(cause);
        neOneSubscriptionRequest.setTargetHost(targetHost);
        neOneSubscriptionRequest.setActionRequestIri(responseVo.getLocationUri());
        save(neOneSubscriptionRequest);
        return responseVo.isStatus();
    }

    @Override
    public boolean updateSubscribe(SubscriptionRequestBody request) {
        NeOneServerCompanyHolder companyHolder = companyService.getById(request.getTargetCompanyId());
        String targetHost = companyHolder.getHost();
        String subscribeUrl = companyHolder.getSubscribeUrl();
        targetHost = UrlFormatUtils.resolveUrl(targetHost + subscribeUrl);
        String requestJsonBody = resolveSubscriptionsJsonBody(request, targetHost);
        SubscriptionResponseVo responseVo = new SubscriptionResponseVo();
        String cause = null;
        String subscribeToken = KeyLockTokenUtils.getCompanyToekn(companyHolder);
        try {
            responseVo = this.requestHelper.updateSubscribe(targetHost, requestJsonBody, subscribeToken);
        } catch (Exception e) {
            log.error(">>>>>>>>>>>>>>>>>>>>> 订阅更新失败", e);
            cause = e.getMessage();
        }
        NeOneSubscriptionRequest neOneSubscriptionRequest = getById(request.getId());
        neOneSubscriptionRequest.setAwbNumbers(request.getAwbNumbers());
        neOneSubscriptionRequest.setFlightNumbers(request.getFlightNumbers());
        neOneSubscriptionRequest.setFsuCodes(request.getFsuCodes());
        neOneSubscriptionRequest.setSubscribeFhl(request.isSubscribeFhl());
        neOneSubscriptionRequest.setDepartureCodes(request.getDepartureCodes());
        neOneSubscriptionRequest.setArrivalCodes(request.getArrivalCodes());
        neOneSubscriptionRequest.setSubscribeType(neOneSubscriptionRequest.getSubscribeType());
        neOneSubscriptionRequest.setSubscribeFailCause(cause);
        neOneSubscriptionRequest.setSubscribeJsonBody(requestJsonBody);
        neOneSubscriptionRequest.setSubscribeStatus(responseVo.isStatus() ? SubscribeStatus.SUBSCRIBE_OK.name() : SubscribeStatus.SUBSCRIBE_UPDATE_FAIL.name());
        neOneSubscriptionRequest.setUpdateTime(LocalDateTime.now());
        updateById(neOneSubscriptionRequest);
        return responseVo.isStatus();

    }

    @Override
    public Result<?> unSubscribe(Long id) {
        NeOneSubscriptionRequest request = getById(id);
        if (SubscribeStatus.UNSUBSCRIBE.name().equals(request.getSubscribeStatus())) {
            return Result.fail("已经取消了订阅，无需重复取消");
        }
        NeOneServerCompanyHolder companyHolder = companyService.getById(request.getTargetCompanyId());
        String targetHost = companyHolder.getHost();
        NeOneServerCompanyHolder localServer = companyService.getLocalServer();
        String host = localServer.getHost();
        boolean unsubscribeStatus = false;
        String cause = null;
        try {
            unsubscribeStatus = this.requestHelper.unsubscribe(targetHost, host);
        } catch (Exception e) {
            log.error(">>>>>>>>>>>>>>>> 取消订阅失败", e);
            cause = e.getMessage();
        }
        request.setSubscribeStatus(unsubscribeStatus ? SubscribeStatus.UNSUBSCRIBE.name() : SubscribeStatus.UNSUBSCRIBE_FAIL.name());
        request.setSubscribeFailCause(cause);
        request.setUpdateTime(LocalDateTime.now());
        updateById(request);
        if (unsubscribeStatus) {
            return Result.ok("取消订阅成功");
        }
        return Result.fail(cause);
    }

    @Override
    public IPage<?> pageList(SubscriptionRequestVo req) {
        LambdaQueryWrapper<NeOneSubscriptionRequest> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StringUtils.isNotBlank(req.getHost()), NeOneSubscriptionRequest::getTargetHost, req.getHost());
        wrapper.like(StringUtils.isNotBlank(req.getSubscribeType()), NeOneSubscriptionRequest::getSubscribeType, req.getSubscribeType());

        if (StringUtils.isNotBlank(req.getCompanyName())) {
            List<NeOneServerCompanyHolder> neOneServerCompanyHolders = this.companyService.likeByName(req.getCompanyName());
            if (CollectionUtils.isNotEmpty(neOneServerCompanyHolders)) {
                wrapper.in(NeOneSubscriptionRequest::getTargetCompanyId, neOneServerCompanyHolders.stream().map(NeOneServerCompanyHolder::getId).collect(Collectors.toList()));
            }
        }
        wrapper.like(StringUtils.isNotBlank(req.getAwbNumbers()), NeOneSubscriptionRequest::getAwbNumbers, req.getAwbNumbers());
        wrapper.like(StringUtils.isNotBlank(req.getFlightNumbers()), NeOneSubscriptionRequest::getFlightNumbers, req.getFlightNumbers());
        wrapper.orderByDesc(NeOneSubscriptionRequest::getCreateTime);
        wrapper.select(NeOneSubscriptionRequest.class, h -> !"subscribe_json_body".equals(h.getColumn()) && !"subscribe_fail_cause".equals(h.getColumn()));

        return page(new Page<>(req.getCurrent(), req.getSize()), wrapper).convert(x -> {
            SubscriptionRequestVo vo = new SubscriptionRequestVo();
            BeanUtils.copyProperties(x, vo);
            vo.setSubscribeStatus(SubscribeStatus.valueOf(x.getSubscribeStatus()).getDesc());
            if (null != x.getTargetCompanyId()) {
                NeOneServerCompanyHolder holder = this.companyService.getById(x.getTargetCompanyId());
                vo.setCompanyName(holder != null ? holder.getCompanyName() : null);
            }
            vo.setHost(x.getTargetHost());
            return vo;
        });
    }

    @Override
    public String getFailCause(Long id) {
        NeOneSubscriptionRequest request = getById(id);
        return request.getSubscribeFailCause();
    }

    @Override
    public Object neOneDetails(Long id) {
        NeOneSubscriptionRequest request = getById(id);
        if (request != null) {
            return JsonPath.using(CONFIG).parse(request.getSubscribeJsonBody()).json();
        }
        return "无数据详情";
    }

    @Override
    public Object getDetails(Long id) {
        NeOneSubscriptionRequest request = getById(id);
        if (ObjectUtils.isEmpty(request)) {
            return null;
        }
        return request;
    }

    @Override
    public Result<?> removeSubscribe(Long id) {
        removeById(id);
        return Result.ok("删除成功");
//        this.unSubscribe(id);
//        NeOneSubscriptionRequest request = getById(id);
//        if (SubscribeStatus.UNSUBSCRIBE.name().equals(request.getSubscribeStatus())) {
//            removeById(id);
//            return Result.ok("删除成功");
//        }
//        return Result.fail(StringUtils.isNotBlank(request.getSubscribeFailCause()) ? request.getSubscribeFailCause() : "删除失败:由于无法和订阅的server先进行订阅取消");
    }

    private String resolveSubscriptionsJsonBody(SubscriptionRequestBody request, String targetHost) {
        String subscribeType = request.getSubscribeType();
        String loType = SubscribeTypes.match(subscribeType);
        String awbNumber = request.getAwbNumbers();
        String flightNumbers = request.getFlightNumbers();
//        if (StringUtils.isAllBlank(awbNumber, flightNumbers)) {
//            throw new EftException("主单号，航班号不能全部为空");
//        }
        NeOneServerCompanyHolder localServer = companyService.getLocalServerNoCache();
        String subscribeHost = localServer.getHost();
        DocumentContext dc = JsonPath.using(CONFIG).parse(subscribeJson)
//                .set("$.@context.publisher", targetHost)
//                .set("$.@context.subscriber", subscribeHost)
                .set("$.api:hasSubscriber.@id", localServer.getCompanyIri());
//                .set("$.api:hasSubscriptionLogisticsObjectType", request.getSubscribeType().toUpperCase())
//                .set("$.api:hasSubscriptionFhl", request.isSubscribeFhl());


        if (StringUtils.isNotBlank(loType)) {
            dc.set("$.api:hasTopic.@value", "https://onerecord.iata.org/ns/cargo#" + loType);
        }
        if (StringUtils.equals(subscribeType, SubscribeTypes.LOGISTICS_OBJECT.name())) {
            List<Map<String, Object>> eventTypes = Arrays.asList(
                    createEventTypeMap("api:LOGISTICS_OBJECT_UPDATED"),
                    createEventTypeMap("api:LOGISTICS_OBJECT_CREATED")
//                    createEventTypeMap("api:LOGISTICS_EVENT_RECEIVED")
            );
            dc.set("$.api:includeSubscriptionEventType", eventTypes);
        } else if (StringUtils.equals(subscribeType, SubscribeTypes.LOGISTICS_EVENT.name())) {
            List<Map<String, Object>> eventTypes = Arrays.asList(
                    createEventTypeMap("api:LOGISTICS_EVENT_RECEIVED")
            );
            dc.set("$.api:includeSubscriptionEventType", eventTypes);
        }
        if (StringUtils.isNotBlank(awbNumber)) {
            List<String> awbNumberList = replaceTrimsAndJoins(awbNumber);
            dc.set("$.api:hasSubscriptionLogisticsObjectNumbers", awbNumberList);
        }
        if (StringUtils.isNotBlank(flightNumbers)) {
            List<String> flightNumberList = replaceTrimsAndJoins(flightNumbers);
            dc.set("$.api:hasSubscriptionFlightNumbers", flightNumberList);
        }
        List<String> fsuList = replaceTrimsAndJoins(request.getFsuCodes());
        if (CollectionUtils.isNotEmpty(fsuList)) {
            dc.set("api:hasSubscriptionFsuCodes", fsuList);
        }
        List<String> departureList = replaceTrimsAndJoins(request.getDepartureCodes());
        if (CollectionUtils.isNotEmpty(departureList)) {
            dc.set("$.api:hasSubscriptionDepartureCodes", departureList);
        }
        List<String> arrivalCodeList = replaceTrimsAndJoins(request.getArrivalCodes());
        if (CollectionUtils.isNotEmpty(arrivalCodeList)) {
            dc.set("$.api:hasSubscriptionArrivalCodes", arrivalCodeList);
        }
        return dc.jsonString();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            subscribeJson = IOUtils.resourceToString("onerecord/subscriptions-Waybill.json", StandardCharsets.UTF_8, NeOnePingController.class.getClassLoader());
        } catch (IOException e) {
            throw new OneRecordServerException("JSON 文件初始化失败", e);
        }
    }

    private List<String> replaceTrimsAndJoins(String data) {
        if (StringUtils.isBlank(data)) {
            return Collections.emptyList();
        }
        String[] dataArray = data.split(",");
        return ListUtil.toList(dataArray).stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());
    }

    /**
     * 创建事件类型Map
     *
     * @param event 事件ID
     * @return 包含@id的Map
     */
    private Map<String, Object> createEventTypeMap(String event) {
        Map<String, Object> map = new HashMap<>();
        map.put("@id", event);
        return map;
    }
}
