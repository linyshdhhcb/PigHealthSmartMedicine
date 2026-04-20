package com.linyi.phsm.mcp.tool;

import com.linyi.phsm.mcp.model.ToolDefinition;
import com.linyi.phsm.mcp.model.ToolRequest;
import com.linyi.phsm.mcp.model.ToolResponse;

public interface ToolExecutor {

    ToolDefinition getToolDefinition();

    ToolResponse execute(ToolRequest request);

    default String getToolId() {
        return getToolDefinition().getToolId();
    }
}
