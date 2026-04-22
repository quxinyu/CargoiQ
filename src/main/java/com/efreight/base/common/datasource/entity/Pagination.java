package com.efreight.base.common.datasource.entity;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;

/**
 * @author alex
 * @date 2022/09/16
 */

@ApiModel
public class Pagination<T> extends Page<T> {
}
