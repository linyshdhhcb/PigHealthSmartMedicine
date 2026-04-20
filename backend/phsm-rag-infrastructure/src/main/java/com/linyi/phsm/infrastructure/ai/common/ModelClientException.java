package com.linyi.phsm.infrastructure.ai.common;

import lombok.Getter;

/**
 * 模型客户端异常类
 * 用于封装模型调用过程中的各类异常信息
 */
@Getter
public class ModelClientException extends RuntimeException {

    /**
     * 错误类型
     */
    private final ModelClientErrorType errorType;

    /**
     * HTTP状态码
     */
    private final Integer statusCode;

    /**
     * 构造带原因的模型客户端异常
     *
     * @param message    异常消息
     * @param errorType  错误类型
     * @param statusCode HTTP状态码
     * @param cause      原始异常
     */
    public ModelClientException(String message, ModelClientErrorType errorType, Integer statusCode, Throwable cause) {
        super(message, cause);
        this.errorType = errorType;
        this.statusCode = statusCode;
    }

    /**
     * 构造模型客户端异常
     *
     * @param message    异常消息
     * @param errorType  错误类型
     * @param statusCode HTTP状态码
     */
    public ModelClientException(String message, ModelClientErrorType errorType, Integer statusCode) {
        super(message);
        this.errorType = errorType;
        this.statusCode = statusCode;
    }
}
