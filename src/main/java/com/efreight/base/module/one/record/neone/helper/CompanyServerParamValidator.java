package com.efreight.base.module.one.record.neone.helper;

import com.efreight.base.module.one.record.neone.ex.NeoneRequestBodyException;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import org.apache.commons.lang3.StringUtils;

/**
 * @author fu yuan hui
 * @since 2024-09-18 10:48:33 星期三
 */
public final class CompanyServerParamValidator {

    private static final Configuration CONFIG = Configuration.defaultConfiguration().addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);

    public static void validate(String pingBody) {
        String id = JsonPath.using(CONFIG).parse(pingBody).read("$.@id");
        String type = JsonPath.using(CONFIG).parse(pingBody).read("$.@type");
        String holderType = JsonPath.using(CONFIG).parse(pingBody).read("$.api:hasDataHolder.@type");

        if (StringUtils.isBlank(id) || StringUtils.isBlank(type) || StringUtils.isBlank(holderType)) {
            throw new NeoneRequestBodyException("非法请求体");
        }

        if (!"api:ServerInformation".equalsIgnoreCase(type)) {
            throw new NeoneRequestBodyException("非法请求体, type应为：ServerInformation");
        }

        if (!"cargo:Company".equalsIgnoreCase(holderType)) {
            throw new NeoneRequestBodyException("非法请求体, holderType应为：cargo:Company");
        }
    }
}
