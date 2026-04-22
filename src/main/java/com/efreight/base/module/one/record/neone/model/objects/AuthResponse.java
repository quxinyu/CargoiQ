package com.efreight.base.module.one.record.neone.model.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author fu yuan hui
 * @since 2024-08-29 10:28:02 星期四
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse implements Serializable {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty(value = "token_type", defaultValue = "Bearer")
    private String tokenType;

    @JsonProperty("expires_in")
    private Long expiresIn;

    @JsonProperty("refresh_token")
    private String refreshToken;
}
