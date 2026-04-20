package com.linyi.phsm.infrastructure.ai.common;

/**
 * 模型客户端错误类型枚举
 * <p>
 * 定义了与AI模型服务交互过程中可能遇到的各种错误类型，
 * 用于统一错误分类和处理策略
 */
public enum ModelClientErrorType {

    /**
     * 未授权错误 - 认证失败或令牌无效
     */
    UNAUTHORIZED,

    /**
     * 速率限制错误 - 请求频率超过限制
     */
    RATE_LIMITED,

    /**
     * 服务器错误 - 模型服务端内部错误
     */
    SERVER_ERROR,

    /**
     * 客户端错误 - 请求参数或格式错误
     */
    CLIENT_ERROR,

    /**
     * 网络错误 - 网络连接或超时问题
     */
    NETWORK_ERROR,

    /**
     * 无效响应 - 模型返回的响应格式不正确
     */
    INVALID_RESPONSE,

    /**
     * 供应商错误 - 模型提供商服务错误
     */
    PROVIDER_ERROR;

    /**
     * 根据 HTTP 状态码推断错误类型
     *
     * @param status HTTP 状态码
     * @return 对应的错误类型
     */
    public static ModelClientErrorType fromHttpStatus(int status) {
        if (status == 401 || status == 403) {
            return UNAUTHORIZED;
        }
        if (status == 429) {
            return RATE_LIMITED;
        }
        if (status >= 500) {
            return SERVER_ERROR;
        }
        return CLIENT_ERROR;
    }
}
