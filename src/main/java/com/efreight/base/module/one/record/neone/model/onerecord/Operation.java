// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.model.onerecord;

import org.eclipse.rdf4j.model.IRI;

import java.util.Objects;

public class Operation implements Referencable {
    // record的所有属性转为private final，保证不可变性
    private final IRI iri;
    private final OperationObject o;
    private final PatchOperation op;
    private final IRI p;
    private final String s;

    // 全参构造器（复刻record的自动构造逻辑）
    public Operation(IRI iri, OperationObject o, PatchOperation op, IRI p, String s) {
        this.iri = iri;
        this.o = o;
        this.op = op;
        this.p = p;
        this.s = s;
    }

    // 仅保留必要的getter（供equals/hashCode访问属性）
    public IRI iri() {
        return iri;
    }

    public OperationObject o() {
        return o;
    }

    public PatchOperation op() {
        return op;
    }

    public IRI p() {
        return p;
    }

    public String s() {
        return s;
    }

    // ========== 仅保留的核心方法：equals ==========
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operation operation = (Operation) o;
        return Objects.equals(iri, operation.iri) &&
                Objects.equals(this.o, operation.o) &&
                Objects.equals(op, operation.op) &&
                Objects.equals(p, operation.p) &&
                Objects.equals(s, operation.s);
    }

    // ========== 仅保留的核心方法：hashCode ==========
    @Override
    public int hashCode() {
        return Objects.hash(iri, o, op, p, s);
    }

    // ========== 仅保留的核心方法：toString ==========
    @Override
    public String toString() {
        return "Operation{" +
                "iri=" + iri +
                ", o=" + o +
                ", op=" + op +
                ", p=" + p +
                ", s='" + s + '\'' +
                '}';
    }

    // 注：Referencable、IRI、OperationObject、PatchOperation为项目已有接口/类，无需在此定义
}
