// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.controller.onerecord;

import com.efreight.base.module.one.record.neone.annotations.Authenticated;
import com.efreight.base.module.one.record.neone.service.onerecord.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Authenticated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/" + NotificationController.RESOURCE_NAME,
        consumes = {"application/ld+json", "application/x-turtle", "text/turtle"},
        produces = {"application/ld+json"})
public class NotificationController {

    public static final String RESOURCE_NAME = "v2/notifications";
    @ConfigProperty(name = "notification.forward", defaultValue = "false")
    boolean forwardNotifications;

    private final NotificationService notificationService;


    @PostMapping
    public ResponseEntity<?> handleNotification(@RequestBody NotificationMessage notification) {
        log.info("Received notification [{}]", notification);
        if (forwardNotifications) {
            log.info("Forwarding notification [{}]", notification);
            notificationService.forward(notification);
        }
        notificationService.invalidateCachedLogisticsObject(notification);
        return ResponseEntity.noContent().build();
    }
}
