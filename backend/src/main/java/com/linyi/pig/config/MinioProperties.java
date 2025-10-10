package com.linyi.pig.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.minio")
public class MinioProperties {
    private String accessKey;
    private String secretKey;
    private String url;
    private String bucketName;
}
