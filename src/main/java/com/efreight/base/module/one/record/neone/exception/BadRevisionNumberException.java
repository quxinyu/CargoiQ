//  Copyright Open Logistics Foundation
//
//  Licensed under the Open Logistics Foundation License 1.3.
//  For details on the licensing terms, see the LICENSE file.
//  SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.exception;

public class BadRevisionNumberException extends RuntimeException {

    public final int badRevisionNumber;

    public BadRevisionNumberException(int revisionNumber) {
        this.badRevisionNumber = revisionNumber;
    }
}

