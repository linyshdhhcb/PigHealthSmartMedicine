package com.linyi.phsm.mcp.registry;

import com.linyi.phsm.mcp.model.ToolDefinition;
import com.linyi.phsm.mcp.tool.ToolExecutor;

import java.util.List;
import java.util.Optional;

public interface ToolRegistry {

    void register(ToolExecutor executor);

    Optional<ToolExecutor> getExecutor(String toolId);

    List<ToolDefinition> listAllTools();

    List<ToolExecutor> listAllExecutors();
}
