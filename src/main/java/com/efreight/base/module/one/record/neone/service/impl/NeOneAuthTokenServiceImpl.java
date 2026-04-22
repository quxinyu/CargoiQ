package com.efreight.base.module.one.record.neone.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.efreight.base.module.one.record.neone.mapper.NeOneAuthTokenMapper;
import com.efreight.base.module.one.record.neone.model.entity.NeOneAuthToken;
import com.efreight.base.module.one.record.neone.service.NeOneAuthTokenService;
import org.springframework.stereotype.Service;

/**
 * @author fu yuan hui
 */
@Service
public class NeOneAuthTokenServiceImpl extends ServiceImpl<NeOneAuthTokenMapper, NeOneAuthToken> implements NeOneAuthTokenService {

    @Override
    public NeOneAuthToken getToken(String clientId, String clientSecret, String grantType) {
        LambdaQueryWrapper<NeOneAuthToken> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(NeOneAuthToken::getClientId, clientId);
        wrapper.eq(NeOneAuthToken::getClientSecret, clientSecret);
        wrapper.eq(NeOneAuthToken::getGrantType, grantType);

        return getOne(wrapper);
    }
}