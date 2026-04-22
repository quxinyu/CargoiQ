//  Copyright Open Logistics Foundation
//
//  Licensed under the Open Logistics Foundation License 1.3.
//  For details on the licensing terms, see the LICENSE file.
//  SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.service.onerecord;

import com.efreight.base.module.one.record.neone.client.RestClientBuilderProducer;
import com.efreight.base.module.one.record.neone.handler.NotificationHandler;
import com.efreight.base.module.one.record.neone.model.onerecord.ActionRequest;
import com.efreight.base.module.one.record.neone.model.onerecord.Notification;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.net.URI;

@Component
@ApplicationScoped
public class ActionRequestNotificationService {

    private static final Logger log = LoggerFactory.getLogger(ActionRequestNotificationService.class);

    @ConfigProperty(name = "quarkus.rest-client.action-request-client.connect-timeout")
    int connectTimeout;

    @ConfigProperty(name = "quarkus.rest-client.action-request-client.read-timeout")
    int readTimeout;

    private final RestClientBuilderProducer restClientBuilderProducer;

    @Inject
    public ActionRequestNotificationService(
            RestClientBuilderProducer restClientBuilderProducer,
            NotificationHandler notificationHandler) {
        this.restClientBuilderProducer = restClientBuilderProducer;
    }

    public void notifyRequestor(ActionRequest actionRequest, Notification notification) {
        String requestedBy = actionRequest.requestedBy().stringValue();
        int idx = requestedBy.lastIndexOf("/logistics-objects");
        if (idx >= 0) {
            String base = requestedBy.substring(0, idx);
            URI address = URI.create(base);
            sendNotification(address, notification);
        }
    }

    /**
     * 发送通知？
     *
     * @param address
     * @param notification
     */
    private void sendNotification(URI address, Notification notification) {
//        var client = restClientBuilderProducer.restClientBuilder()
//            .baseUri(address)
//            .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
//            .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
//            .build(OneRecordClient.class);
//        var response = client.notifySubscriber(new NotificationMessage().withNotification(notification));
//        response.close();
    }
}
