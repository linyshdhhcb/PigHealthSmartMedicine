package com.linyi.phsm.domain.knowledge.model.request;

import lombok.Data;

import java.util.List;

/**
 * 知识库 Chunk 批量操作请求
 */
@Data
public class KnowledgeChunkBatchRequest {

    /**
     * Chunk ID 列表（可选，不传则操作文档下所有 chunk）
     */
    private List<String> chunkIds;
}
