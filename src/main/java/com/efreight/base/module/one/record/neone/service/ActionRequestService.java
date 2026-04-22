package com.efreight.base.module.one.record.neone.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.efreight.base.module.one.record.neone.model.entity.NeOneActionRequests;

import java.util.Optional;

/**
 * @author fu yuan hui
 * @since 2024-09-09 17:09:25 星期一
 */
public interface ActionRequestService extends IService<NeOneActionRequests> {

    void saveActionRequest(NeOneActionRequests actionRequests);

    Optional<NeOneActionRequests> getWithIri(String iri);

    Optional<NeOneActionRequests> getWithActionRequestId(String actionRequestId);

    String updateActionRequest(String actionRequestId, String status);

    void deleteActionRequest(String actionRequestId);

}
