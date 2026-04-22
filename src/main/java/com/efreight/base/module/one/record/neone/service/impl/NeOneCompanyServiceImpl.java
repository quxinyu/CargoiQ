package com.efreight.base.module.one.record.neone.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.efreight.base.common.core.enmus.LoModuleType;
import com.efreight.base.common.core.exception.EftException;
import com.efreight.base.common.core.exception.OneRecordServerException;
import com.efreight.base.module.one.record.neone.config.NeOneConfigProperties;
import com.efreight.base.module.one.record.neone.enums.CompanyStatus;
import com.efreight.base.module.one.record.neone.enums.FromType;
import com.efreight.base.module.one.record.neone.ex.NeOneCompanyHolderException;
import com.efreight.base.module.one.record.neone.helper.IriGenerator;
import com.efreight.base.module.one.record.neone.helper.LocalCompanyCacheHelper;
import com.efreight.base.module.one.record.neone.helper.NeOneServerRequestHelper;
import com.efreight.base.module.one.record.neone.mapper.NeOneCompanyMapper;
import com.efreight.base.module.one.record.neone.model.entity.NeOneServerCompanyHolder;
import com.efreight.base.module.one.record.neone.model.entity.NeOneServerInfo;
import com.efreight.base.module.one.record.neone.model.request.CompanyHolderRequest;
import com.efreight.base.module.one.record.neone.service.NeOneCompanyService;
import com.efreight.base.module.one.record.neone.service.NeOneLogisticsObjectsService;
import com.efreight.base.module.one.record.neone.service.NeOneServerInfoService;
import com.efreight.base.module.one.record.neone.utils.RandomStringGenerator;
import com.efreight.base.module.one.record.neone.utils.UUIDTools;
import com.efreight.base.module.one.record.neone.utils.UrlFormatUtils;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author qiuguan
 * @date 2024/09/16 22:44:41  星期一
 */
@RequiredArgsConstructor
@Service
public class NeOneCompanyServiceImpl extends ServiceImpl<NeOneCompanyMapper, NeOneServerCompanyHolder> implements NeOneCompanyService, InitializingBean {

