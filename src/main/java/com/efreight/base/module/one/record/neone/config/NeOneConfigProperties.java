package com.efreight.base.module.one.record.neone.config;

import com.efreight.base.module.one.record.neone.model.objects.AuthRequest;
import com.google.common.collect.ImmutableList;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 1R服务器相关配置类
 *
 * @author fu yuan hui
 * @since 2024-08-14 14:44:02 星期三
 */
@Data
@Slf4j
@Component
@ConfigurationProperties(prefix = "neone.server.config")
public class NeOneConfigProperties {

    /**
     * 版本号
     */
    private String version = "v3";

    /**
     * baseIri地址
     */
    private String baseIri = "http://localhost:6041";

    /**
     * token失效时间
     */
    private Integer tokenExpireHours = 2;

    /**
     * refresh token失效时间
     */
    private Integer refreshTokenExpireHours = 24;

    /**
     * 开启多租户
     */
    private boolean enableMultiTenant = false;

    /**
     * 开启oneRecord 的认证，需要token才可以访问接口
     */
    private boolean enableAuth = false;

    /**
     * 哪些url不走认证
     */
    private List<String> ignoreInterceptorUrls;

    /**
     * token认证相关的
     */
    private List<AuthRequest> allowRequestSecretGroups;

    private boolean loJsonFormatObjectOutput = true;

    //ImmutableList.of("Waybill", "Booking", "Piece", "Shipment");
    private List<String> hasSupportedLogisticsObjectTypes = ImmutableList.of("Waybill", "Booking");

    private List<String> hasSupportedSubscriptionsTypes = ImmutableList.of("Waybill", "Booking", "LogisticsEvents");

    public boolean matchConfigIri(String iri) {
        String s1 = this.baseIri.replaceAll("/", "");
        String s2 = iri.replaceAll("/", "");
        return s1.equalsIgnoreCase(s2);
    }
}
