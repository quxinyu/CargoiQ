package com.efreight.base.common.core.exception;

/**
 * 对于不正常数据专用异常类
 * 适用场景：
 * 1.理论应返回数据，但是未返回数据的
 * 2.状态校验不通过的情况
 * @author alex
 * @date 2023/05/25
 */
public class EftDataErrorException extends EftException{

    public EftDataErrorException() {
        super("数据异常！");
    }
}
