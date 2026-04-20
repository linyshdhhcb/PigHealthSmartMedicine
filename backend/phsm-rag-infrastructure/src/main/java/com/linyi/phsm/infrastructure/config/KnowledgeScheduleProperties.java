package com.linyi.phsm.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

/**
 * 知识库定时任务配置
 */
@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = "rag.knowledge.schedule")
public class KnowledgeScheduleProperties {

    /**
     * 定时扫描间隔（毫秒）
     */
    private Long scanDelayMs = 10000L;

    /**
     * 分布式锁持有时长（秒）
     */
    private Long lockSeconds = 900L;

    /**
     * 每次扫描批量大小
     */
    private Integer batchSize = 20;

    /**
     * 定时拉取最小间隔（秒）
     */
    private Long minIntervalSeconds = 60L;

    /**
     * RUNNING 状态超时阈值（分钟），超过此时间未完成的文档重置为 FAILED
     */
    private Long runningTimeoutMinutes = 30L;
}
