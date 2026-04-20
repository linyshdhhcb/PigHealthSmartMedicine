package com.linyi.phsm.framework.exception;

import com.linyi.phsm.framework.errorcode.BaseErrorCode;
import com.linyi.phsm.framework.errorcode.ErrorCode;

/**
 * 远程服务调用异常
 * 比如订单调用支付失败，向上抛出的异常应该是远程服务调用异常
 */
public class RemoteException extends AbstractException {

    public RemoteException(String message) {
        this(message, null, BaseErrorCode.REMOTE_ERROR);
    }

    public RemoteException(String message, ErrorCode errorCode) {
        this(message, null, errorCode);
    }

    public RemoteException(String message, Throwable throwable, ErrorCode errorCode) {
        super(message, throwable, errorCode);
    }

    @Override
    public String toString() {
        return "RemoteException{" +
                "code='" + errorCode + "'," +
                "message='" + errorMessage + "'" +
                '}';
    }
}
