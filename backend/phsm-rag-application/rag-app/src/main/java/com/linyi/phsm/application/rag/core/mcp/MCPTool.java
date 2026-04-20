package com.linyi.phsm.application.rag.core.mcp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * MCP 工具定义
 * <p>
 * 描述一个可被调用的外部工具/API，包含工具元信息和参数定义
 * 类似于 Function Calling 中的 function definition
 * <p>
 * 注意：
 * - name 和 examples 字段已移除，这些信息由意图树表（IntentNodeDO）管理
 * - 意图树负责意图识别阶段的匹配，MCPTool 负责参数提取和执行阶段
 * - 一个 MCPTool 可以对应多个意图节点，实现业务视角和技术视角的分离
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MCPTool {

    /**
     * 工具唯一标识（标准 MCP 字段）
     * 例如：attendance_query、approval_list、leave_balance
     */
    private String toolId;

    /**
     * 工具描述（标准 MCP 字段）
     * 用于参数提取阶段，LLM 根据此描述理解工具能力并提取参数
     */
    private String description;

    /**
     * 参数定义（标准 MCP 字段）
     * key: 参数名, value: 参数描述
     */
    private Map<String, ParameterDef> parameters;

    /**
     * 是否需要用户身份（调用时自动注入 userId）
     */
    @Builder.Default
    private boolean requireUserId = true;

    /**
     * MCP Server 地址（可选，用于远程调用）
     */
    private String mcpServerUrl;

    /**
     * 参数定义
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ParameterDef {

        /**
         * 参数描述
         */
        private String description;

        /**
         * 参数类型：string, number, boolean, array, object
         */
        @Builder.Default
        private String type = "string";

        /**
         * 是否必填
         */
        @Builder.Default
        private boolean required = false;

        /**
         * 默认值
         */
        private Object defaultValue;

        /**
         * 枚举值（可选）
         */
        private List<String> enumValues;
    }
}
