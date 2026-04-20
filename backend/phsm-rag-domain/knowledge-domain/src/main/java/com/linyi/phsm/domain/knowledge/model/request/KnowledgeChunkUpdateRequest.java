package com.linyi.phsm.domain.knowledge.model.request;

import lombok.Data;

/**
 * 知识库 Chunk 更新请求
 */
@Data
public class KnowledgeChunkUpdateRequest {

    /**
     * 分块正文内容
     */
    private String content;
}
