package com.efreight.base.module.one.record.neone.helper;

import com.efreight.base.common.core.utils.SpringUtils;
import com.efreight.base.module.one.record.neone.config.NeOneConfigProperties;
import com.efreight.base.module.one.record.neone.model.entity.NeOneServerCompanyHolder;
import com.efreight.base.module.one.record.neone.service.NeOneCompanyService;
import com.efreight.base.module.one.record.neone.utils.UrlFormatUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URL;

/**
 * 完整Iri路径生成器
 *
 * @author fu yuan hui
 * @since 2024-08-14 17:28:54 星期三
 */
@Slf4j
@Component
public class IriGenerator {

    @Autowired
    private NeOneConfigProperties neOneConfigProperties;

    @Value("${server.servlet.context-path:}")
    private String contextPath;
    // LO对象的访问前缀
    private final static String LOGISTICS_OBJECTS_PREFIX = "/logistics-objects";
    // LO对象事件的访问前缀
    private final static String LOGISTICS_EVENTS_PREFIX = "/logistics-events";
    // LO对象ACTION REQUESTS访问前缀
    private final static String ACTION_REQUESTS_PREFIX = "/action-requests";

    /**
     * 生成lo完整访问路径
     *
     * @param loId       lo对象uuid
     * @param serverCode 多租户情况下租户的访问前缀
     * @return lo完整访问路径
     */
    public String generateLogisticsObjectLoId(String loId, String... serverCode) {
        NeOneCompanyService neOneCompanyService = SpringUtils.getBean("neOneCompanyServiceImpl");
        NeOneServerCompanyHolder holder = neOneCompanyService.getLocalServerNoCache();
//        String host = getHostFromBaseUri(holder.getHost());
        String host = holder.getHost();
        if (this.neOneConfigProperties.isEnableMultiTenant()) {
            return UrlFormatUtils.resolveUrl(host + contextPath + "/" + serverCode[0] + LOGISTICS_OBJECTS_PREFIX + "/" + loId);
        }
        return UrlFormatUtils.resolveUrl(host + contextPath + LOGISTICS_OBJECTS_PREFIX + "/" + loId);
    }

    /**
     * 生成lo事件对象完整访问路径
     *
     * @param loId       lo对象uuid
     * @param eventId    lo事件对象uuid
     * @param serverCode 多租户情况下租户的访问前缀
     * @return lo事件对象完整访问路径
     */
    public String generateLogisticsEventLoId(String loId, String eventId, String... serverCode) {
        NeOneCompanyService neOneCompanyService = SpringUtils.getBean("neOneCompanyServiceImpl");
        NeOneServerCompanyHolder holder = neOneCompanyService.getLocalServerNoCache();
//        String host = getHostFromBaseUri(holder.getHost());
        String host = holder.getHost();
        if (this.neOneConfigProperties.isEnableMultiTenant()) {
            return UrlFormatUtils.resolveUrl(host + contextPath + "/" + serverCode[0] + LOGISTICS_OBJECTS_PREFIX + "/" + loId + LOGISTICS_EVENTS_PREFIX + "/" + eventId);
        }
        return UrlFormatUtils.resolveUrl(host + contextPath + LOGISTICS_OBJECTS_PREFIX + "/" + loId + LOGISTICS_EVENTS_PREFIX + "/" + eventId);
    }

    /**
     * 生成lo事件对象完整访问路径（扩展：路径中不含有LO对象id）
     *
     * @param eventId    lo事件对象uuid
     * @param serverCode 多租户情况下租户的访问前缀
     * @return lo事件对象完整访问路径
     */
    public String generateExtLogisticsEventLoId(String eventId, String... serverCode) {
        NeOneCompanyService neOneCompanyService = SpringUtils.getBean("neOneCompanyServiceImpl");
        NeOneServerCompanyHolder holder = neOneCompanyService.getLocalServerNoCache();
        String host = getHostFromBaseUri(holder.getHost());
        if (this.neOneConfigProperties.isEnableMultiTenant()) {
            return UrlFormatUtils.resolveUrl(host + contextPath + "/" + serverCode[0] + LOGISTICS_EVENTS_PREFIX + "/" + eventId);
        }
        if (!host.contains("/api")) {
            host = host + "/api";
        }
        if (!host.contains("https://")) {
            host = "https://" + host;
        }
        return UrlFormatUtils.resolveUrl(host + contextPath + LOGISTICS_EVENTS_PREFIX + "/" + eventId);
    }

    /**
     * 生成Action Request的完整Iri路径
     *
     * @param host host
     * @param uuid Action Request对象的uuid地址
     * @return Action Request的完整Iri路径
     */
    public String generateActionRequestIri(String host, String uuid) {
        if (host.contains(contextPath.replace("/", ""))) {
            return UrlFormatUtils.resolveUrl(host + ACTION_REQUESTS_PREFIX + "/" + uuid);
        }
        return UrlFormatUtils.resolveUrl(host + contextPath + ACTION_REQUESTS_PREFIX + "/" + uuid);
    }

    /**
     * 从配置的baseIri中获取Host
     *
     * @param baseIri baseIri
     * @return Host
     */
    private String getHostFromBaseUri(String baseIri) {
        try {
            return new URL(baseIri).getHost();
        } catch (Throwable e) {
            log.error(e.toString(), e);
            return null;
        }
    }
}
