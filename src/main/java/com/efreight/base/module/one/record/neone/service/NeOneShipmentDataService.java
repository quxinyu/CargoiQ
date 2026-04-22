package com.efreight.base.module.one.record.neone.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.efreight.base.common.core.model.Result;
import com.efreight.base.module.one.record.neone.model.entity.NeOneShipmentData;
import com.efreight.base.module.one.record.neone.model.request.NeOneShipmentDataRequest;
import com.efreight.base.module.one.record.neone.model.request.NeOneShipmentSendRequest;

public interface NeOneShipmentDataService extends IService<NeOneShipmentData> {

    IPage<?> pageList(NeOneShipmentDataRequest req);

    Result<?> send(String id);

    Result<?> sendCheck(NeOneShipmentSendRequest request);

    void getObjectFromOneRecord(String oneRecordBody);

}
