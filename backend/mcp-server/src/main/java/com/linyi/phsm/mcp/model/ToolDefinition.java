package com.linyi.phsm.mcp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToolDefinition {

    private String toolId;

    private String description;

    private Map<String, ParameterDef> parameters;

    @Builder.Default
    private boolean requireUserId = true;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ParameterDef {

        private String description;

        @Builder.Default
        private String type = "string";

        @Builder.Default
        private boolean required = false;

        private Object defaultValue;

        private List<String> enumValues;
    }
}
