package com.efreight.base.module.one.record.neone.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.efreight.base.common.core.enmus.LoModuleType;
import com.efreight.base.common.core.enmus.MessageDataType;
import com.efreight.base.common.core.enmus.OneRecordParseVersionType;
import com.efreight.base.module.one.record.neone.model.entity.NeOneResolvedLogisticsObject;
import com.efreight.base.module.one.record.neone.model.objects.LoQueryRequestParam;
import com.efreight.base.module.one.record.neone.model.objects.OneRecordRequestBody;

import java.util.List;

/**
 * @author fu yuan hui
 * @since 2024-08-14 15:26:04 星期三
 */
public interface OneRecordResolvedLogisticsObjectService extends IService<NeOneResolvedLogisticsObject> {


    /**
     * 报文转One Record
     * @param report
     * @return
     */
    String uploadReport(String report, OneRecordParseVersionType parseVersion);

    /**
     * 报文转One Record
     * @param report
     * @return
     */
    String uploadReport(String report, MessageDataType messageDataType, OneRecordParseVersionType parseVersion);


    /**
     * 携带主单号
     * @param recordRequestBody
     * @return
     */
    String uploadOneRecordBody(OneRecordRequestBody recordRequestBody);

    /**
     * 根据主单号查询所有LO对象地址
     * @param masterCode
     * @return
     */
    List<String> listLos(String masterCode);

    List<String> listLos(String masterCode, LoModuleType type);

    IPage<?> pageList(Page<NeOneResolvedLogisticsObject> page, LoQueryRequestParam request);

}
