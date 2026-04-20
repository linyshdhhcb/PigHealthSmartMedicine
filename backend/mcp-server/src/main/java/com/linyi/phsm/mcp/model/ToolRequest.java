package com.linyi.phsm.mcp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToolRequest {

    private String toolId;

    private String userId;

    private String conversationId;

    private String userQuestion;

    @Builder.Default
    private Map<String, Object> parameters = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <T> T getParameter(String key) {
        Object value = parameters.get(key);
        return value != null ? (T) value : null;
    }

    public String getStringParameter(String key) {
        Object value = parameters.get(key);
        return value != null ? value.toString() : null;
    }
}
