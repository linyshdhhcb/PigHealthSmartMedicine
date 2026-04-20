package com.linyi.phsm.domain.knowledge.model.request;

import lombok.Data;

@Data
public class KnowledgeBaseUpdateRequest {

    private String id;

    /**
     * 知识库名称（可修改）
     */
    private String name;

    /**
     * 嵌入模型（有文档分块后禁止修改）
     */
    private String embeddingModel;
}
