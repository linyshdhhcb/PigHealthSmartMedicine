package com.linyi.phsm.mcp.registry;

import com.linyi.phsm.mcp.model.ToolDefinition;
import com.linyi.phsm.mcp.tool.ToolExecutor;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultToolRegistry implements ToolRegistry {

    private final Map<String, ToolExecutor> executorMap = new ConcurrentHashMap<>();
    private final List<ToolExecutor> autoDiscoveredExecutors;

    @PostConstruct
    public void init() {
        if (autoDiscoveredExecutors == null || autoDiscoveredExecutors.isEmpty()) {
            log.info("MCP 工具注册跳过, 未发现任何工具执行器");
            return;
        }
        for (ToolExecutor executor : autoDiscoveredExecutors) {
            register(executor);
        }
        log.info("MCP 工具自动注册完成, 共注册 {} 个工具", autoDiscoveredExecutors.size());
    }

    @Override
    public void register(ToolExecutor executor) {
        String toolId = executor.getToolId();
        executorMap.put(toolId, executor);
        log.info("MCP 工具注册成功, toolId: {}", toolId);
    }

    @Override
    public Optional<ToolExecutor> getExecutor(String toolId) {
        return Optional.ofNullable(executorMap.get(toolId));
    }

    @Override
    public List<ToolDefinition> listAllTools() {
        return executorMap.values().stream()
                .map(ToolExecutor::getToolDefinition)
                .toList();
    }

    @Override
    public List<ToolExecutor> listAllExecutors() {
        return new ArrayList<>(executorMap.values());
    }
}
