package com.efreight.base.module.one.record.neone.model.objects;

import com.efreight.base.module.one.record.neone.annotations.HandlerMethodArgumentAlias;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 获取token请求参数
 *
 * @author fu yuan hui
 * @since 2024-08-29 10:28:18 星期四
 * 注意：{@link JsonAlias} 注解会在application/json请求中生效，如果是表单则不会生效
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest implements Serializable {

    @HandlerMethodArgumentAlias(value = {"username", "userName"})
    @JsonAlias(value = {"username", "userName"})
    private String username;

    @JsonProperty(value = "password")
    private String password;

    @JsonProperty(defaultValue = "client_credentials")
    @HandlerMethodArgumentAlias(value = {"grant_type", "grantType"}, defaultValue = "client_credentials")
    @JsonAlias(value = {"grant_type", "grantType"})
    private String grantType;

    @NotBlank
    @HandlerMethodArgumentAlias(value = {"client_id", "clientId"})
    @JsonAlias(value = {"client_id", "clientId"})
    private String clientId;

    @NotBlank
    @HandlerMethodArgumentAlias(value = {"client_secret", "clientSecret"})
    @JsonAlias(value = {"client_secret", "clientSecret"})
    private String clientSecret;
}
