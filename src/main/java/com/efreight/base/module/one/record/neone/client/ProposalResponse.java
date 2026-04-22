// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.client;

// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3
import java.time.Instant;
import java.util.Objects;

public class ProposalResponse {
    private final boolean subscribe;
    private final boolean sendLoBody;
    private final String includeEventType;
    private final Instant expiresAt;

    public ProposalResponse(boolean subscribe, boolean sendLoBody, String includeEventType, Instant expiresAt) {
        this.subscribe = subscribe;
        this.sendLoBody = sendLoBody;
        this.includeEventType = includeEventType;
        this.expiresAt = expiresAt;
    }

    public boolean subscribe() {
        return subscribe;
    }

    public boolean sendLoBody() {
        return sendLoBody;
    }

    public String includeEventType() {
        return includeEventType;
    }

    public Instant expiresAt() {
        return expiresAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProposalResponse that = (ProposalResponse) o;
        return subscribe == that.subscribe &&
                sendLoBody == that.sendLoBody &&
                Objects.equals(includeEventType, that.includeEventType) &&
                Objects.equals(expiresAt, that.expiresAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subscribe, sendLoBody, includeEventType, expiresAt);
    }

    @Override
    public String toString() {
        return "ProposalResponse{" +
                "subscribe=" + subscribe +
                ", sendLoBody=" + sendLoBody +
                ", includeEventType='" + includeEventType + '\'' +
                ", expiresAt=" + expiresAt +
                '}';
    }
}

