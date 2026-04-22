package com.efreight.base.module.one.record.neone.helper;

import com.alibaba.fastjson2.JSON;
import com.efreight.base.module.one.record.neone.constants.RedisConstants;
import com.efreight.base.module.one.record.neone.model.entity.NeOneServerCompanyHolder;
import com.efreight.base.module.one.record.neone.service.NeOneCompanyService;
import com.efreight.base.module.one.record.neone.utils.ApplicationContextUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author fu yuan hui
 * @since 2024-12-06 16:21:54 星期五
 */
@RequiredArgsConstructor
@Component
public class LocalCompanyCacheHelper {

    private final StringRedisTemplate stringRedisTemplate;

    private final Map<String, NeOneServerCompanyHolder> holderMap = new HashMap<>(2);

    public NeOneServerCompanyHolder get() {
        NeOneServerCompanyHolder holder = this.holderMap.get(RedisConstants.LOCAL_SERVER_KEY);
        if (holder != null) {
            return holder;
        }

        String companyJson = this.stringRedisTemplate.opsForValue().get(RedisConstants.LOCAL_SERVER_KEY);
        if (StringUtils.isNotBlank(companyJson)) {
            return JSON.parseObject(companyJson, NeOneServerCompanyHolder.class);
        }

        NeOneCompanyService neOneCompanyService = ApplicationContextUtils.getBean(NeOneCompanyService.class);
        NeOneServerCompanyHolder localServer = neOneCompanyService.getLocalServerNoCache();
        if (localServer != null) {
            this.set(localServer);

            return localServer;
        }

        return null;
    }

    public void set(NeOneServerCompanyHolder companyHolder) {
        this.holderMap.put(RedisConstants.LOCAL_SERVER_KEY, companyHolder);
        this.stringRedisTemplate.opsForValue().set(RedisConstants.LOCAL_SERVER_KEY, JSON.toJSONString(companyHolder), 1, TimeUnit.DAYS);
    }

    public void delete() {
        this.holderMap.remove(RedisConstants.LOCAL_SERVER_KEY);
        this.stringRedisTemplate.delete(RedisConstants.LOCAL_SERVER_KEY);
    }
}
