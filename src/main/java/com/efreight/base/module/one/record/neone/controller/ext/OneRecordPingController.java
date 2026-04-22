package com.efreight.base.module.one.record.neone.controller.ext;

import com.efreight.base.common.core.exception.OneRecordServerException;
import com.efreight.base.common.core.model.Result;
import com.efreight.base.module.one.record.neone.annotations.Authenticated;
import com.efreight.base.module.one.record.neone.config.NeOneConfigProperties;
import com.efreight.base.module.one.record.neone.controller.NeOnePingController;
import com.efreight.base.module.one.record.neone.ex.NeOneRequestWrapperException;
import com.efreight.base.module.one.record.neone.helper.NeOneServerRequestHelper;
import com.efreight.base.module.one.record.neone.model.entity.NeOneServerInfo;
import com.efreight.base.module.one.record.neone.service.NeOneServerInfoService;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author fu yuan hui
 * @since 2024-09-19 16:57:53 星期四
 * <p>
 * one Record 交互理论上是可以通过地址互通的，但是由于我们搭配了页面，前端直接访问对方的one record server 可能会存在跨域的问题
 *
 * 所以这个接口下的全部方法都是为页面提供的
 */
@Authenticated
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/ext")
@RestController
public class OneRecordPingController {

    private final NeOneConfigProperties neOneConfigProperties;

    private final NeOneServerRequestHelper requestHelper;

    private final NeOneServerInfoService neOneServerInfoService;

    private static final Configuration CONFIG = Configuration.defaultConfiguration().addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);

    private String neOneServerInfo;

    @PostMapping("/ping")
    public Result<?> ping(@RequestBody Map<String, String> neOneMap) {
        String neOneUrl = neOneMap.get("neOneUrl");
        if (StringUtils.isBlank(neOneUrl)) {
            throw new NeOneRequestWrapperException("neOneUrl 为空");
        }

        //自己ping自己
        if (this.neOneConfigProperties.matchConfigIri(neOneUrl)) {
            NeOneServerInfo host = this.neOneServerInfoService.getByHost(neOneUrl);
            if (host == null) {
                if (neOneUrl.endsWith("/")) {
                    neOneUrl = neOneUrl.substring(0, neOneUrl.length() - 1);
                    host = this.neOneServerInfoService.getByHost(neOneUrl);
                }
                if (host == null) {
                    neOneUrl += "/";
                    host = this.neOneServerInfoService.getByHost(neOneUrl);
                }
            }

            String pingBody = JsonPath.parse(neOneServerInfo)
                    .set("$.@id", neOneConfigProperties.getBaseIri())
                    .set("$.api:hasServerEndpoint", neOneConfigProperties.getBaseIri())
                    .set("$.api:hasDataHolder.@id", host.getCompanyIri())
                    .set("$.api:hasSupportedLogisticsObjectType", this.neOneConfigProperties.getHasSupportedLogisticsObjectTypes())
                    .jsonString();

            return Result.ok(pingBody);

        }

        String ping = this.requestHelper.ping(neOneUrl);
        String companyIri = JsonPath.using(CONFIG).parse(ping).read("$.api:hasDataHolder.@id");
        this.requestHelper.getCompanyServer(companyIri);
        return Result.ok(ping);
    }

    @PostConstruct
    public void init() {
        try {
            neOneServerInfo = IOUtils.resourceToString("onerecord/ping.json", StandardCharsets.UTF_8, NeOnePingController.class.getClassLoader());
        } catch (IOException e) {
            throw new OneRecordServerException("JSON 文件初始化失败", e);
        }
    }
}
