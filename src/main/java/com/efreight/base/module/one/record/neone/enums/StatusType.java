package com.efreight.base.module.one.record.neone.enums;

/**
 * @author fu yuan hui
 */
public interface StatusType {


    int getStatusCode();


    Status.Family getFamily();

    String getReasonPhrase();

    default Status toEnum() {
        return Status.fromStatusCode(getStatusCode());
    }
}
