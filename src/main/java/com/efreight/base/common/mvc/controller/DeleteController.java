package com.efreight.base.common.mvc.controller;

import com.efreight.base.common.core.model.Result;
import com.efreight.base.common.mvc.entity.BaseEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;


/**
 * 删除Controller
 *
 * @param <Entity> 实体
 * @author efreight
 * @date 2022-09-21
 */
public interface DeleteController<Entity extends BaseEntity> extends BaseController<Entity> {

    /**
     * 删除方法
     *
     * @param ids id
     * @return 是否成功
     */
    @ApiOperation(value = "删除")
    @DeleteMapping("/{ids}")
    @Transactional
    default Result delete(@PathVariable String[] ids) {
        beforeDelete(ids);
        getBaseService().doDelete(ids);
        afterDelete(ids);
        return Result.ok();
    }

    /**
     * 自定义删除前相关处理
     *
     * @param ids id
     * @return
     */
    default void beforeDelete(String[] ids) {}

    /**
     * 自定义删除后相关处理
     */
    default void afterDelete() {}

    /**
     * 自定义删除后相关处理
     */
    default void afterDelete(String[] ids) {}
}
