package com.efreight.base.module.one.record.neone.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.efreight.base.module.one.record.neone.mapper.NeOneServerInfoMapper;
import com.efreight.base.module.one.record.neone.model.entity.NeOneServerInfo;
import com.efreight.base.module.one.record.neone.service.NeOneServerInfoService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author fu yuan hui
 * @since 2024-09-29 18:31:31 星期日
 */
@Service
public class NeOneServerInfoServiceImpl extends ServiceImpl<NeOneServerInfoMapper, NeOneServerInfo> implements NeOneServerInfoService {


    @Resource
    private StringRedisTemplate stringRedisTemplate;



    @Override
    public String getToken(String companyId) {
        return null;
    }

    @Override
    public NeOneServerInfo getByHost(String host) {
        LambdaQueryWrapper<NeOneServerInfo> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(NeOneServerInfo::getBaseIri, host);

        return getOne(wrapper);
    }

    @Override
    public NeOneServerInfo getByCompanyId(String companyId) {
        LambdaQueryWrapper<NeOneServerInfo> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(NeOneServerInfo::getCompanyId, companyId);

        return getOne(wrapper);
    }

    @Override
    public NeOneServerInfo fetchLocalServer() {
        return null;
    }

    @Override
    public NeOneServerInfo getByServiceAddressCode(String serviceAddressCode) {
        LambdaQueryWrapper<NeOneServerInfo> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(NeOneServerInfo::getServerAddressCode, serviceAddressCode);

        return getOne(wrapper);
    }

    @Override
    public void removeServer(String companyId) {
        LambdaQueryWrapper<NeOneServerInfo> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(NeOneServerInfo::getCompanyId, companyId);

        remove(wrapper);
    }

}
