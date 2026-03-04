

package com.nageoffer.ai.ragent.rag.core.vector;

import com.nageoffer.ai.ragent.core.chunk.VectorChunk;

import java.util.List;

/**
 * 向量存储服务接口
 */
public interface VectorStoreService {

    /**
     * 批量建立文档的向量索引
     *
     * @param kbId   知识库唯一标识
     * @param docId  文档唯一标识
     * @param chunks 文档切片列表，包含文本内容、chunk索引等信息
     * @throws IllegalArgumentException 当参数为空或无效时抛出
     */
    void indexDocumentChunks(String kbId, String docId, List<VectorChunk> chunks);

    /**
     * 更新单个 chunk 的向量索引
     *
     * @param kbId  知识库唯一标识
     * @param docId 文档唯一标识
     * @param chunk 待更新的文档切片，包含最新的文本内容
     * @throws IllegalArgumentException 当参数为空或无效时抛出
     */
    void updateChunk(String kbId, String docId, VectorChunk chunk);

    /**
     * 删除文档的所有向量索引
     *
     * @param kbId  知识库唯一标识
     * @param docId 文档唯一标识
     * @throws IllegalArgumentException 当参数为空或无效时抛出
     */
    void deleteDocumentVectors(String kbId, String docId);

    /**
     * 删除指定的单个 chunk 向量索引
     *
     * @param kbId    知识库唯一标识
     * @param chunkId chunk 的唯一标识
     * @throws IllegalArgumentException 当参数为空或无效时抛出
     */
    void deleteChunkById(String kbId, String chunkId);
}
