package com.efreight.base.module.one.record.neone.controller;

import com.efreight.base.common.core.exception.OneRecordServerException;
import com.efreight.base.module.one.record.neone.annotations.Authenticated;
import com.efreight.base.module.one.record.neone.config.NeOneConfigProperties;
import com.efreight.base.module.one.record.neone.model.entity.NeOneServerCompanyHolder;
import com.efreight.base.module.one.record.neone.service.NeOneCompanyService;
import com.efreight.base.module.one.record.neone.utils.ResponseEntityBuilder;
import com.jayway.jsonpath.JsonPath;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


/**
 * @author fu yuan hui
 * @since 2024-08-28 15:33:38 星期三
 */
@Api(tags = "one record 验证")
@Authenticated
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class NeOnePingController {

    private static final String GET_SERVER_INFO_JSON;

    private final NeOneConfigProperties neOneConfigProperties;

    private final NeOneCompanyService companyService;

    static {
        try {
            GET_SERVER_INFO_JSON = IOUtils.resourceToString("onerecord/ping.json", StandardCharsets.UTF_8, NeOnePingController.class.getClassLoader());
        } catch (IOException e) {
            throw new OneRecordServerException("JSON 文件初始化失败", e);
        }
    }
    @ApiOperation(value = "验证1R服务", notes = "验证1R服务")
    @GetMapping(produces = "application/ld+json")
    public ResponseEntity<?> ping() {

        NeOneServerCompanyHolder localServer = companyService.getLocalServer();
        String pingBody = JsonPath.parse(GET_SERVER_INFO_JSON)
                .set("$.@id", neOneConfigProperties.getBaseIri())
                .set("$.api:hasServerEndpoint", neOneConfigProperties.getBaseIri())
                .set("$.api:hasDataHolder.@id", localServer.getCompanyIri())
                .set("$.api:hasSupportedLogisticsObjectType", this.neOneConfigProperties.getHasSupportedLogisticsObjectTypes())
                .jsonString();

        return ResponseEntityBuilder.ok().body(pingBody);
    }
}
