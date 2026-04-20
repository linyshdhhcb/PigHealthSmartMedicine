package com.linyi.phsm.mcp.protocol;

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
public class ToolSchema {

    private String name;

    private String description;

    private InputSchema inputSchema;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InputSchema {

        @Builder.Default
        private String type = "object";

        private Map<String, PropertyDef> properties;

        private List<String> required;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PropertyDef {

        private String type;

        private String description;

        @com.google.gson.annotations.SerializedName("enum")
        private List<String> enumValues;
    }
}
