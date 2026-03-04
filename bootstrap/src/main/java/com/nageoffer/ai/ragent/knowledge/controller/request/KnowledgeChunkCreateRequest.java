

package com.nageoffer.ai.ragent.knowledge.controller.request;

import lombok.Data;

/**
 * 知识库 Chunk 创建请求
 */
@Data
public class KnowledgeChunkCreateRequest {

    /**
     * 分块正文内容
     */
    private String content;

    /**
     * 下标
     */
    private Integer index;

    /**
     * 分块 ID
     */
    private String chunkId;
}
