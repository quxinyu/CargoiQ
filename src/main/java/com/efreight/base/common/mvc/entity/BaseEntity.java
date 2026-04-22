package com.efreight.base.common.mvc.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 数据库实体类需要继承此类
 * @author alex
 * @date 2022/09/20
 */
@Data
@NoArgsConstructor
public abstract class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
}
