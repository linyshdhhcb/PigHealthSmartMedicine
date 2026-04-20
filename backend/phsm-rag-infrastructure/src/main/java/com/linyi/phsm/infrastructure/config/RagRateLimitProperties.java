package com.linyi.phsm.infrastructure.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class RagRateLimitProperties {

    @Value("${rag.rate-limit.global.enabled:false}")
    private Boolean globalEnabled;

    @Value("${rag.rate-limit.global.max-concurrent:1}")
    private Integer globalMaxConcurrent;

    @Value("${rag.rate-limit.global.max-wait-seconds:3}")
    private Integer globalMaxWaitSeconds;

    @Value("${rag.rate-limit.global.lease-seconds:30}")
    private Integer globalLeaseSeconds;

    @Value("${rag.rate-limit.global.poll-interval-ms:200}")
    private Integer globalPollIntervalMs;
}
