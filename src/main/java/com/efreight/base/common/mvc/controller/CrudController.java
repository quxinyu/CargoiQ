package com.efreight.base.common.mvc.controller;

import com.efreight.base.common.mvc.entity.BaseEntity;
import com.efreight.base.common.mvc.entity.DTO;
import com.efreight.base.common.mvc.entity.PageEntity;
import com.efreight.base.common.mvc.service.SuperService;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;

/**
 * @author alex
 * @date 2022/09/19
 */
public abstract class CrudController<S extends SuperService<Entity, Dto>, Entity extends BaseEntity, PageQuery extends PageEntity<Entity>, Dto extends DTO<Entity>>
        implements SaveController<Entity, Dto>, UpdateController<Entity, Dto>, DeleteController<Entity>, QueryController<Entity, PageQuery>{

    Class<Entity> entityClass = null;
    @Autowired(required = false)
    protected S baseService;

    @Override
    public Class<Entity> getEntityClass() {
        if (entityClass == null) {
            this.entityClass = (Class<Entity>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        }
        return this.entityClass;
    }

    @Override
    public SuperService<Entity, Dto> getBaseService() {
        return baseService;
    }

}
