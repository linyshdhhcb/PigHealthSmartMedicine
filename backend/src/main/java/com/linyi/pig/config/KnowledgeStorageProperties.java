package com.linyi.pig.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "knowledge.storage")
public class KnowledgeStorageProperties {
    /**
     * 知识库存储根目录，建议配置为绝对路径；
     * 若不配置，将默认使用项目下 backend/src/main/resources/knowledge
     */
    private String root;
}
