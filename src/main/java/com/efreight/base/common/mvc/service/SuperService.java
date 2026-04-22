package com.efreight.base.common.mvc.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.efreight.base.common.core.enmus.ResultStatusCode;
import com.efreight.base.common.core.exception.EftException;
import com.efreight.base.common.mvc.enmus.UpdateStrategy;
import com.efreight.base.common.mvc.entity.BaseEntity;
import com.efreight.base.common.mvc.entity.DTO;
import com.efreight.base.common.mvc.entity.PageEntity;
import com.efreight.base.common.mvc.mapper.SuperMapper;

import java.io.Serializable;
import java.util.List;

/**
 * 基于MP的 IService 扩展了2个实现方法： saveBatchSomeColumn、updateAllById
 * 新增doSave、doUpdate、doDelete业务类方法和默认实现，并提供环绕方法以供扩展
 * 新增list方法，支持分页查询和全集查询
 * 其中doUpdate方法提供额外的策略方法支持 updateStrategy()
 *
 * @param <T> 实体
 * @param <Dto> 新增、编辑数据传输对象
 * @author efreight
 * @date 2022-09-21
 */
public interface SuperService<T extends BaseEntity, Dto extends DTO<T>> extends IService<T> {
    /**
     * 获取实体的类型
     *
     * @return
     */
    @Override
    Class<T> getEntityClass();

    SuperMapper<T> getSuperMapper();

    /**
     * 根据id修改 entity 的所有字段
     * 扩展出来的非业务方法，主要区别于mp的updateById方法
     * updateById仅更新非空字段
     * updateAllById会更新所有字段
     *
     * @param entity
     * @return
     */
    default boolean updateAllById(T entity) {
        return SqlHelper.retBool(getSuperMapper().updateAllById(entity));
    };

    /**
     * 批量保存数据
     * <p>
     * 注意：该方法仅仅测试过mysql
     *
     * @param entityList
     * @return
     */
    default boolean saveBatchSomeColumn(List<T> entityList) {
        if (entityList.isEmpty()) {
            return true;
        }
        if (entityList.size() > 5000) {
            throw new EftException(ResultStatusCode.TOO_MUCH_DATA_ERROR);
        }
        return SqlHelper.retBool(((SuperMapper) getBaseMapper()).insertBatchSomeColumn(entityList));
    }

    /**
     * 指定更新策略
     * @return
     */
    default UpdateStrategy updateStrategy(){
        return UpdateStrategy.UPDATE_NOT_NULL;
    }

    /**
     * 列表查询（如果传了page对象则是分页查询，否则查询全集）
     * @param query
     * @return
     */
    List<T> list(PageEntity query);

    /**
     * 自定义业务保存方法，区别于mp的save
     * @param dto
     * @return
     */
    boolean doSave(Dto dto);

    /**
     * 自定义业务更新方法，区别于mp的update相关方法
     * @param dto
     * @return
     */
    boolean doUpdate(Dto dto);

    /**
     * 自定义业务删除方法，区别于mp的remove方法
     * @return
     */
    boolean doDelete(String[] ids);

    /**
     * 新增前相关处理
     *
     * @param dto 实体
     * @return
     */
    default T beforeSave(Dto dto) {
        return BeanUtil.toBean(dto, getEntityClass());
    }

    /**
     * 新增后相关处理
     * @param dto
     * @param model
     */
    default void afterSave(Dto dto, T model) {}

    /**
     * 修改前相关处理
     * @param dto
     * @return
     */
    default T beforeUpdate(Dto dto) {
        return BeanUtil.toBean(dto, getEntityClass());
    }

    /**
     * 修改后相关处理
     * @param dto
     * @param model
     */
    default void afterUpdate(Dto dto, T model) {}

    /**
     * 删除前相关处理
     *
     * @param ids id集合
     */
    default void beforeDelete(String[] ids) {}

    /**
     * 删除后相关处理
     * @param ids id集合
     */
    default void afterDelete(String[] ids) {}
}
