package com.efreight.base.module.one.record.neone.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.efreight.base.module.one.record.neone.model.entity.NeOneServerCompanyHolder;
import com.efreight.base.module.one.record.neone.model.request.CompanyHolderRequest;

import java.util.List;

/**
 * @author qiuguan
 * @date 2024/09/16 22:44:09  星期一
 */
public interface NeOneCompanyService extends IService<NeOneServerCompanyHolder> {

    Long create(String pingBody);

    IPage<?> pageList(CompanyHolderRequest holderRequest);

    String getCompanyBody(Long id);

    void deleteByHost(String host);

    void pull(Long id);

    void updateCompanyInfo(CompanyHolderRequest request);

    List<NeOneServerCompanyHolder> allServer();

    NeOneServerCompanyHolder getLocalServer();

    NeOneServerCompanyHolder getLocalServerNoCache();

    Long createMySelfServer(CompanyHolderRequest request);

    void updateMySelfServer(CompanyHolderRequest request);

    void removeCompanyInfo(Long id);

    List<NeOneServerCompanyHolder> likeByName(String name);

    Long createServer(CompanyHolderRequest request);

}
