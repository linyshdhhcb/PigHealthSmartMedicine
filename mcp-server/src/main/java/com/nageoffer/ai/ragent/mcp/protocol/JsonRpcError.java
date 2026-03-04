

package com.nageoffer.ai.ragent.mcp.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JSON-RPC 2.0 错误对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JsonRpcError {

    /**
     * 方法不存在
     */
    public static final int METHOD_NOT_FOUND = -32601;

    /**
     * 参数非法
     */
    public static final int INVALID_PARAMS = -32602;

    /**
     * 服务器内部错误
     */
    public static final int INTERNAL_ERROR = -32603;

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误消息
     */
    private String message;
}
