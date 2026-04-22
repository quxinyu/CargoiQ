package com.efreight.base.common.mvc.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.efreight.base.common.core.enmus.ResultStatusCode;
import com.efreight.base.common.core.exception.EftException;
import com.efreight.base.common.mvc.enmus.UpdateStrategy;
import com.efreight.base.common.mvc.entity.BaseEntity;
import com.efreight.base.common.mvc.entity.DTO;
import com.efreight.base.common.mvc.entity.PageEntity;
import com.efreight.base.common.mvc.mapper.SuperMapper;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;


/**
 * 经过封装的service实现
 *
 * @param <M> Mapper
 * @param <T> 实体
 * @param <Dto> 新增、编辑数据传输对象
 * @author efreight
 * @date 2022-09-21
 */
public class SuperServiceImpl<M extends SuperMapper<T>, T extends BaseEntity, Dto extends DTO<T>> extends ServiceImpl<M, T>
        implements SuperService<T, Dto> {

    private Class<T> entityClass = null;

    @Override
    public SuperMapper<T> getSuperMapper() {
        if (baseMapper instanceof SuperMapper) {
            return baseMapper;
        }
        throw new EftException(ResultStatusCode.SERVICE_MAPPER_ERROR);
    }

    @Override
    public Class<T> getEntityClass() {
        if (entityClass == null) {
            this.entityClass = (Class) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        }
        return this.entityClass;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean doSave(Dto dto) {
        T model = beforeSave(dto);
        boolean success = super.save(model);
        afterSave(dto, model);
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean doDelete(String[] ids) {
        beforeDelete(ids);
        boolean success = super.removeByIds(Arrays.asList(ids));
        afterDelete(ids);
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean doUpdate(Dto dto) {
        T model = beforeUpdate(dto);
        boolean success;
        if(updateStrategy() == UpdateStrategy.UPDATE_ALL) {
            success = updateAllById(model);
        }else {
            success = super.updateById(model);
        }
        afterUpdate(dto, model);
        return success;
    }

    @Override
    public List<T> list(PageEntity query) {
        return getSuperMapper().list(query);
    }
}
