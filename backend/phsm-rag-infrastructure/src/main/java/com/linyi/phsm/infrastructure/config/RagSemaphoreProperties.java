package com.linyi.phsm.infrastructure.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

/**
 * 分布式信号量配置
 */
@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = "rag.semaphore")
public class RagSemaphoreProperties {

    @Valid
    private PermitExpirableConfig documentUpload = new PermitExpirableConfig();

    @Data
    public static class PermitExpirableConfig {

        /**
         * Redisson 信号量名称
         */
        @NotBlank
        private String name = "rag:document:upload";

        /**
         * 最大并发数
         */
        @Min(1)
        private Integer maxConcurrent = 10;

        /**
         * 获取许可最大等待时间（秒）
         */
        @Min(0)
        private Integer maxWaitSeconds = 30;

        /**
         * permit 自动释放时间（秒）
         */
        @Min(1)
        private Integer leaseSeconds = 30;
    }
}
