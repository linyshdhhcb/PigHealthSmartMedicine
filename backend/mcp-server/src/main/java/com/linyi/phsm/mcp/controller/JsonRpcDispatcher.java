package com.linyi.phsm.mcp.controller;

import com.linyi.phsm.mcp.model.ToolDefinition;
import com.linyi.phsm.mcp.model.ToolRequest;
import com.linyi.phsm.mcp.model.ToolResponse;
import com.linyi.phsm.mcp.protocol.JsonRpcError;
import com.linyi.phsm.mcp.protocol.JsonRpcRequest;
import com.linyi.phsm.mcp.protocol.JsonRpcResponse;
import com.linyi.phsm.mcp.protocol.ToolSchema;
import com.linyi.phsm.mcp.registry.ToolRegistry;
import com.linyi.phsm.mcp.tool.ToolExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JsonRpcDispatcher {

    private final ToolRegistry toolRegistry;

    public JsonRpcResponse dispatch(JsonRpcRequest request) {
        String method = request.getMethod();
        Object id = request.getId();

        if (id == null) {
            log.debug("MCP notification received: {}", method);
            return null;
        }

        return switch (method) {
            case "initialize" -> handleInitialize(id);
            case "tools/list" -> handleToolsList(id);
            case "tools/call" -> handleToolsCall(id, request.getParams());
            default -> JsonRpcResponse.error(id, JsonRpcError.METHOD_NOT_FOUND, "Unknown method: " + method);
        };
    }

    private JsonRpcResponse handleInitialize(Object id) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("protocolVersion", "2026-02-28");

        Map<String, Object> capabilities = new LinkedHashMap<>();
        capabilities.put("tools", Map.of("listChanged", false));
        result.put("capabilities", capabilities);

        Map<String, Object> serverInfo = new LinkedHashMap<>();
        serverInfo.put("name", "ragent-mcp-server");
        serverInfo.put("version", "0.0.1");
        result.put("serverInfo", serverInfo);

        return JsonRpcResponse.success(id, result);
    }

    private JsonRpcResponse handleToolsList(Object id) {
        List<ToolDefinition> tools = toolRegistry.listAllTools();

        List<ToolSchema> schemas = tools.stream().map(this::toSchema).toList();

        return JsonRpcResponse.success(id, Map.of("tools", schemas));
    }

    private JsonRpcResponse handleToolsCall(Object id, Map<String, Object> params) {
        if (params == null || !params.containsKey("name") || params.get("name") == null) {
            return JsonRpcResponse.error(id, JsonRpcError.INVALID_PARAMS, "Missing 'name' in params");
        }

        String toolName = String.valueOf(params.get("name"));

        Optional<ToolExecutor> executorOpt = toolRegistry.getExecutor(toolName);
        if (executorOpt.isEmpty()) {
            return JsonRpcResponse.error(id, JsonRpcError.METHOD_NOT_FOUND, "Tool not found: " + toolName);
        }

        Map<String, Object> arguments = new HashMap<>();
        Object rawArguments = params.get("arguments");
        if (rawArguments instanceof Map<?, ?> argMap) {
            for (Map.Entry<?, ?> entry : argMap.entrySet()) {
                if (entry.getKey() != null) {
                    arguments.put(String.valueOf(entry.getKey()), entry.getValue());
                }
            }
        }

        ToolRequest toolRequest = ToolRequest.builder()
                .toolId(toolName)
                .parameters(arguments)
                .build();

        try {
            ToolResponse toolResponse = executorOpt.get().execute(toolRequest);

            List<Map<String, Object>> content = new ArrayList<>();
            Map<String, Object> textContent = new LinkedHashMap<>();
            textContent.put("type", "text");
            textContent.put("text", toolResponse.getTextResult() != null ? toolResponse.getTextResult() : "");
            content.add(textContent);

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("content", content);
            result.put("isError", !toolResponse.isSuccess());

            return JsonRpcResponse.success(id, result);
        } catch (Exception e) {
            log.error("Tool execution failed: {}", toolName, e);

            List<Map<String, Object>> content = new ArrayList<>();
            Map<String, Object> textContent = new LinkedHashMap<>();
            textContent.put("type", "text");
            textContent.put("text", "工具调用异常: " + e.getMessage());
            content.add(textContent);

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("content", content);
            result.put("isError", true);

            return JsonRpcResponse.success(id, result);
        }
    }

    private ToolSchema toSchema(ToolDefinition def) {
        Map<String, ToolSchema.PropertyDef> properties = new LinkedHashMap<>();
        List<String> required = new ArrayList<>();

        if (def.getParameters() != null) {
            def.getParameters().forEach((name, paramDef) -> {
                properties.put(name, ToolSchema.PropertyDef.builder()
                        .type(paramDef.getType())
                        .description(paramDef.getDescription())
                        .enumValues(paramDef.getEnumValues())
                        .build());
                if (paramDef.isRequired()) {
                    required.add(name);
                }
            });
        }

        return ToolSchema.builder()
                .name(def.getToolId())
                .description(def.getDescription())
                .inputSchema(ToolSchema.InputSchema.builder()
                        .type("object")
                        .properties(properties)
                        .required(required.isEmpty() ? null : required)
                        .build())
                .build();
    }
}
