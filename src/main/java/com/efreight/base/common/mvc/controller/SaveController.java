package com.efreight.base.common.mvc.controller;

import com.efreight.base.common.core.model.Result;
import com.efreight.base.common.core.utils.validate.annotation.group.Insert;
import com.efreight.base.common.mvc.entity.BaseEntity;
import com.efreight.base.common.mvc.entity.DTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 新增
 *
 * @param <Entity> 实体
 * @param <Dto>    保存参数
 * @author efreight
 * @date 2022-09-21
 */
public interface SaveController<Entity extends BaseEntity, Dto extends DTO<Entity>> extends BaseController<Entity> {

    /**
     * 新增
     *
     * @param dto 保存参数
     * @return
     */
    @ApiOperation(value = "新增")
    @PostMapping
    @Transactional
    default Result save(@RequestBody @Validated(Insert.class) Dto dto) {
        beforeSave(dto);
        getBaseService().doSave(dto);
        afterSave(dto);
        return Result.ok();
    }

    /**
     * 自定义新增前相关处理
     *
     * @param dto 保存对象
     */
    default void beforeSave(Dto dto) {
    }

    /**
     * 自定义新增后相关处理
     *
     * @param dto 保存对象
     */
    default void afterSave(Dto dto) {
    }
}
