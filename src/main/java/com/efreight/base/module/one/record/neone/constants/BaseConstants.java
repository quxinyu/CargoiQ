package com.efreight.base.module.one.record.neone.constants;

import com.efreight.base.module.one.record.neone.utils.UUIDTools;
import okhttp3.MediaType;

import java.util.HashMap;
import java.util.Map;

/**
 * 基础常量池
 *
 * @author fu yuan hui
 * @since 2024-08-13 16:57:50 星期二
 */
public interface BaseConstants {

    // TOKEN 前缀
    String DEFAULT_TOKEN_TYPE = "Bearer";
    // JWT 签名密钥
    String JWT_SIGN_KEY = "token!@#$%^efreight.!one";
    // 默认GRANT_TYPE的值
    String DEFAULT_GRANT_TYPE = "client_credentials";
    // JWT TOKEN HEADER
    Map<String, Object> JWT_TOKEN_HEADER = new HashMap<String, Object>() {
        {
            put("VERSION", "1.0.0");
            put("COMPANY", "iata");
            put("SIGN", UUIDTools.generate512BitsUUID());
        }
    };
    // Content-Type LD_JSON类型
    String CONTENT_TYPE_LD_JSON = "application/ld+json";
    // OKHTTP LD_JSON类型
    MediaType OK3_LD_JSON = MediaType.get("application/ld+json; charset=utf-8");
}
