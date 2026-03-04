

package com.nageoffer.ai.ragent.rag.config;

import com.nageoffer.ai.ragent.rag.config.validation.ValidMemoryConfig;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

/**
 * 记忆配置属性类
 * 用于配置 RAG 系统中的对话记忆管理相关参数
 * 包括历史轮数保留、缓存时间、摘要压缩等功能的配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "rag.memory")
@Validated
@ValidMemoryConfig
public class MemoryProperties {

    /**
     * 保留原文的最近轮数（user+assistant 视为一轮）
     */
    @Min(1)
    @Max(100)
    private Integer historyKeepTurns = 8;

    /**
     * 缓存过期时间（分钟）
     */
    private Integer ttlMinutes = 60;

    /**
     * 是否启用对话记忆压缩
     */
    private Boolean summaryEnabled = false;

    /**
     * 开始摘要的轮数阈值
     */
    private Integer summaryStartTurns = 9;

    /**
     * 摘要最大字数
     */
    @Min(200)
    @Max(1000)
    private Integer summaryMaxChars = 200;

    /**
     * 会话标题最大长度（用于提示词约束）
     */
    @Min(10)
    @Max(100)
    private Integer titleMaxLength = 30;
}
