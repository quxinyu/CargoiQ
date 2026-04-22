package com.efreight.base.module.one.record.neone.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.efreight.base.common.core.enmus.LoModuleType;
import com.efreight.base.common.core.enmus.OneRecordParseVersionType;
import com.efreight.base.module.one.record.neone.enums.FromType;
import com.efreight.base.module.one.record.neone.model.entity.NeOneLogisticsObjects;


/**
 * @author fu yuan hui
 * @since 2024-08-13 16:53:51 星期二
 */
public interface NeOneLogisticsObjectsService extends IService<NeOneLogisticsObjects> {

    String create(String iri, String oneRecordBody, LoModuleType type, FromType fromType);

    String create(String iri, String oneRecordBody, LoModuleType type, FromType fromType, OneRecordParseVersionType versionType);

    void createResolveBody(String iri, String oneRecordBody, FromType fromType);

    NeOneLogisticsObjects get(String iri);

    /**
     * @param loId 就是uuid
     * @return NeOneLogisticsObjectBase
     */
    NeOneLogisticsObjects getWithLoId(String loId);

    NeOneLogisticsObjects getServerInfo();

    void removeLogisticsObject(String loId);

    String updateLogisticsObject(String loId, String oneRecordUpdateBody);

    String auditTrail(String loId, String updateFrom, String updateTo, String status);
}
