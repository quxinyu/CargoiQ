package com.efreight.base.module.one.record.neone.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.efreight.base.module.one.record.neone.model.entity.NeOneServerInfo;

/**
 * @author fu yuan hui
 * @since 2024-09-29 18:31:11 星期日
 */
public interface NeOneServerInfoService extends IService<NeOneServerInfo> {

    NeOneServerInfo getByHost(String host);

    NeOneServerInfo getByCompanyId(String companyId);

    NeOneServerInfo fetchLocalServer();

    NeOneServerInfo getByServiceAddressCode(String serviceAddressCode);

    void removeServer(String companyId);

    String getToken(String companyId);
}
