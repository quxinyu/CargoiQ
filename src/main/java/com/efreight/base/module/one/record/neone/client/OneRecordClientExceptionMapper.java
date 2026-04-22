//  Copyright Open Logistics Foundation
//
//  Licensed under the Open Logistics Foundation License 1.3.
//  For details on the licensing terms, see the LICENSE file.
//  SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.client;

import com.efreight.base.module.one.record.neone.model.onerecord.IdProvider;

//@ApplicationScoped
//public class OneRecordClientExceptionMapper implements ResponseExceptionMapper<RuntimeException> {
//
//    private final IdProvider idProvider;
//
//    @Inject
//    public OneRecordClientExceptionMapper(IdProvider idProvider) {
//        this.idProvider = idProvider;
//    }
//
//    @Override
//    public RuntimeException toThrowable(Response response) {
//        return switch (response.getStatusInfo().getFamily()) {
//            case SERVER_ERROR -> OneRecordClientException.requestFailed(idProvider, response);
//            case CLIENT_ERROR -> handleClientError(response);
//            default -> null;
//        };
//    }
//
//    private RuntimeException handleClientError(Response response) {
//        return switch (response.getStatus()) {
//            case 401 -> OneRecordClientException.unauthenticated(idProvider);
//            case 403 -> OneRecordClientException.unauthorized(idProvider);
//            case 404 -> OneRecordClientException.notFound(idProvider);
//            default -> OneRecordClientException.requestFailed(idProvider, response);
//        };
//    }
//}
