package com.linyi.phsm.mcp.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * JSON-RPC 2.0 请求
 * <p>
 * 对应 HTTP POST /mcp 的请求体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JsonRpcRequest {

    /**
     * 协议版本，固定为 2.0
     */
    private String jsonrpc = "2.0";

    /**
     * 请求 ID，通知请求可为空
     */
    private Object id;

    /**
     * 调用方法名，例如 initialize、tools/list、tools/call
     */
    private String method;

    /**
     * 方法参数，key 为参数名，value 为参数值
     */
    private Map<String, Object> params;
}
