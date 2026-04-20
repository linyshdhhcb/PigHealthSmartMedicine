package com.linyi.phsm.bootstrap.config;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * HTTP客户端配置类
 * 用于配置OkHttpClient的全局实例，设置连接超时、读取超时和写入超时等参数
 */
@Configuration
public class HttpClientConfig {

    /**
     * 创建并配置OkHttpClient实例
     *
     * @return 配置好的OkHttpClient实例
     */
    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(Duration.ofSeconds(30))
                .writeTimeout(Duration.ofSeconds(60))
                .readTimeout(Duration.ZERO)
                .callTimeout(Duration.ZERO)
                .retryOnConnectionFailure(true)
                .build();
    }
}
