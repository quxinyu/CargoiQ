package com.efreight.base.module.one.record.neone.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.efreight.base.module.one.record.neone.model.entity.NeOneLogisticsEvents;
import com.efreight.base.module.one.record.neone.model.entity.NeOneLogisticsObjects;
import com.efreight.base.module.one.record.neone.model.vo.LogisticsEventsRequestVO;

/**
 * @author fu yuan hui
 * @since 2024-09-18 11:23:28 星期三
 */
public interface NeOneLogisticsEventsService extends IService<NeOneLogisticsEvents> {

    void create(String loId, String uuid, String iri, String logisticsEventBody, String loIri);

    /**
     * 这个是拓展接口
     *
     * @param eventBody 事件请求体
     */
    String create(String eventBody);

    NeOneLogisticsObjects getLoByLoId(String loId);

    NeOneLogisticsEvents getByEventId(String eventId);

    NeOneLogisticsEvents getByEventIdAndLoId(String eventId, String loId);

    /**
     * 获取物流对象的所有物流事件信息
     *
     * @param logisticsEventsRequestVO 批量获取物流事件请求参数VO对象
     * @return 物流对象的所有物流事件信息
     */
    String getEvents(LogisticsEventsRequestVO logisticsEventsRequestVO);

}
