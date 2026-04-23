package com.efreight.base.module.one.record.neone.notify.receive;

import cn.gov.caac.issp.utility.CaacParseTransfer;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.efreight.base.common.core.enmus.LoModuleType;
import com.efreight.base.common.core.enmus.OneRecordParseVersionType;
import com.efreight.base.common.core.exception.EftException;
import com.efreight.base.module.one.record.neone.config.NeOneDataModelProperties;
import com.efreight.base.module.one.record.neone.enums.FromType;
import com.efreight.base.module.one.record.neone.enums.NotifyEventType;
import com.efreight.base.module.one.record.neone.helper.IriGenerator;
import com.efreight.base.module.one.record.neone.model.entity.NeOneLogisticsObjects;
import com.efreight.base.module.one.record.neone.model.entity.NeOneServerCompanyHolder;
import com.efreight.base.module.one.record.neone.model.entity.NeOneSubscriptionRequest;
import com.efreight.base.module.one.record.neone.service.NeOneCompanyService;
import com.efreight.base.module.one.record.neone.service.NeOneLogisticsObjectsService;
import com.efreight.base.module.one.record.neone.service.NeOneShipmentDataService;
import com.efreight.base.module.one.record.neone.service.OneRecordSubscriptionsNotifyService;
import com.efreight.base.module.one.record.neone.utils.HttpClientUtil;
import com.efreight.base.module.one.record.neone.utils.IriUtils;
import com.efreight.base.module.one.record.neone.utils.KeyLockTokenUtils;
import com.google.common.eventbus.EventBus;
import com.jayway.jsonpath.JsonPath;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

