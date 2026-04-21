package com.linyi.pig.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MilvusConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.ai.vectorstore.milvus")
    public MilvusProperties milvusProperties() {
        return new MilvusProperties();
    }

    @Data
    public static class MilvusProperties {
        private Client client = new Client();

        @Data
        public static class Client {
            private String host = "127.0.0.1";
            private Integer port = 19530;
        }
    }
}
