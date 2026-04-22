package com.efreight.base.common.mvc.injector;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.extension.injector.methods.AlwaysUpdateSomeColumnById;

import java.util.function.Predicate;

/**
 * 修改所有的字段
 *
 * @author efreight
 * @date 2022-09-19
 */
public class UpdateAllById extends AlwaysUpdateSomeColumnById {

    public UpdateAllById(Predicate<TableFieldInfo> predicate) {
        super("updateAllById", predicate);
    }
}
