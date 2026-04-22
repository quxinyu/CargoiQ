//  Copyright Open Logistics Foundation
//
//  Licensed under the Open Logistics Foundation License 1.3.
//  For details on the licensing terms, see the LICENSE file.
//  SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.controller.onerecord;

import com.efreight.base.module.one.record.neone.model.onerecord.LogisticsObject;
import com.efreight.base.module.one.record.neone.model.onerecord.Notification;

import java.util.Optional;

public class NotificationMessage {

    Notification notification;
    Optional<LogisticsObject> logisticsObject = Optional.empty();

    public Notification getNotification() {
        return notification;
    }

    public Optional<LogisticsObject> getLogisticsObject() {
        return logisticsObject;
    }

    public NotificationMessage withNotification(Notification notification) {
        this.notification = notification;
        return this;
    }

    public NotificationMessage withLogisticsObject(LogisticsObject lo) {
        this.logisticsObject = Optional.ofNullable(lo);
        return this;
    }
}
