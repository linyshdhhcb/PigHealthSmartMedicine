

package com.nageoffer.ai.ragent.mcp.endpoint;

import com.nageoffer.ai.ragent.mcp.protocol.JsonRpcRequest;
import com.nageoffer.ai.ragent.mcp.protocol.JsonRpcResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * MCP Streamable HTTP 端点
 * 提供 /mcp 端点接收 JSON-RPC 请求和通知
 */
@RestController
@RequiredArgsConstructor
public class MCPEndpoint {

    private final MCPDispatcher dispatcher;

    @PostMapping("/mcp")
    public ResponseEntity<?> handle(@RequestBody JsonRpcRequest request) {
        JsonRpcResponse response = dispatcher.dispatch(request);
        if (response == null) {
            // JSON-RPC Notification：无需响应体
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }
}
