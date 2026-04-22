package com.efreight.base.common.core.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author alex
 * @date 2022/04/15
 */
@Data
public class PageParam {
    @Min(1)
    @ApiModelProperty("当前所在页")
    private Integer current = 1;
    @Min(1)
    @ApiModelProperty("每页显示数量")
    private Integer size = 10;
}
