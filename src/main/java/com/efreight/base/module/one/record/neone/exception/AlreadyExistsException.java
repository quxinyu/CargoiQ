//  Copyright Open Logistics Foundation
//
//  Licensed under the Open Logistics Foundation License 1.3.
//  For details on the licensing terms, see the LICENSE file.
//  SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.exception;

public class AlreadyExistsException extends RuntimeException {

    private final String resource;

    public AlreadyExistsException(String resource) {
        this.resource = resource;
    }

    public String getResource() {
        return resource;
    }
}
