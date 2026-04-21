package com.linyi.pig.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "ai")
public class AiProviderProperties {
    private ProviderSelection selection = new ProviderSelection();
    private ChatConfig chat = new ChatConfig();
    private EmbeddingConfig embedding = new EmbeddingConfig();
    private Providers providers = new Providers();

    @Data
    public static class ProviderSelection {
        private String chatProvider = "ollama";
        private String embeddingProvider = "ollama";
    }

    @Data
    public static class ChatConfig {
        private String defaultModel = "qwen2.5:7b";
    }

    @Data
    public static class EmbeddingConfig {
        private String defaultModel = "nomic-embed-text";
    }

    @Data
    public static class Providers {
        private Provider ollama = new Provider();
        private Provider bailian = new Provider();
        private Provider siliconflow = new Provider();
    }

    @Data
    public static class Provider {
        private String url;
        private String apiKey;
        private Endpoint endpoints = new Endpoint();
    }

    @Data
    public static class Endpoint {
        private String chat = "/v1/chat/completions";
        private String embedding = "/v1/embeddings";
    }
}
