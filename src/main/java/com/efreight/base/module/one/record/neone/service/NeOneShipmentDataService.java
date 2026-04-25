package com.efreight.base.module.one.record.neone.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.efreight.base.common.core.model.Result;
import com.efreight.base.module.one.record.neone.model.entity.NeOneShipmentData;
import com.efreight.base.module.one.record.neone.model.request.NeOneShipmentDataRequest;
import com.efreight.base.module.one.record.neone.model.request.NeOneShipmentSendRequest;

import java.util.List;

public interface NeOneShipmentDataService extends IService<NeOneShipmentData> {

    IPage<?> pageList(NeOneShipmentDataRequest req);

    Result<?> send(String id);

    Result<?> sendCheck(NeOneShipmentSendRequest request);

    void getObjectFromOneRecord(String oneRecordBody, String loId);

    void getObjectFromOneRecordEvent(String oneRecordBody);

    Result<?> check(List<NeOneShipmentSendRequest> request);

    Result<?> autoCheck(NeOneShipmentSendRequest request);

    Result<?> queryCheck(String id);

    Result<?> queryEhcContent(String id);

    Result<?> queryFsuEvent(String loId);
}
