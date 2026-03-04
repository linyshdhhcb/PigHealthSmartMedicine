

package com.nageoffer.ai.ragent.mcp.protocol;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * MCP tools/list 返回的工具 Schema（符合 MCP 规范）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MCPToolSchema {

    /**
     * 工具名称，等于工具 ID
     */
    private String name;

    /**
     * 工具描述
     */
    private String description;

    /**
     * 输入参数 Schema
     */
    private InputSchema inputSchema;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InputSchema {

        /**
         * Schema 类型，默认 object
         */
        @Builder.Default
        private String type = "object";

        /**
         * 参数属性定义
         */
        private Map<String, PropertyDef> properties;

        /**
         * 必填参数列表
         */
        private List<String> required;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PropertyDef {

        /**
         * 参数类型，例如 string、number、boolean
         */
        private String type;

        /**
         * 参数说明
         */
        private String description;

        /**
         * 枚举候选值，对外序列化字段名为 enum
         */
        @com.google.gson.annotations.SerializedName("enum")
        private List<String> enumValues;
    }
}
