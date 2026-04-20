package com.linyi.phsm.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * RAG Trace 配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "rag.trace")
public class RagTraceProperties {

    /**
     * 是否启用注解式 Trace 采集
     */
    private boolean enabled = true;

    /**
     * 错误信息最大长度，防止落库过大
     */
    private int maxErrorLength = 1000;
}
