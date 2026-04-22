// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.security;

import org.eclipse.rdf4j.model.IRI;

/**
 * 访问主体
 * <p>
 * 简单的值对象，表示当前访问者的身份
 * <p>
 * 不作为 Spring Bean，而是手动创建或通过工厂方法创建
 *
 * @author quuxinyu
 * @since 2025-01-09
 */
public class AccessSubject {

    private final IRI iri;

    /**
     * 构造函数
     *
     * @param iri 访问者的 IRI
     */
    public AccessSubject(IRI iri) {
        this.iri = iri;
    }

    /**
     * 获取访问者的 IRI
     *
     * @return IRI
     */
    public IRI iri() {
        return iri;
    }

    /**
     * 创建 AccessSubject 的工厂方法
     *
     * @param iri 访问者的 IRI
     * @return AccessSubject 实例
     */
    public static AccessSubject from(IRI iri) {
        return new AccessSubject(iri);
    }
}
