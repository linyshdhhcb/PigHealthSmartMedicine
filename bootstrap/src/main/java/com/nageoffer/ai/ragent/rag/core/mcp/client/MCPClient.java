

package com.nageoffer.ai.ragent.rag.core.mcp.client;

import com.nageoffer.ai.ragent.rag.core.mcp.MCPTool;

import java.util.List;
import java.util.Map;

/**
 * MCP 协议客户端接口
 * 用于与远程 MCP Server 通信，遵循 MCP 协议标准（JSON-RPC 2.0）
 */
public interface MCPClient {

    /**
     * 初始化连接，获取 server 能力
     *
     * @return 初始化是否成功
     */
    boolean initialize();

    /**
     * 获取远程工具列表
     *
     * @return 工具定义列表
     */
    List<MCPTool> listTools();

    /**
     * 调用远程工具
     *
     * @param toolName  工具名称（即 toolId）
     * @param arguments 调用参数
     * @return 工具调用结果（文本形式）
     */
    String callTool(String toolName, Map<String, Object> arguments);
}
