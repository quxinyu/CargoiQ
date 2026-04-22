package com.efreight.base.module.one.record.neone.model.vo;

import lombok.Data;

/**
 * 批量获取物流事件请求参数VO对象
 *
 * @author Ling; Jiatong
 * Date: 2025/4/14 14:50
 */
@Data
public class LogisticsEventsRequestVO {

    /**
     * 物流对象id
     */
    private String loId;

    /**
     * 物流事件代码
     */
    private String eventCode;

    /**
     * 筛选在某个时间点之后创建的事件
     */
    private String createdAfter;

    /**
     * 筛选在某个时间点之前创建的事件
     */
    private String createdBefore;

    /**
     * 筛选在某个时间点之后发生事件
     */
    private String occurredAfter;

    /**
     * 筛选在某个时间点之前发生事件
     */
    private String occurredBefore;

    /**
     * 排序规则
     */
    private String sort;

    /**
     * 返回的物流事件数量
     */
    private Integer limit;

    /**
     * 跳过的物流事件数量
     */
    private Integer skip;
}
