package com.efreight.base.common.mvc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.efreight.base.common.mvc.entity.BaseEntity;
import com.efreight.base.common.mvc.entity.PageEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 基于MP的 BaseMapper 新增了2个方法： insertBatchSomeColumn、updateAllById
 * 新增list接口，支持分页查询和全集查询
 *
 * @param <T> 实体
 * @author efreight
 * @date 2022-09-21
 */
public interface SuperMapper<T extends BaseEntity> extends BaseMapper<T> {

    /**
     * 全量修改所有字段
     *
     * @param entity 实体
     * @return 修改数量
     */
    int updateAllById(@Param(Constants.ENTITY) T entity);

    /**
     * 批量插入所有字段
     * <p>
     * 只测试过MySQL！只测试过MySQL！只测试过MySQL！
     *
     * @param entityList 实体集合
     * @return 插入数量
     */
    int insertBatchSomeColumn(List<T> entityList);

    /**
     * 列表查询
     * @return
     */
    List<T> list(PageEntity query);
}
