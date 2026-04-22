package com.efreight.base.module.one.record.neone.model.objects;

import com.efreight.base.module.one.record.neone.model.request.BaseParam;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author fu yuan hui
 * @since 2024-08-29 10:28:18 星期四
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AuthInnerRequest extends BaseParam implements Serializable {

    /**
     * 主键id
     */
    private Integer id;

    @NotBlank(message = "企业名称不能为空")
    @JsonProperty("orgName")
    private String orgName;

    @JsonProperty("parentOrgName")
    private String parentOrgName;

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    @JsonProperty("opposite")
    private String opposite;

    @JsonProperty("exp")
    private Integer exp;

    @JsonAlias(value = {"grant_type", "grantType"})
    private String grantType;

    @JsonAlias(value = {"client_id", "clientId"})
    private String clientId;

    @JsonAlias(value = {"client_secret", "clientSecret"})
    private String clientSecret;
}
