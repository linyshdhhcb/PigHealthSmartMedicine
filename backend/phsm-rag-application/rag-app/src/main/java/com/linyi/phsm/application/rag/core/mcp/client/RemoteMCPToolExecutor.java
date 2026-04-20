package com.linyi.phsm.application.rag.core.mcp.client;

import com.linyi.phsm.application.rag.core.mcp.MCPRequest;
import com.linyi.phsm.application.rag.core.mcp.MCPResponse;
import com.linyi.phsm.application.rag.core.mcp.MCPTool;
import com.linyi.phsm.application.rag.core.mcp.MCPToolExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 远程 MCP 工具执行器
 * 实现 MCPToolExecutor 接口，通过 MCPClient 远程调用 MCP Server 上的工具
 */
@Slf4j
@RequiredArgsConstructor
public class RemoteMCPToolExecutor implements MCPToolExecutor {

    private final MCPClient mcpClient;
    private final MCPTool toolDefinition;

    @Override
    public MCPTool getToolDefinition() {
        return toolDefinition;
    }

    @Override
    public MCPResponse execute(MCPRequest request) {
        long start = System.currentTimeMillis();
        try {
            String result = mcpClient.callTool(toolDefinition.getToolId(), request.getParameters());
            long costMs = System.currentTimeMillis() - start;

            if (result == null) {
                MCPResponse response = MCPResponse.error(request.getToolId(), "REMOTE_CALL_FAILED", "远程工具调用失败");
                response.setCostMs(costMs);
                return response;
            }

            MCPResponse response = MCPResponse.success(request.getToolId(), result);
            response.setCostMs(costMs);
            return response;
        } catch (Exception e) {
            long costMs = System.currentTimeMillis() - start;
            String reason = e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
            log.warn("远程 MCP 工具调用异常, toolId={}, reason={}", request.getToolId(), reason);

            MCPResponse response = MCPResponse.error(request.getToolId(), "REMOTE_CALL_ERROR", reason);
            response.setCostMs(costMs);
            return response;
        }
    }
}
