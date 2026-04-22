package com.efreight.base.common.mvc.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.efreight.base.common.core.model.PageParam;
import com.efreight.base.common.core.model.Result;
import com.efreight.base.common.mvc.entity.BaseEntity;
import com.efreight.base.common.mvc.entity.PageEntity;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.Serializable;
import java.util.List;

/**
 * 分页控制器
 *
 * @param <Entity> 实体
 * @param <Query>  查询对象
 * @author efreight
 * @date 2022-09-21
 */
public interface QueryController<Entity extends BaseEntity, Query extends PageEntity<Entity>> extends BaseController<Entity> {
    /**
     * 查询
     *
     * @param id 主键id
     * @return 查询结果
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", dataType = "long", paramType = "path"),
    })
    @ApiOperation(value = "单体查询", notes = "单体查询")
    @GetMapping("/{id}")
    default Result<Entity> get(@PathVariable Serializable id) {
        return Result.ok((Entity) getBaseService().getById(id));
    }

    /**
     * 分页查询
     *
     * @param params 分页参数
     * @param query  查询条件对象
     * @return 分页数据
     */
    @ApiOperation(value = "分页列表查询")
    @GetMapping(value = "/page")
    default Result<Page<Entity>> page(Query query, @Validated PageParam params) {
        beforePage(query);
        Page<Entity> page = query.initPage(params);
        page.setRecords(getBaseService().list(query));
        return Result.ok(page);
    }

    /**
     * 批量查询
     *
     * @param query 查询条件对象
     * @return 查询结果
     */
    @ApiOperation(value = "批量查询", notes = "批量查询")
    @PostMapping("/batchQuery")
    default Result<List<Entity>> query(@RequestBody Query query) {
        return Result.ok(getBaseService().list(query));
    }

    /**
     * 自定义查询前相关处理
     *
     * @param query 保存对象
     */
    default void beforePage(Query query) {
    }
}
