// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.model.onerecord;

import org.eclipse.rdf4j.model.IRI;

import java.util.Objects;

// 替换record为普通class，直接实现Referencable接口（复用项目已有接口，不创建内部类）
public class OperationObject implements Referencable {
    // record的属性转为private final，保证不可变性
    private final IRI iri;
    private final String datatype;
    private final String value;

    // 全参构造器（复刻record自动生成的构造逻辑）
    public OperationObject(IRI iri, String datatype, String value) {
        this.iri = iri;
        this.datatype = datatype;
        this.value = value;
    }

    // 仅保留必要的getter（供equals/hashCode访问属性）
    public IRI iri() {
        return iri;
    }

    public String datatype() {
        return datatype;
    }

    public String value() {
        return value;
    }

    // ========== 仅保留的核心方法：equals ==========
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OperationObject that = (OperationObject) o;
        return Objects.equals(iri, that.iri) &&
                Objects.equals(datatype, that.datatype) &&
                Objects.equals(value, that.value);
    }

    // ========== 仅保留的核心方法：hashCode ==========
    @Override
    public int hashCode() {
        return Objects.hash(iri, datatype, value);
    }

    // ========== 仅保留的核心方法：toString ==========
    @Override
    public String toString() {
        return "OperationObject{" +
                "iri=" + iri +
                ", datatype='" + datatype + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    // 注：Referencable、IRI为项目已有接口/类，无需在此定义
}
