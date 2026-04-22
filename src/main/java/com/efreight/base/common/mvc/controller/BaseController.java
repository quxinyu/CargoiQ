package com.efreight.base.common.mvc.controller;

import com.efreight.base.common.mvc.entity.BaseEntity;
import com.efreight.base.common.mvc.service.SuperService;

/**
 * @author efreight
 * @date 2022/09/19
 */
public interface BaseController<Entity extends BaseEntity> {

    /**
     * 获取实体的类型
     *
     * @return 实体的类型
     */
    Class<Entity> getEntityClass();

    /**
     * 获取Service
     *
     * @return Service
     */
    SuperService getBaseService();
}
