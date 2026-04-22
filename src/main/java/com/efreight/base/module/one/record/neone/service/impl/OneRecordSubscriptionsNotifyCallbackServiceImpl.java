package com.efreight.base.module.one.record.neone.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.efreight.base.common.core.exception.EftException;
import com.efreight.base.module.one.record.neone.enums.SubscribeStatus;
import com.efreight.base.module.one.record.neone.helper.NeOneServerRequestHelper;
import com.efreight.base.module.one.record.neone.mapper.OneRecordSubscriptionsNotifyCallbackMapper;
import com.efreight.base.module.one.record.neone.model.entity.NeOneSubscriptionCallback;
import com.efreight.base.module.one.record.neone.service.OneRecordSubscriptionsNotifyCallbackService;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author fu yuan hui
 * @since 2024-10-09 13:33:45 星期三
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class OneRecordSubscriptionsNotifyCallbackServiceImpl extends ServiceImpl<OneRecordSubscriptionsNotifyCallbackMapper, NeOneSubscriptionCallback> implements OneRecordSubscriptionsNotifyCallbackService {

    private static final Configuration CONFIG = Configuration.defaultConfiguration().addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);

    private final NeOneServerRequestHelper requestHelper;

    @Override
    public void resolveSubscriptions(String body) {
        save(createCallback(body));
    }

    @Override
    public NeOneSubscriptionCallback retireSubscribeByHost(String subscribeHost) {
        LambdaQueryWrapper<NeOneSubscriptionCallback> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(NeOneSubscriptionCallback::getSubscriberHost, subscribeHost);
        return this.getOne(wrapper);
    }

    @Override
    public void resolveUpdateSubscriptions(String body) {
        DocumentContext dc = JsonPath.using(CONFIG).parse(body);

        String subscriberHost = dc.read("$.@context.subscriber");
        if (StringUtils.isBlank(subscriberHost)) {
            throw new EftException("订阅者host不能为空");
        }

        NeOneSubscriptionCallback neOneSubscriptionCallback = this.retireSubscribeByHost(subscriberHost);
        if(neOneSubscriptionCallback == null) {
            throw new EftException("根据订阅者host = '" + subscriberHost +"' 无法查找到订阅者信息,系统无法更新订阅信息");
        }


        NeOneSubscriptionCallback callback = createCallback(body);
        callback.setCreateTime(neOneSubscriptionCallback.getCreateTime());
        callback.setUpdateTime(LocalDateTime.now());
        callback.setId(neOneSubscriptionCallback.getId());

        updateById(callback);
    }

    @Override
    public void unsubscribe(String host) {
        NeOneSubscriptionCallback neOneSubscriptionCallback = retireSubscribeByHost(host);
        if (neOneSubscriptionCallback == null) {
            throw new EftException("根据订阅者host = '" + host +"' 无法查找到订阅者信息,系统无法取消订阅");
        }
        if (SubscribeStatus.UNSUBSCRIBE.name().equals(neOneSubscriptionCallback.getSubscribeStatus())) {
            return;
        }
        neOneSubscriptionCallback.setSubscribeStatus(SubscribeStatus.UNSUBSCRIBE.name());
        updateById(neOneSubscriptionCallback);
    }


    private NeOneSubscriptionCallback createCallback(String body) {
        DocumentContext dc = JsonPath.using(CONFIG).parse(body);

        String subscriberHost = dc.read("$.@context.subscriber");
        if (StringUtils.isBlank(subscriberHost)) {
            throw new EftException("订阅者host不能为空");
        }

        String subscriberCompanyIri = dc.read("$.api:hasSubscriber.@id");
        if (StringUtils.isBlank(subscriberCompanyIri)) {
            throw new EftException("订阅者CompanyIri不能为空");
        }

        String companyServerBody = requestHelper.getCompanyServer(subscriberCompanyIri);
        String companyName = JsonPath.using(CONFIG).parse(companyServerBody).read("$.cargo:name");
        String subscribeType = dc.read("$.api:hasSubscriptionLogisticsObjectType");
        List<String> awbNumbers = dc.read("$.api:hasSubscriptionLogisticsObjectNumbers");
        Boolean hasSubscribeFhl = dc.read("$.api:hasSubscriptionFhl");
        List<String> fsuCodes = dc.read("$.api:hasSubscriptionFsuCodes");
        List<String> flightNumbers = dc.read("$.api:hasSubscriptionFlightNumbers");
        List<String> departureCodes = dc.read("$.api:hasSubscriptionDepartureCodes");
        List<String> arriveCodes = dc.read("$.api:hasSubscriptionArrivalCodes");

        String awbNumberString = resolveList(awbNumbers);
        String flightNumberString = resolveList(flightNumbers);
        if (StringUtils.isAllBlank(awbNumberString, flightNumberString)) {
            throw new EftException("订阅的运单号和航班号不能同时为空");
        }
        //收集订阅信息
        NeOneSubscriptionCallback callback = new NeOneSubscriptionCallback();
        callback.setSubscriberHost(subscriberHost);
        callback.setSubscriberCompanyName(companyName);
        callback.setSubscribeType(subscribeType);
        callback.setSubscribeFhl(Boolean.TRUE.equals(hasSubscribeFhl));
        callback.setAwbNumbers(awbNumberString);
        callback.setFsuCodes(resolveList(fsuCodes));
        callback.setFlightNumbers(flightNumberString);
        callback.setDepartureCodes(resolveList(departureCodes));
        callback.setArrivalCodes(resolveList(arriveCodes));
        callback.setSubscribeJsonBody(body);
        callback.setCreateTime(LocalDateTime.now());
        callback.setSubscribeStatus(SubscribeStatus.SUBSCRIBE_OK.name());

        return callback;
    }

    private static String resolveList(List<String> dataList) {
        if (CollectionUtils.isEmpty(dataList)) {
            return null;
        }

        return String.join(",", dataList);
    }

}
