// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.client;

import com.efreight.base.module.one.record.neone.controller.onerecord.NotificationMessage;
import com.efreight.base.module.one.record.neone.model.onerecord.Subscription;
import org.eclipse.rdf4j.model.Model;

/**
 * One Record Client Interface
 * JAX-RS annotations removed - use RestTemplate or WebClient for implementation
 */
public interface OneRecordClient {

    Subscription proposeSubscription(String topic, String topicType);

    void notifySubscriber(NotificationMessage notification);

    Model getLogisticsObject(String id);
}
