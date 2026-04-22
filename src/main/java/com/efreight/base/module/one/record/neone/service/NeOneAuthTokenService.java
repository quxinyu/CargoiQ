package com.efreight.base.module.one.record.neone.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.efreight.base.module.one.record.neone.model.entity.NeOneAuthToken;

/**
 * @author fu yuan hui
 */
public interface NeOneAuthTokenService extends IService<NeOneAuthToken> {

    NeOneAuthToken getToken(String clientId, String clientSecret, String grantType);
}