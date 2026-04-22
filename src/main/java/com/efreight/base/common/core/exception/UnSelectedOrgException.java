package com.efreight.base.common.core.exception;

import com.efreight.base.common.core.enmus.ResultStatusCode;

/**
 * 未指定企业身份 异常
 * @author alex
 * @date 2022/09/15
 */
public class UnSelectedOrgException extends EftException{

    public UnSelectedOrgException() {
        super(ResultStatusCode.UN_SELECTED_ORG);
    }
}
