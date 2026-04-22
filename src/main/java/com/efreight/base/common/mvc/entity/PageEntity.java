package com.efreight.base.common.mvc.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.efreight.base.common.core.model.PageParam;
import com.efreight.base.common.datasource.entity.Pagination;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

/**
 * 分页查询对象需要继承此类，以获得分页能力
 * @author alex
 * @date 2022/09/20
 */
public abstract class PageEntity<T extends BaseEntity> extends BaseEntity{
    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    protected Pagination<T> page;

    public Page<T> initPage(PageParam pageParam) {
        page = new Pagination();
        if (pageParam == null) {
            return page;
        }
        page.setCurrent(pageParam.getCurrent());
        page.setSize(pageParam.getSize());

        return page;
    }
}