    protected static final Configuration CONFIG = Configuration.defaultConfiguration().addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);

    private final NeOneServerRequestHelper requestHelper;

    private final NeOneConfigProperties neOneConfigProperties;

    private final NeOneServerInfoService neOneServerInfoService;

    private final NeOneLogisticsObjectsService logisticsObjectsService;

    private final LocalCompanyCacheHelper localCompanyRedisHelper;

    private final IriGenerator generator;

    private String companyJson;

    private String pingJson;

    @Transactional
    @Override
    public Long create(String pingBody) {
        String host = JsonPath.using(CONFIG).parse(pingBody).read("$.api:hasServerEndpoint");
        //host+deleted做唯一索引
        NeOneServerInfo serverInfo = this.getByHost(host);
        if (serverInfo != null) {
            throw new NeOneCompanyHolderException("neone server 重复创建");
        }

        String companyIri = JsonPath.using(CONFIG).parse(pingBody).read("$.api:hasDataHolder.@id");
        String companyId = companyIri.substring(companyIri.lastIndexOf("/") + 1);

        NeOneServerInfo info = new NeOneServerInfo(host, companyId, companyIri, null, pingBody);
        this.neOneServerInfoService.save(info);

        NeOneServerCompanyHolder holder = createCompanyHolder(companyIri, host, companyId);
        holder.setCreateTime(LocalDateTime.now());

        save(holder);

        return holder.getId();
    }

    @Override
    public IPage<?> pageList(CompanyHolderRequest req) {
        LambdaQueryWrapper<NeOneServerCompanyHolder> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StringUtils.isNotBlank(req.getHost()), NeOneServerCompanyHolder::getHost, req.getHost());
        wrapper.like(StringUtils.isNotBlank(req.getCompanyId()), NeOneServerCompanyHolder::getCompanyId, req.getCompanyId());
        wrapper.like(StringUtils.isNotBlank(req.getCompanyName()), NeOneServerCompanyHolder::getCompanyName, req.getCompanyName());
        wrapper.like(StringUtils.isNotBlank(req.getCompanyShortName()), NeOneServerCompanyHolder::getCompanyShortName, req.getCompanyShortName());
        wrapper.like(StringUtils.isNotBlank(req.getContactName()), NeOneServerCompanyHolder::getContactName, req.getContactName());
        wrapper.like(StringUtils.isNotBlank(req.getCategory()), NeOneServerCompanyHolder::getContactShortName, req.getCategory());
        wrapper.select(NeOneServerCompanyHolder.class, h -> !"company_body".equals(h.getColumn()));
        return page(new Page<>(req.getCurrent(), req.getSize()), wrapper);
    }


    @Override
    public String getCompanyBody(Long id) {
        LambdaQueryWrapper<NeOneServerCompanyHolder> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(NeOneServerCompanyHolder::getId, id);
        wrapper.select(NeOneServerCompanyHolder::getCompanyBody);

        return getOne(wrapper).getCompanyBody();
    }

    @Override
    public void deleteByHost(String host) {
        LambdaQueryWrapper<NeOneServerCompanyHolder> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(NeOneServerCompanyHolder::getHost, host);

        this.remove(wrapper);
    }

    @Override
    public void pull(Long id) {
        NeOneServerCompanyHolder holder = getById(id);
        NeOneServerCompanyHolder companyHolder = createCompanyHolder(holder.getCompanyIri(), holder.getHost(), holder.getCompanyId());
        companyHolder.setUpdateTime(LocalDateTime.now());
        companyHolder.setId(id);
        updateById(companyHolder);

    }

    @Override
    public void updateCompanyInfo(CompanyHolderRequest request) {
        NeOneServerCompanyHolder dbHolder = getById(request.getId());
        BeanUtils.copyProperties(request, dbHolder);
        dbHolder.setCompanyIri(request.getHost() + "/neone/logistics-objects/" + request.getCompanyId());
        updateById(dbHolder);
    }

    @Override
    public List<NeOneServerCompanyHolder> allServer() {
        LambdaQueryWrapper<NeOneServerCompanyHolder> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(NeOneServerCompanyHolder::getCompanyType, FromType.OTHER.name());
//        wrapper.select(NeOneServerCompanyHolder::getId, NeOneServerCompanyHolder::getCompanyName);
        return list(wrapper);
    }

    @Override
    public NeOneServerCompanyHolder getLocalServer() {
        NeOneServerCompanyHolder holder = this.localCompanyRedisHelper.get();
        if (holder != null) {
            return holder;
        }
        return getLocalServerNoCache();
    }

    @Override
    public NeOneServerCompanyHolder getLocalServerNoCache() {
        LambdaQueryWrapper<NeOneServerCompanyHolder> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(NeOneServerCompanyHolder::getCompanyType, FromType.CREATE_SELF.name());
        return getOne(wrapper);
    }

    @Transactional
    @Override
    public Long createServer(CompanyHolderRequest request) {
        NeOneServerCompanyHolder holder = getLocalServerNoCache();
        if (holder != null) {
//            updateMySelfServer(request);
//            return holder.getId();
        }
        //String serverAddressCode = createAddressCode(request);
        String companyId = UUIDTools.generateSimpleUUID();
        String host = request.getHost();

        //String host = this.neOneConfigProperties.isEnableMultiTenant() ? baseIri + serverAddressCode : baseIri;
        String companyIri = UrlFormatUtils.resolveUrl(host + "/neone/logistics-objects/" + companyId);
        String pingBody = JsonPath.using(CONFIG).parse(pingJson)
                .set("$.@id", host)
                .set("$.api:hasServerEndpoint", host)
                .set("$.api:hasDataHolder.@id", UrlFormatUtils.resolveUrl(host + "/logistics-objects/" + companyId))
                .set("$.api:hasSupportedLogisticsObjectType", this.neOneConfigProperties.getHasSupportedLogisticsObjectTypes())
                .jsonString();
        // 1.保存server info
        NeOneServerInfo info = new NeOneServerInfo(host, companyId, companyIri, "", pingBody, FromType.OTHER);
        this.neOneServerInfoService.save(info);
        String companyBody = JsonPath.using(CONFIG).parse(companyJson)
                .set("$.cargo:name", request.getCompanyName())
                .set("$.cargo:shortName", request.getCompanyShortName())
                .set("$.cargo:address", request.getAddress())
                .set("$.cargo:category", request.getCategory())
                .set("$.cargo:contactPersons[0].cargo:firstName", request.getContactName())
                .set("$.cargo:contactPersons[0].cargo:salutation", request.getSalutation())
                .set("$.cargo:contactPersons[0].cargo:contactMethod", request.getContactMethod())
                .jsonString();
        NeOneServerCompanyHolder companyHolder = new NeOneServerCompanyHolder();
        companyHolder.setHost(host);
        companyHolder.setCompanyIri(companyIri);
        companyHolder.setCompanyId(companyId);
        companyHolder.setCompanyName(request.getCompanyName());
        companyHolder.setCompanyShortName(request.getCompanyShortName());
        companyHolder.setAddress(request.getAddress());
        companyHolder.setContactMethod(request.getContactMethod());
        companyHolder.setContactName(request.getContactName());
        companyHolder.setCompanyBody(companyBody);
        companyHolder.setCompanyStatus(CompanyStatus.INIT.name());
        companyHolder.setCategory(request.getCategory());
        companyHolder.setCompanyType(FromType.OTHER.name());

        companyHolder.setCompanyType(FromType.OTHER.name());
        companyHolder.setCompanyType(FromType.OTHER.name());
        companyHolder.setCompanyType(FromType.OTHER.name());
        companyHolder.setCompanyType(FromType.OTHER.name());
        // 2. 保存company info
        save(companyHolder);
        localCompanyRedisHelper.set(companyHolder);
        // 3.将company也保存到物流对象中
        String iri = generator.generateLogisticsObjectLoId(companyId, "");
        this.logisticsObjectsService.create(iri, companyBody, LoModuleType.LOGISTICS_COMPANY, FromType.CREATE_SELF);
        return companyHolder.getId();
    }

    @SuppressWarnings("all")
    @Transactional
    @Override
    public Long createMySelfServer(CompanyHolderRequest request) {
        NeOneServerCompanyHolder holder = getLocalServerNoCache();
        if (holder != null) {
            updateMySelfServer(request);
            return holder.getId();
        }
        //String serverAddressCode = createAddressCode(request);
        String companyId = UUIDTools.generateSimpleUUID();
        String host = request.getHost();
        //String host = this.neOneConfigProperties.isEnableMultiTenant() ? baseIri + serverAddressCode : baseIri;
        String companyIri = UrlFormatUtils.resolveUrl(request.getHost() + "/neone/logistics-objects/" + companyId);
        String pingBody = JsonPath.using(CONFIG).parse(pingJson)
                .set("$.@id", host)
                .set("$.api:hasServerEndpoint", host)
                .set("$.api:hasDataHolder.@id", UrlFormatUtils.resolveUrl(host + "/logistics-objects/" + companyId))
                .set("$.api:hasSupportedLogisticsObjectType", this.neOneConfigProperties.getHasSupportedLogisticsObjectTypes())
                .jsonString();
        // 1.保存server info
        NeOneServerInfo info = new NeOneServerInfo(host, companyId, companyIri, "", pingBody, FromType.CREATE_SELF);
        this.neOneServerInfoService.save(info);
        String companyBody = JsonPath.using(CONFIG).parse(companyJson)
                .set("$.cargo:name", request.getCompanyName())
                .set("$.cargo:shortName", request.getCompanyShortName())
                .set("$.cargo:address", request.getAddress())
                .set("$.cargo:category", request.getCategory())
                .set("$.cargo:contactPersons[0].cargo:firstName", request.getContactName())
                .set("$.cargo:contactPersons[0].cargo:salutation", request.getSalutation())
                .set("$.cargo:contactPersons[0].cargo:contactMethod", request.getContactMethod())
                .jsonString();

        NeOneServerCompanyHolder companyHolder = new NeOneServerCompanyHolder();
        companyHolder.setHost(host);
        companyHolder.setCompanyIri(companyIri);
        companyHolder.setCompanyId(companyId);
        companyHolder.setCompanyName(request.getCompanyName());
        companyHolder.setCompanyShortName(request.getCompanyShortName());
        companyHolder.setAddress(request.getAddress());
        companyHolder.setContactMethod(request.getContactMethod());
        companyHolder.setContactName(request.getContactName());
        companyHolder.setCompanyBody(companyBody);
        companyHolder.setCompanyStatus(CompanyStatus.INIT.name());
        companyHolder.setCategory(request.getCategory());
        companyHolder.setCompanyType(FromType.CREATE_SELF.name());
        // 2. 保存company info
        save(companyHolder);
        localCompanyRedisHelper.set(companyHolder);
        // 3.将company也保存到物流对象中
        String iri = generator.generateLogisticsObjectLoId(companyId, "");
        this.logisticsObjectsService.create(iri, companyBody, LoModuleType.LOGISTICS_COMPANY, FromType.CREATE_SELF);
        return companyHolder.getId();

    }

    @SuppressWarnings("all")
    @Transactional
    @Override
    public void updateMySelfServer(CompanyHolderRequest request) {
        NeOneServerCompanyHolder holder = getById(request.getId());
        //String serverAddressCode = createAddressCode(request);

        NeOneServerInfo serverInfo = this.neOneServerInfoService.getByCompanyId(holder.getCompanyId());
        serverInfo.setServerAddressCode("");
        this.neOneServerInfoService.updateById(serverInfo);

        String companyBody = JsonPath.using(CONFIG).parse(companyJson)
                .set("$.cargo:name", request.getCompanyName())
                .set("$.cargo:shortName", request.getCompanyShortName())
                .set("$.cargo:address", request.getAddress())
                .set("$.cargo:category", request.getCategory())
                .set("$.cargo:contactPersons[0].cargo:firstName", request.getContactName())
                .set("$.cargo:contactPersons[0].cargo:salutation", request.getSalutation())
                .set("$.cargo:contactPersons[0].cargo:contactMethod", request.getContactMethod())
                .jsonString();

        holder.setCompanyName(request.getCompanyName());
        holder.setCompanyShortName(request.getCompanyShortName());
        holder.setAddress(request.getAddress());
        holder.setContactMethod(request.getContactMethod());

        holder.setContactName(request.getContactName());
        holder.setCompanyBody(companyBody);
        holder.setCompanyStatus(CompanyStatus.INIT.name());
        holder.setCategory(request.getCategory());
        holder.setCompanyType(FromType.CREATE_SELF.name());
        holder.setUpdateTime(LocalDateTime.now());

        this.localCompanyRedisHelper.delete();

        updateById(holder);

        this.localCompanyRedisHelper.set(holder);

    }

    private String createAddressCode(CompanyHolderRequest request) {
        String serverAddressCode = request.getServerAddressCode();
        if (this.neOneConfigProperties.isEnableMultiTenant()) {
            if (StringUtils.isNotBlank(serverAddressCode) && (serverAddressCode.length() != 7 || containsDigit(serverAddressCode))) {
                throw new EftException("地址码过长，请输入7位字符串,不能包含数字,如果不填写，将由系统自动生成");
            }

            if (StringUtils.isNotBlank(serverAddressCode)) {
                NeOneServerInfo serverInfo = this.neOneServerInfoService.getByServiceAddressCode(serverAddressCode);
                if (serverInfo != null) {
                    throw new EftException("地址码已存在，请重新输入");
                }
            }

            if (StringUtils.isBlank(serverAddressCode)) {
                serverAddressCode = checkServerAddressCode(RandomStringGenerator.generateRandomString());
            }
        }
        return serverAddressCode;
    }

    @Transactional
    @Override
    public void removeCompanyInfo(Long id) {
        NeOneServerCompanyHolder holder = getById(id);
        String companyId = holder.getCompanyId();
        this.neOneServerInfoService.removeServer(companyId);
        this.logisticsObjectsService.removeLogisticsObject(companyId);
        removeById(holder.getId());
    }

    @Override
    public List<NeOneServerCompanyHolder> likeByName(String name) {
        LambdaQueryWrapper<NeOneServerCompanyHolder> wrapper = Wrappers.lambdaQuery();
        wrapper.like(NeOneServerCompanyHolder::getCompanyName, name);
        return list(wrapper);
    }


    private @NotNull NeOneServerCompanyHolder createCompanyHolder(String companyIri, String host, String companyId) {
        String companyServer = requestHelper.getCompanyServer(companyIri);
        String companyName = JsonPath.using(CONFIG).parse(companyServer).read("$.cargo:name");
        String companyShortName = JsonPath.using(CONFIG).parse(companyServer).read("$.cargo:shortName");
        //这里先只提取联系人吧
        List<String> contactPersons = JsonPath.using(CONFIG).parse(companyServer).read("$.cargo:contactPersons[*].cargo:firstName");
        List<String> salutations = JsonPath.using(CONFIG).parse(companyServer).read("$.cargo:contactPersons[*].cargo:salutation");
        List<String> contactMethods = JsonPath.using(CONFIG).parse(companyServer).read("$.cargo:contactPersons[*].cargo:contactMethod");

        String address = JsonPath.using(CONFIG).parse(companyServer).read("$.cargo:address");
        String category = JsonPath.using(CONFIG).parse(companyServer).read("$.cargo:category");
        NeOneServerCompanyHolder holder = new NeOneServerCompanyHolder();
        holder.setHost(host);
        holder.setCompanyIri(companyIri);
        holder.setCompanyId(companyId);

        holder.setCompanyName(companyName);
        holder.setCompanyShortName(companyShortName);
        holder.setAddress(StringUtils.isNotBlank(address) ? address : null);
        holder.setCategory(StringUtils.isNotBlank(category) ? category : null);

        holder.setContactMethod(!isEmpty(contactMethods) ? CollUtil.join(contactMethods, ",") : null);
        holder.setContactName(!isEmpty(contactPersons) ? CollUtil.join(contactPersons, ",") : null);
        holder.setSalutation(!isEmpty(salutations) ? CollUtil.join(salutations, ",") : null);
        holder.setCompanyBody(companyServer);
        holder.setCompanyStatus(CompanyStatus.INIT.name());
        return holder;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            companyJson = IOUtils.resourceToString("onerecord/company.json", StandardCharsets.UTF_8, NeOneCompanyServiceImpl.class.getClassLoader());
            pingJson = IOUtils.resourceToString("onerecord/ping.json", StandardCharsets.UTF_8, NeOneCompanyServiceImpl.class.getClassLoader());
        } catch (IOException e) {
            throw new OneRecordServerException("JSON 文件初始化失败", e);
        }
    }

    private NeOneServerInfo getByHost(String host) {
        return neOneServerInfoService.getByHost(host);
    }

    private String checkServerAddressCode(String addressCode) {
        for (int i = 0; i < 10; i++) {
            NeOneServerInfo serverInfo = this.neOneServerInfoService.getByServiceAddressCode(addressCode);
            if (serverInfo == null) {
                return addressCode;
            }
        }

        return RandomStringGenerator.generateRandomString(7);
    }

    private static boolean containsDigit(String str) {
        return str.matches(".*\\d.*");
    }

    private boolean isEmpty(Collection<String> collection) {
        if (CollectionUtils.isEmpty(collection)) {
            return true;
        }

        List<String> result = new ArrayList<>();
        for (String str : collection) {
            if (StringUtils.isBlank(str)) {
                continue;
            }

            result.add(str);
        }

        return CollectionUtils.isEmpty(result);
    }
}
