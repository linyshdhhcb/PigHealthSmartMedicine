

package com.nageoffer.ai.ragent.rag.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

/**
 * RustFS S3 客户端配置类
 * 用于配置和初始化与 RustFS 对象存储服务交互的 S3 客户端
 */
@Configuration
public class RestFSS3Config {

    /**
     * 创建 S3 客户端 Bean
     *
     * @param rustfsUrl       RustFS 服务的访问地址
     * @param accessKeyId     访问密钥 ID
     * @param secretAccessKey 访问密钥
     * @return 配置完成的 S3Client 实例
     */
    @Bean
    public S3Client s3Client(@Value("${rustfs.url}") String rustfsUrl,
                             @Value("${rustfs.access-key-id}") String accessKeyId,
                             @Value("${rustfs.secret-access-key}") String secretAccessKey) {
        // 构建 S3 客户端实例
        S3Client s3 = S3Client.builder()
                // 设置 RustFS 服务的访问端点
                .endpointOverride(URI.create(rustfsUrl))
                // 可写死，RustFS 不校验 region
                .region(Region.US_EAST_1)
                // 配置静态凭证提供者，用于身份验证
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(accessKeyId, secretAccessKey)
                        )
                )
                // 关键配置！RustFS 需启用 Path-Style 访问方式
                .forcePathStyle(true)
                .build();
        return s3;
    }
}
