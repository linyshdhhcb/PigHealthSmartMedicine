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
public class ToolResponse {

    @Builder.Default
    private boolean success = true;

    private String toolId;

    @Builder.Default
    private Map<String, Object> data = new HashMap<>();

    private String textResult;

    private String errorMessage;

    private String errorCode;

    private long costMs;

    public static ToolResponse success(String toolId, String textResult) {
        return ToolResponse.builder()
                .success(true)
                .toolId(toolId)
                .textResult(textResult)
                .build();
    }

    public static ToolResponse success(String toolId, String textResult, Map<String, Object> data) {
        return ToolResponse.builder()
                .success(true)
                .toolId(toolId)
                .textResult(textResult)
                .data(data)
                .build();
    }

    public static ToolResponse error(String toolId, String errorCode, String errorMessage) {
        return ToolResponse.builder()
                .success(false)
                .toolId(toolId)
                .errorCode(errorCode)
                .errorMessage(errorMessage)
                .build();
    }
}
