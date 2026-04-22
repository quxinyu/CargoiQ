package com.efreight.base.module.one.record.neone.model.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @author qiuguan
 * @date 2024/09/17 09:49:47  星期二
 */
@Getter
@Setter
public abstract class BaseParam {

    public long total;

    public long size = 10;

    public long current = 1;
}
