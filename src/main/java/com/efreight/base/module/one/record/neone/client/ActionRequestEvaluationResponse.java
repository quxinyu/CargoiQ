//  Copyright Open Logistics Foundation
//
//  Licensed under the Open Logistics Foundation License 1.3.
//  For details on the licensing terms, see the LICENSE file.
//  SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.client;

public class ActionRequestEvaluationResponse {

    private String actionRequestStatus;

    public ActionRequestEvaluationResponse() {
    }

    public ActionRequestEvaluationResponse(String actionRequestStatus) {
        this.actionRequestStatus = actionRequestStatus;
    }

    public String getActionRequestStatus() {
        return actionRequestStatus;
    }

    public void setActionRequestStatus(String actionRequestStatus) {
        this.actionRequestStatus = actionRequestStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActionRequestEvaluationResponse that = (ActionRequestEvaluationResponse) o;

        return actionRequestStatus != null ? actionRequestStatus.equals(that.actionRequestStatus) : that.actionRequestStatus == null;
    }

    @Override
    public int hashCode() {
        return actionRequestStatus != null ? actionRequestStatus.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ActionRequestEvaluationResponse{" +
                "actionRequestStatus='" + actionRequestStatus + '\'' +
                '}';
    }
}
