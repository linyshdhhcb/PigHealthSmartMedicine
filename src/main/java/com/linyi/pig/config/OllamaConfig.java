package com.linyi.pig.config;

import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: linyi
 * @Date: 2025/2/14
 * @ClassName: OllamaConfig
 * @Version: 1.0
 * @Description: Ollama 配置类
 */
@Configuration
public class OllamaConfig {

    @Value("${ai.ollama.url}")
    private String url;


    /**
     * 创建并配置OllamaApi实例
     *
     * 该方法通过Spring的@Bean注解定义了一个Bean，确保在需要时Spring容器能够自动创建并管理该Bean的生命周期
     * 这里的OllamaApi实例是通过传入一个URL参数来构造的，URL参数定义了API的基础地址
     *
     * @return OllamaApi实例，该实例用于与Ollama服务进行通信
     */
    @Bean
    public OllamaChatModel getOllamaChatModel() {
        return new OllamaChatModel(new OllamaApi(url));
    }
}
