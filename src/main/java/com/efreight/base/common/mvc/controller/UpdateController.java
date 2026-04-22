package com.efreight.base.common.mvc.controller;

import com.efreight.base.common.core.model.Result;
import com.efreight.base.common.core.utils.validate.annotation.group.Update;
import com.efreight.base.common.mvc.entity.BaseEntity;
import com.efreight.base.common.mvc.entity.DTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 修改Controller
 *
 * @param <Entity>    实体
 * @param <Dto>       修改参数
 * @author efreight
 * @date 2022-09-21
 */
public interface UpdateController<Entity extends BaseEntity, Dto extends DTO<Entity>> extends BaseController<Entity> {

    /**
     * 修改
     *
     * @param dto 修改DTO
     * @return
     */
    @ApiOperation(value = "修改", notes = "修改UpdateDTO中不为空的字段")
    @PutMapping
    @Transactional
    default Result update(@RequestBody @Validated(Update.class) Dto dto) {
        beforeUpdate(dto);
        getBaseService().doUpdate(dto);
        afterUpdate(dto);
        return Result.ok();
    }

    /**
     * 自定义更新前相关处理
     *
     * @param dto 修改的DTO
     */
    default void beforeUpdate(Dto dto) {}

    /**
     * 自定义更新后相关处理
     * @param dto 修改的DTO
     */
    default void afterUpdate(Dto dto) {}
}
