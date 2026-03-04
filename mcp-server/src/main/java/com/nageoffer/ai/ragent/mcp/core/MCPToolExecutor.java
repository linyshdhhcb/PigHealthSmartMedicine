

package com.nageoffer.ai.ragent.mcp.core;

/**
 * MCP 工具执行器接口
 */
public interface MCPToolExecutor {

    /**
     * 获取工具定义信息
     * 返回该工具的元数据定义，包括工具ID、名称、描述、参数定义等信息
     *
     * @return 工具定义对象
     */
    MCPToolDefinition getToolDefinition();

    /**
     * 执行工具逻辑
     * 根据传入的请求参数执行具体的工具逻辑，并返回执行结果
     *
     * @param request 工具执行请求，包含执行所需的参数
     * @return 工具执行响应，包含执行结果和状态信息
     */
    MCPToolResponse execute(MCPToolRequest request);

    /**
     * 获取工具唯一标识
     * 默认实现从工具定义中获取工具ID，用于标识和查找工具
     *
     * @return 工具唯一标识符
     */
    default String getToolId() {
        return getToolDefinition().getToolId();
    }
}
