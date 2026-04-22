package com.efreight.base.module.one.record.neone.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.efreight.base.module.one.record.neone.enums.ActionRequestStatus;
import com.efreight.base.module.one.record.neone.ex.ActionRequestException;
import com.efreight.base.module.one.record.neone.ex.StatusMissingException;
import com.efreight.base.module.one.record.neone.helper.IriGenerator;
import com.efreight.base.module.one.record.neone.mapper.NeOneActionRequestsMapper;
import com.efreight.base.module.one.record.neone.model.entity.NeOneActionRequests;
import com.efreight.base.module.one.record.neone.service.ActionRequestService;
import com.google.common.eventbus.EventBus;
import com.jayway.jsonpath.JsonPath;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author fu yuan hui
 * @since 2024-09-09 17:09:35 星期一
 */
@RequiredArgsConstructor
@Service
public class ActionRequestServiceImpl extends ServiceImpl<NeOneActionRequestsMapper, NeOneActionRequests> implements ActionRequestService {

    private final EventBus asyncNeOneEventBus;

    private final IriGenerator iriGenerator;

    @Override
    public void saveActionRequest(NeOneActionRequests actionRequests) {
        this.save(actionRequests);
    }

    @Override
    public Optional<NeOneActionRequests> getWithIri(String iri) {
        LambdaQueryWrapper<NeOneActionRequests> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(NeOneActionRequests::getActionRequestIri, iri);

        return Optional.ofNullable(this.getOne(wrapper));
    }

    @Override
    public Optional<NeOneActionRequests> getWithActionRequestId(String actionRequestId) {
        LambdaQueryWrapper<NeOneActionRequests> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(NeOneActionRequests::getActionRequestId, actionRequestId);

        List<NeOneActionRequests> list = list(wrapper);
        return CollectionUtils.isEmpty(list) ? Optional.empty() : Optional.ofNullable(list.get(list.size() - 1));

    }

    @Override
    public String updateActionRequest(String actionRequestId, String status) {
        String resolveStatus = status;
        if (status.startsWith("http") || status.startsWith("https")) {
            String[] split = status.split("#");
            if (split.length != 2) {
                throw new StatusMissingException("status missing");
            }
            resolveStatus = split[1];
        }

        ActionRequestStatus actionRequestStatus = ActionRequestStatus.match(resolveStatus);

        if (ActionRequestStatus.REQUEST_PENDING == actionRequestStatus) {
            throw new ActionRequestException("The update request status is invalid", 400);
        }

        Optional<NeOneActionRequests> optional = getWithActionRequestId(actionRequestId);
        if (!optional.isPresent()
                || ActionRequestStatus.SUBSCRIPTION_REQUEST_REMOVED.name().equalsIgnoreCase(optional.get().getActionRequestStatus())) {
            throw new ActionRequestException("Action Request not found", 404);
        }
        NeOneActionRequests neOneActionRequests = optional.get();
        String actionResponseBody = neOneActionRequests.getActionResponseBody();
        String newBody = JsonPath.parse(actionResponseBody).set("$.api:hasRequestStatus.@id", "api:" + actionRequestStatus.name()).jsonString();
        neOneActionRequests.setActionRequestStatus(actionRequestStatus.name());
        neOneActionRequests.setActionResponseBody(newBody);
        neOneActionRequests.setUpdateTime(LocalDateTime.now());

        this.updateById(neOneActionRequests);
        this.asyncNeOneEventBus.post(neOneActionRequests);

        return neOneActionRequests.getActionRequestIri();
    }

    @Override
    public void deleteActionRequest(String actionRequestId) {
        Optional<NeOneActionRequests> optional = getWithActionRequestId(actionRequestId);
        if (!optional.isPresent()) {
            throw new ActionRequestException("Action Request not found", 404);
        }

        NeOneActionRequests neOneActionRequests = optional.get();
        neOneActionRequests.setActionRequestStatus(ActionRequestStatus.SUBSCRIPTION_REQUEST_REMOVED.name());
        neOneActionRequests.setUpdateTime(LocalDateTime.now());

        updateById(neOneActionRequests);
        this.asyncNeOneEventBus.post(neOneActionRequests.getActionRequestIri());
    }
}
