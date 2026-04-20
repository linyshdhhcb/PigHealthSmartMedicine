package com.linyi.phsm.domain.ingestion.model.settings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 索引器设置实体类
 * <p>
 * 定义向量索引节点的配置参数，包括嵌入模型等
 * 索引器负责将处理后的文本块存储到向量数据库中
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IndexerSettings {

    /**
     * 用于生成向量嵌入的模型标识
     */
    private String embeddingModel;

    /**
     * 要存储的元数据字段列表
     * 指定哪些元数据字段应被存储到向量数据库中
     */
    private List<String> metadataFields;
}
