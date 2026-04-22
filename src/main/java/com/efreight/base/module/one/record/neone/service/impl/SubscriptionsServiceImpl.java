package com.efreight.base.module.one.record.neone.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.efreight.base.common.core.exception.OneRecordServerException;
import com.efreight.base.common.core.utils.SpringUtils;
import com.efreight.base.module.one.record.neone.enums.ActionRequestStatus;
import com.efreight.base.module.one.record.neone.enums.NotifyEventType;
import com.efreight.base.module.one.record.neone.enums.SubscribeStatus;
import com.efreight.base.module.one.record.neone.enums.TopicType;
import com.efreight.base.module.one.record.neone.ex.NeoneRequestBodyException;
import com.efreight.base.module.one.record.neone.ex.TopicMissingException;
import com.efreight.base.module.one.record.neone.helper.IriGenerator;
import com.efreight.base.module.one.record.neone.helper.LocalCompanyCacheHelper;
import com.efreight.base.module.one.record.neone.helper.NeOneServerRequestHelper;
import com.efreight.base.module.one.record.neone.helper.SubscriptionsParamValidator;
import com.efreight.base.module.one.record.neone.mapper.NeOneSubscriptionsMapper;
import com.efreight.base.module.one.record.neone.model.entity.*;
import com.efreight.base.module.one.record.neone.model.vo.SubscriptionVo;
import com.efreight.base.module.one.record.neone.service.ActionRequestService;
import com.efreight.base.module.one.record.neone.service.NeOneCompanyService;
import com.efreight.base.module.one.record.neone.service.OneRecordSubscriptionsNotifyCallbackService;
import com.efreight.base.module.one.record.neone.service.SubscriptionsService;
import com.efreight.base.module.one.record.neone.utils.IriUtils;
import com.efreight.base.module.one.record.neone.utils.UUIDTools;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author fu yuan hui
 * @since 2024-09-09 17:09:04 星期一
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class SubscriptionsServiceImpl extends ServiceImpl<NeOneSubscriptionsMapper, NeOneSubscriptions> implements SubscriptionsService {

    private static final String SUBSCRIPTIONS_FROM_TYPE;
    private static final String SUBSCRIPTIONS_FROM_IDENTIFIER;
    private final ActionRequestService actionRequestService;
    private final IriGenerator iriGenerator;
    private final LocalCompanyCacheHelper localCompanyRedisHelper;
    private final OneRecordSubscriptionsNotifyCallbackService oneRecordSubscriptionsNotifyCallbackService;

    private final NeOneServerRequestHelper neOneServerRequestHelper;

    private static final Configuration CONFIG = Configuration.defaultConfiguration().addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);

    static {
        try {
            SUBSCRIPTIONS_FROM_TYPE = IOUtils.resourceToString("onerecord/subscriptions-publish-type.json", StandardCharsets.UTF_8, SubscriptionsServiceImpl.class.getClassLoader());
            SUBSCRIPTIONS_FROM_IDENTIFIER = IOUtils.resourceToString("onerecord/subscriptions-publish-identifier.json", StandardCharsets.UTF_8, SubscriptionsServiceImpl.class.getClassLoader());
        } catch (IOException e) {
            throw new OneRecordServerException("JSON 文件初始化失败", e);
        }
    }


    @Override
    public String handleSubscriptionProposal(TopicType topicType, String topic) {
        if (TopicType.LOGISTICS_OBJECT_IDENTIFIER == topicType) {
            /*
               topic=https://1r.example.com/logistics-objects/1a8ded38-1804-467c-a369-81a411416b7c
               如果 TopicType = LOGISTICS_OBJECT_IDENTIFIER, 那么topic应该是一个物流对象
               TODO: 这里要检查物流对象的有效性，先留着，后面再写

               这里发生了循环依赖，暂时先注释掉，因为用不到它
             */
//            DocumentContext parse = JsonPath.parse(SUBSCRIPTIONS_FROM_IDENTIFIER);
//            return parse.set("$.api:hasSubscriber.@id", neOneCompanyService.getLocalServer().getCompanyIri())
//                    .set("$.api:hasTopic.@value", topic)
//                    .jsonString();
            return null;
        }
        /*
         * 类型
         * topic=https://onerecord.iata.org/ns/cargo#Shipment
         */
        String[] topicArray = topic.split("#");
        if (topicArray.length != 2) {
            throw new TopicMissingException("topic缺失：" + topic);
        }

//        return JsonPath.parse(SUBSCRIPTIONS_FROM_TYPE)
//                .set("$.@context.publisher", this.neOneConfigProperties.getBaseIri())
//                .set("$.@context.subscriber", this.neOneConfigProperties.getBaseIri())
//                .set("$.api:hasSubscriber.@id", neOneCompanyService.getLocalServer().getCompanyIri())
//                .set("$.api:hasTopic.@value", topic)
//                .jsonString();

        return null;
    }

    @Transactional
    @Override
    public String receiveSubscriptions(SubscriptionsParamValidator.ResolvedSubscriptions config, String subscribeBody) {
        NeOneSubscriptions neOneSubscriptions = NeOneSubscriptions.create(config);

        // 公司id
        String companyId = IriUtils.extractLastVariableFromUrl(config.getSubscriptionCompanyUrl());
        LambdaQueryWrapper<NeOneServerCompanyHolder> schWrapper = new LambdaQueryWrapper<>();
        schWrapper.eq(NeOneServerCompanyHolder::getCompanyId, companyId);
        NeOneCompanyService neOneCompanyService = SpringUtils.getBean("neOneCompanyServiceImpl");
        NeOneServerCompanyHolder serverCompanyHolder = neOneCompanyService.getOne(schWrapper);
        if (ObjectUtils.isEmpty(serverCompanyHolder)) {
            throw new NeoneRequestBodyException("Subscription Company Url Unauthorized");
        }
        neOneSubscriptions.setCallbackHost(serverCompanyHolder.getHost());
        if (this.hasSubscription(neOneSubscriptions)) {
            throw new NeoneRequestBodyException("Repeat Subscription");
        }
        NeOneServerCompanyHolder holder = this.localCompanyRedisHelper.get();
        String actionResponseJson = JsonPath.parse(config.getSubscriptionJsonBody())
                .put("$", "api:isRequestedBy", new LinkedHashMap<String, String>() {{
                    put("@id", config.getSubscriptionCompanyUrl());
                }})
                .put("$", "api:hasRequestStatus", new LinkedHashMap<String, String>() {{
                    put("@id", "api:REQUEST_PENDING");
                }})
                .put("$", "api:isRequestedAt", new LinkedHashMap<String, String>() {{
                    put("@type", "http://www.w3.org/2001/XMLSchema#dateTime");
                    put("@value", Instant.now().toString());
                }})
                .jsonString();

        String actionRequestId = UUIDTools.generateSimpleUUID();
        String actionRequestIri = iriGenerator.generateActionRequestIri(holder.getHost(), actionRequestId);
        neOneSubscriptions.setActionRequestIri(actionRequestIri);
        neOneSubscriptions.setActionRequestStatus(ActionRequestStatus.REQUEST_PENDING.name());

        this.save(neOneSubscriptions);
//        this.actionRequestService.saveActionRequest(new NeOneActionRequests(actionRequestId, actionRequestIri, actionResponseJson));
        //更新到订阅接收表中,可以从拓展页面中查询到
        NeOneSubscriptionCallback subscriptionReceive = new NeOneSubscriptionCallback();
        subscriptionReceive.setSubscribeJsonBody(subscribeBody);
        subscriptionReceive.setSubscribeStatus(SubscribeStatus.PENDING.name());
        subscriptionReceive.setSubscriberHost(serverCompanyHolder.getHost());
        subscriptionReceive.setSubscriberCompanyName(serverCompanyHolder.getCompanyName());
        subscriptionReceive.setActionRequestIri(actionRequestIri);
        subscriptionReceive.setSubscribeType(config.getSubTypeLoType());
        subscriptionReceive.setSubscriptionsId(neOneSubscriptions.getId());
        this.oneRecordSubscriptionsNotifyCallbackService.save(subscriptionReceive);
        return actionRequestIri;
    }

    @Override
    public List<NeOneSubscriptions> retrieveSubscriptionsWithStatus() {
        LambdaQueryWrapper<NeOneSubscriptions> wrapper = Wrappers.lambdaQuery();
//        wrapper.eq(NeOneSubscriptions::getActionRequestStatus, ActionRequestStatus.REQUEST_ACCEPTED.name());
        return list(wrapper);
    }

    @Override
    public boolean hasSubscription(NeOneSubscriptions neOneSubscriptions) {
        LambdaQueryWrapper<NeOneSubscriptions> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(NeOneSubscriptions::getCallbackHost, neOneSubscriptions.getCallbackHost());
        wrapper.eq(NeOneSubscriptions::getSubType, neOneSubscriptions.getSubType());
        wrapper.eq(NeOneSubscriptions::getSubTypeLoType, neOneSubscriptions.getSubTypeLoType());
//        wrapper.eq(NeOneSubscriptions::getSubscriptionCompanyUrl, neOneSubscriptions.getSubscriptionCompanyUrl());
        return count(wrapper) > 0;
    }

    @Override
    public NeOneSubscriptions getByActionRequestIri(String actionRequestIri) {
        return baseMapper.getByActionRequestIri(actionRequestIri);
    }

    @Override
    public void doSubscriptions() {

    }

    @Override
    public void unsubscribe(String host) {
        oneRecordSubscriptionsNotifyCallbackService.unsubscribe(host);
    }

    @Override
    public IPage<?> pageList(SubscriptionVo param) {
        LambdaQueryWrapper<NeOneSubscriptions> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StringUtils.isNotBlank(param.getSubType()), NeOneSubscriptions::getSubType, param.getSubType());
        wrapper.like(StringUtils.isNotBlank(param.getCallbackHost()), NeOneSubscriptions::getCallbackHost, param.getCallbackHost());
        wrapper.orderByDesc(NeOneSubscriptions::getCreateTime);
        return page(new Page<>(param.getCurrent(), param.getSize()), wrapper);
    }

    /**
     * 接收详情
     *
     * @param id
     * @return
     */
    @Override
    public NeOneSubscriptionCallback receiveDetails(Long id) {
        return oneRecordSubscriptionsNotifyCallbackService.getById(id);
    }


    @Override
    @Transactional
    public boolean deleteSubscription(Long id) {
        LambdaQueryWrapper<NeOneSubscriptionCallback> delWrapper = new LambdaQueryWrapper<>();
        delWrapper.eq(NeOneSubscriptionCallback::getSubscriptionsId, id);
        oneRecordSubscriptionsNotifyCallbackService.remove(delWrapper);
        return this.removeById(id);
    }

    /**
     * TODO 获取替换创建对象时 查询订阅
     *
     * @param loObject
     * @return
     */
    @Override
    public List<NeOneSubscriptions> getSubscriptions(Object loObject) {
        List<NeOneSubscriptions> resultList = new ArrayList<>();
        if (loObject instanceof NeOneLogisticsObjects) {
            NeOneLogisticsObjects oneLogisticsObjects = (NeOneLogisticsObjects) loObject;
            // 物流对象类型
            String contextType = oneLogisticsObjects.getContextType();
            NotifyEventType notifyEventType = oneLogisticsObjects.getNotifyEventType();
            String loId = oneLogisticsObjects.getLoId();
            LambdaQueryWrapper<NeOneSubscriptions> subscriptionsWrapper = new LambdaQueryWrapper<>();
            subscriptionsWrapper.or(wrapper -> {
                // 第一个条件：contextType 不为空时，eq(subTypeLoType, contextType)
                wrapper.eq(StringUtils.isNotBlank(contextType), NeOneSubscriptions::getSubTypeLoType, contextType);
            });
            subscriptionsWrapper.or(wrapper -> {
                // 第二个条件：loId 不为空时，eq(subTypeLoType, loId)
                wrapper.eq(StringUtils.isNotBlank(loId), NeOneSubscriptions::getSubTypeLoType, loId);
            });
            resultList = this.list(subscriptionsWrapper);
            if (CollectionUtils.isEmpty(resultList)) {
                return resultList;
            }
            resultList = resultList
                    .stream().filter(bean -> bean.getIncludeSubscriptionType().contains(notifyEventType.name()))
                    .collect(Collectors.toList());
            //  TODO  后续完善更细节的过滤  主单号 订阅 始发港 目的港过滤
            //  TODO  ne_one_subscriptions_receive
            return resultList;
        } else if (loObject instanceof NeOneLogisticsEvents) {
            NeOneLogisticsEvents neOneLogisticsEvents = (NeOneLogisticsEvents) loObject;
            String loId = neOneLogisticsEvents.getLoId();
            NotifyEventType notifyEventType = neOneLogisticsEvents.getNotifyEventType();
            LambdaQueryWrapper<NeOneSubscriptions> subscriptionsWrapper = new LambdaQueryWrapper<>();
            subscriptionsWrapper.or(wrapper -> {
                // 第一个条件：contextType 不为空时，eq(subTypeLoType, contextType)
                wrapper.eq( NeOneSubscriptions::getSubTypeLoType, "Waybill");
            });
            subscriptionsWrapper.or(wrapper -> {
                // 第二个条件：loId 不为空时，eq(subTypeLoType, loId)
                wrapper.eq(StringUtils.isNotBlank(loId), NeOneSubscriptions::getSubTypeLoType, loId);
            });
            resultList = this.list(subscriptionsWrapper);
            if (CollectionUtils.isEmpty(resultList)) {
                return resultList;
            }
            resultList = resultList
                    .stream().filter(bean -> bean.getIncludeSubscriptionType().contains(notifyEventType.name()))
                    .collect(Collectors.toList());
        }
        return resultList;
    }

    @Override
    @Transactional
    public void internalSubscription(NeOneSubscriptions neOneSubscriptions) {
        neOneSubscriptions.setCallbackHost("Ebase");
        neOneSubscriptions.setSubscriptionCompanyUrl("Ebase");
        neOneSubscriptions.setTopic("Ebase");
        neOneSubscriptions.setSubscriptionJsonBody("{}");
        neOneSubscriptions.setActionRequestIri("Ebase");
        if (ObjectUtils.isEmpty(neOneSubscriptions.getId())) {
            this.save(neOneSubscriptions);
            NeOneSubscriptionCallback subscriptionReceive = new NeOneSubscriptionCallback();
            subscriptionReceive.setSubscribeJsonBody(neOneSubscriptions.getSubscriptionJsonBody());
            subscriptionReceive.setSubscribeStatus(SubscribeStatus.PENDING.name());
            subscriptionReceive.setSubscriberHost(neOneSubscriptions.getCallbackHost());
            subscriptionReceive.setSubscriberCompanyName("Ebase订阅");
            subscriptionReceive.setActionRequestIri(neOneSubscriptions.getActionRequestIri());
            subscriptionReceive.setSubscribeType(neOneSubscriptions.getSubTypeLoType());
            subscriptionReceive.setSubscriptionsId(neOneSubscriptions.getId());
            this.oneRecordSubscriptionsNotifyCallbackService.save(subscriptionReceive);
        } else {
            this.updateById(neOneSubscriptions);
            Long id = neOneSubscriptions.getId();
            LambdaQueryWrapper<NeOneSubscriptionCallback> receiveWrapper = new LambdaQueryWrapper<>();
            receiveWrapper.eq(ObjectUtils.isNotEmpty(id), NeOneSubscriptionCallback::getSubscriptionsId, id);
            NeOneSubscriptionCallback receive = this.oneRecordSubscriptionsNotifyCallbackService.getOne(receiveWrapper);
            receive.setSubscribeType(neOneSubscriptions.getSubTypeLoType());
            this.oneRecordSubscriptionsNotifyCallbackService.updateById(receive);
        }

    }
}
