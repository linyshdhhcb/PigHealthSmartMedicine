package com.linyi.phsm.domain.ingestion.model.settings;

import com.linyi.phsm.domain.ingestion.model.enums.EnhanceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 增强器设置实体类
 * 定义文档增强节点的配置参数，包括使用的模型ID和增强任务列表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnhancerSettings {

    /**
     * 用于增强处理的模型ID
     */
    private String modelId;

    /**
     * 要执行的增强任务列表
     */
    private List<EnhanceTask> tasks;

    /**
     * 增强任务配置
     * 定义单个增强任务的类型和提示词配置
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class EnhanceTask {

        /**
         * 增强任务类型
         */
        private EnhanceType type;

        /**
         * 系统提示词
         */
        private String systemPrompt;

        /**
         * 用户提示词模板
         * 支持变量替换
         */
        private String userPromptTemplate;
    }
}
