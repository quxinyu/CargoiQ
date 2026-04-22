package com.efreight.base.common.mvc.enmus;

/**
 * 更新策略
 * @author alex
 * @date 2022/09/19
 */
public enum UpdateStrategy {
    /**
     * 全字段覆盖更新
     */
    UPDATE_ALL,
    /**
     * 仅更新非空字段
     */
    UPDATE_NOT_NULL
}