/**
 * lo对象相关回调
 *
 * @author fu yuan hui
 * @since 2024-09-14 13:23:00 星期六
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LogisticsObjectCallback extends AbstractNotifyCallback<NeOneObjectNotifications> {

    private final NeOneDataModelProperties neOneDataModelProperties;

    private final NeOneCompanyService neOneCompanyService;

    private final NeOneShipmentDataService neOneShipmentDataService;

    private final NeOneLogisticsObjectsService neOneLogisticsObjectService;

    private final OneRecordSubscriptionsNotifyService oneRecordSubscriptionsNotifyService;

    private final HttpClientUtil httpClientUtil;

    private final EventBus asyncNeOneEventBus;

    private final IriGenerator iriGenerator;

    @Override
    protected void doCallback(NeOneObjectNotifications neOneObjectNotifications) {
        try {
            toCallback(neOneObjectNotifications);
        } catch (Exception e) {
            log.error(">>>>>>>>>>>>>>>>>>>通知回调 from:{}  出错:{}  from : {}", neOneObjectNotifications.getNotifyFrom(), e);
            throw new EftException("通知回调出错: " + e);
        }
    }

    public void toCallback(NeOneObjectNotifications notifications) {
        String notifyEventType = notifications.getNotifyEventType();
        log.info(">>>>>>>>>>>>>>>>>>>>>> 收到物流对象创建的回调通知： {}", notifications);
        String notifyBody = notifications.getNotifyBody();
        String from = JsonPath.using(CONF).parse(notifyBody).read("$.api:isTriggeredBy.@id");
        String actionRequestId = IriUtils.extractLastVariableFromUrl(from);
        // 查询订阅通知
        LambdaQueryWrapper<NeOneSubscriptionRequest> srWrapper = Wrappers.lambdaQuery();
        srWrapper.like(NeOneSubscriptionRequest::getActionRequestIri, actionRequestId);
        srWrapper.orderByDesc(NeOneSubscriptionRequest::getCreateTime);
        srWrapper.last("LIMIT 1");
        NeOneSubscriptionRequest subscriptionRequest = oneRecordSubscriptionsNotifyService.getOne(srWrapper);
        if (ObjectUtils.isEmpty(subscriptionRequest)) {
            throw new EftException(">>>通知回调 from ：" + from + " 未匹配到订阅记录 无法回调获取LO对象");
        }
        String targetCompanyId = subscriptionRequest.getTargetCompanyId();
        LambdaQueryWrapper<NeOneServerCompanyHolder> scWrapper = Wrappers.lambdaQuery();
        scWrapper.eq(NeOneServerCompanyHolder::getCompanyId, targetCompanyId);
        NeOneServerCompanyHolder companyHolder = neOneCompanyService.getOne(scWrapper);
        if (ObjectUtils.isEmpty(companyHolder)) {
            throw new EftException(">>>>通知回调 from : " + from + " 服务地址id : " + targetCompanyId + " 未匹配到服务地址无法回调获取LO对象");
        }
        log.info(">>>>>>>>>>>>>>>>>>>通知回调 from ：{} ,回调地址信息 :{} ", notifications.getNotifyFrom(), companyHolder);
        String loIri = JsonPath.using(CONF).parse(notifyBody).read("$.api:hasLogisticsObject.@id");
        String topicType = JsonPath.using(CONF).parse(notifyBody).read("$.api:hasLogisticsObjectType.@value");
        if (StringUtils.isBlank(topicType)) {
            topicType = JsonPath.using(CONF).parse(notifyBody).read("$.api:hasLogisticsObjectType.@id");
        }
        if (StringUtils.isBlank(topicType)) {
            log.error(">>>>>>>>>>>>>>>>>> 通知的请求体结构没有 '{}' 节点, 这是非法结构", "api:hasLogisticsObjectType.@value");
        } else {
            String[] topicTypes = topicType.split("#");
            if (ArrayUtils.isEmpty(topicTypes)) {
                log.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 从订阅的请求体中无法湖区订阅的类型：{}", notifyBody);
            }
            topicType = topicTypes.length == 1 ? topicTypes[0] : topicTypes[1];
        }
        NeOneLogisticsObjects base = new NeOneLogisticsObjects();
        base.setIri(this.iriGenerator.generateLogisticsObjectLoId(notifications.getLoId()));
        base.setLoId(notifications.getLoId());
        base.setContextType(topicType);
        base.setLoModuleType(LoModuleType.LOGISTICS_OBJECT.name());
        base.setCreateTime(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
        base.setLoFromType(FromType.SUBSCRIPTION_NOTIFY.name());
        base.setVersion(OneRecordParseVersionType.V3.getName());
        // 报文内容
        String bodyText = getBodyTextFromIri(loIri, companyHolder);
        neOneShipmentDataService.getObjectFromOneRecord(bodyText, loIri);
        String firstMsgType = neOneDataModelProperties.getModelByCompanyName(companyHolder.getCompanyName());
        log.info(">>>>>>>>>>>>>使用 :{} 数据模型获取主单号>>>>>>>>>>>>>", firstMsgType);
        if (StringUtils.isNotBlank(bodyText)) {
            JSONObject bt = JSONObject.parseObject(bodyText);
            bt.put("@id", base.getIri());
            Map<String, Object> record = CaacParseTransfer.parse(bt.toJSONString(), firstMsgType, "2", "JSON");
            String mawbCode = (String) record.get("MawbCode");
            log.info(">>>>>>>>>>>>>主单号: {} >>>>>>>>>>>>>", mawbCode);
            base.setMawbCode(mawbCode);
            base.setBodyText(bt.toJSONString());
        }
        if (StringUtils.isBlank(bodyText)) {
            base.setBodyText("{}");
        }
        this.neOneLogisticsObjectService.save(base);
        base.setNotifyEventType(NotifyEventType.LOGISTICS_OBJECT_CREATED);
        asyncNeOneEventBus.post(base);

    }

    /**
     * 从iri地址获取lo对象的内容
     *
     * @param iri 物流对象的获取地址
     */
    private String getBodyTextFromIri(String iri, NeOneServerCompanyHolder companyHolder) {

        String token = KeyLockTokenUtils.getCompanyToekn(companyHolder);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/ld+json; version=2.0.0-dev");
        headers.add("Content-Type", "application/ld+json; version=2.0.0-dev");
        headers.add("Authorization", "Bearer " + token);
        try {
            String url = iri;
            log.info(">>>>>>>>>>>>>>>>>>获取lo对象，url/iri :{}，headers :{}", url, headers);
            ResponseEntity<String> response = httpClientUtil.get(url, headers, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                log.info(">>>>>>>>>>>>>>>>>>获取 :{} lo对象成功，请求结果 :{}", companyHolder.getCompanyName(), response.getBody());
                return response.getBody();
            } else {
                log.error(">>>>>>>>>>>>>>>>>>获取 :{} lo对象失败，请求结果 :{}", companyHolder.getCompanyName(), JSON.toJSONString(response));
                throw new EftException(">>>>>>获取:" + companyHolder.getCompanyName() + " lo对象失败，请求结果 :" + JSON.toJSONString(response));
            }
        } catch (Throwable e) {
            log.error(">>>>>>>>>>>>>>>>>>获取 :{} lo对象失败，原因 :{}", companyHolder.getCompanyName(), e.getMessage());
            throw new EftException(">>>>>>获取 :" + companyHolder.getCompanyName() + " lo对象失败: " + e);
        }
    }
}
