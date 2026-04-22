package com.efreight.base.module.one.record.neone.helper;

import com.alibaba.fastjson2.JSONArray;
import com.efreight.base.module.one.record.neone.enums.TopicType;
import com.efreight.base.module.one.record.neone.ex.NeoneRequestBodyException;
import com.efreight.base.module.one.record.neone.ex.NotSupportLogisticsObjectTypeException;
import com.efreight.base.module.one.record.neone.ex.TopicMissingException;
import com.efreight.base.module.one.record.neone.utils.IriUtils;
import com.efreight.base.module.one.record.neone.utils.UrlFormatUtils;
import com.jayway.jsonpath.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author fu yuan hui
 * @since 2024-09-10 16:14:15 星期二
 */
@Slf4j
public final class SubscriptionsParamValidator {

    private static final Set<String> TOPIC_TYPE_SET = TopicType.resolveTopicName();

    /**
     * 如果某一个key不存在，默认会报错，使用这个配置就不会报错，而是返回null
     */
    private static final Configuration CONF = Configuration.defaultConfiguration().addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);

    private static final Configuration CONF_LIST = Configuration.defaultConfiguration().addOptions(Option.ALWAYS_RETURN_LIST);


    public static ResolvedSubscriptions validate(String subscriptionJsonBody) {
        return validate(subscriptionJsonBody, null);
    }

    public static ResolvedSubscriptions validate(String subscriptionJsonBody, String holderHost3) {
        ResolvedSubscriptions resolvedSubscriptions = new ResolvedSubscriptions();
        try {
            String type = JsonPath.using(CONF).parse(subscriptionJsonBody).read("$.@type", String.class);
            if (type == null || !type.toLowerCase().contains("subscription")) {
                throw new NeoneRequestBodyException("请求的结构非法, 缺失@type属性");
            }
            String topicType = JsonPath.using(CONF).parse(subscriptionJsonBody).read("$.api:hasTopicType.@id", String.class);
            if (StringUtils.isBlank(topicType)) {
                throw new NeoneRequestBodyException("请求的结构非法,缺失topicType");
            }
            if (topicType.startsWith("api:")) {
                topicType = topicType.replaceAll("api:", "");
            }
            if (!TOPIC_TYPE_SET.contains(topicType)) {
                throw new NotSupportLogisticsObjectTypeException("不支持的订阅对象类型：" + topicType);
            }
            resolvedSubscriptions.setSubType(topicType);
            if (TopicType.LOGISTICS_OBJECT_IDENTIFIER.name().equalsIgnoreCase(topicType)) {
                String companyUrl = resolveCompanyUrl(subscriptionJsonBody, "");
                String host = companyUrl.substring(0, companyUrl.lastIndexOf("logistics-objects"));
                resolvedSubscriptions.setCallbackHost(host);
                resolvedSubscriptions.setSubscriptionCompanyUrl(companyUrl);
            } else if (TopicType.LOGISTICS_OBJECT_TYPE.name().equalsIgnoreCase(topicType)) {
                String subscriptionHost = JsonPath.using(CONF).parse(subscriptionJsonBody).read("$.@context.subscriber", String.class);
                String publisherHost = JsonPath.using(CONF).parse(subscriptionJsonBody).read("$.@context.publisher", String.class);
                if (StringUtils.isNotBlank(publisherHost) && publisherHost.equalsIgnoreCase(subscriptionHost)) {
                    throw new NeoneRequestBodyException("订阅者和发布者不能使用相同主机域名");
                }
                String hasSubscriber = JsonPath.using(CONF).parse(subscriptionJsonBody).read("$.api:hasSubscriber.@id", String.class);
                if (StringUtils.isBlank(hasSubscriber)) {
                    throw new NeoneRequestBodyException("请求的结构非法,缺失回调地址Url");
                }
                String subUrl = hasSubscriber;
                if (subUrl.startsWith("subscriber:")) {
                    subUrl = hasSubscriber.replaceAll("subscriber:", "");
                }
                String companyUrl = subUrl;
                if (!companyUrl.startsWith("http") && !companyUrl.startsWith("https")) {
                    companyUrl = UrlFormatUtils.resolveUrl(subscriptionHost + "/" + companyUrl);
                }
//                resolvedSubscriptions.setCallbackHost(subscriptionHost);
                resolvedSubscriptions.setSubscriptionCompanyUrl(companyUrl);
            } else {
                String companyUrl = resolveCompanyUrl(subscriptionJsonBody, "holderHost");
                String host = companyUrl.substring(0, companyUrl.lastIndexOf("logistics-objects"));
                resolvedSubscriptions.setCallbackHost(host);
                resolvedSubscriptions.setSubscriptionCompanyUrl(companyUrl);
                List<String> loNumbers = JsonPath.using(CONF).parse(subscriptionJsonBody).read("$.api:hasSubscriptionLogisticsObjectNumber[*].@id");
                if (CollectionUtils.isEmpty(loNumbers)) {
                    throw new NeoneRequestBodyException("没有发现可订阅的主单号");
                }
                resolvedSubscriptions.setSubTypeLoType(TopicType.LoType.LO_NUMBER.getType());
            }
            String topic = JsonPath.using(CONF).parse(subscriptionJsonBody).read("$.api:hasTopic.@value", String.class);
            if (StringUtils.isBlank(topic)) {
                throw new NeoneRequestBodyException("请求的结构非法,缺失topic");
            }
            // 检查topic
            if (TopicType.LOGISTICS_OBJECT_TYPE.name().equalsIgnoreCase(topicType)
                    || TopicType.LOGISTICS_OBJECT_LO_NUMBER.name().equalsIgnoreCase(topicType)) {
                //https://onerecord.iata.org/ns/cargo#Shipment
                //https://onerecord.iata.org/ns/cargo#LoNumber
                String[] split = topic.split("#");
                if (split.length != 2) {
                    throw new TopicMissingException("topic 参数缺失");
                }
                String topicTypeLoType = split[1];
                TopicType.LoType from = TopicType.LoType.from(topicTypeLoType);
                resolvedSubscriptions.setSubTypeLoType(from.getType());
            } else {
                if(!UrlFormatUtils.validUrl(topic)) {
                    throw new NeoneRequestBodyException("topic指定的物流对象非法");
                }
                String loId = IriUtils.extractLastVariableFromUrl(topic);
                resolvedSubscriptions.setSubTypeLoType(loId);
            }
            // 第一步：解析成 List<Map<String, Object>>（每个元素是 { "@id": "xxx" }）
            List<Map<String, Object>> tempList = JsonPath.using(CONF)
                    .parse(subscriptionJsonBody)
                    .read("$['api:includeSubscriptionEventType']", List.class);
            // 第二步：手动提取每个 Map 中的 "@id" 值，转换成 List<String>
            List<String> includeSubscriptionEventType = tempList.stream()
                    .map(map -> (String) map.get("@id")) // 提取 @id 对应的字符串
                    .collect(Collectors.toList());
            JSONArray jsonArray = new JSONArray(includeSubscriptionEventType);
            resolvedSubscriptions.setIncludeSubscriptionType(jsonArray.toJSONString());
            resolvedSubscriptions.setTopic(topic);
            resolvedSubscriptions.setSubscriptionJsonBody(subscriptionJsonBody);
            return resolvedSubscriptions;
        } catch (PathNotFoundException e) {
            log.error(">>>>>>>>>>>>>>>>>..json path 解析发生错误", e);
            throw new NeoneRequestBodyException("The request is invalid");
        } catch (Exception e) {
            log.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>one record 参数校验失败", e);
            throw new NeoneRequestBodyException(e.getMessage());
        }
    }

    @Data
    public static class ResolvedSubscriptions {

        /**
         * @see TopicType
         */
        private String subType;

        /**
         * @see TopicType.LoType
         */
        private String subTypeLoType;

        private String callbackHost;

        private String subscriptionCompanyUrl;

        private String topic;

        private String subscriptionJsonBody;

        private String includeSubscriptionType;
    }

    private static String resolveCompanyUrl(String subscriptionJsonBody, String holderHost) {
        String companyUrl = JsonPath.using(CONF).parse(subscriptionJsonBody).read("$.api:hasSubscriber.@id", String.class);
        if (StringUtils.isBlank(companyUrl)) {
            throw new NeoneRequestBodyException("请求的结构非法,缺失Company地址");
        }
//        if (!UrlFormatUtils.validUrl(companyUrl) || !companyUrl.contains("logistics-objects")) {
//            throw new NeoneRequestBodyException("company id地址非法");
//        }
        String host = companyUrl.substring(0, companyUrl.lastIndexOf("logistics-objects"));

        if (StringUtils.isNotBlank(holderHost) && holderHost.equalsIgnoreCase(host)) {
            throw new NeoneRequestBodyException("回调地址非法,请填写发起者的host");
        }

        return companyUrl;
    }
}
