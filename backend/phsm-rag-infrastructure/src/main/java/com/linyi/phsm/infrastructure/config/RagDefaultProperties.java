package com.linyi.phsm.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "rag.default")
public class RagDefaultProperties {

    private String collectionName;

    private Integer dimension;

    private String metricType;
}
