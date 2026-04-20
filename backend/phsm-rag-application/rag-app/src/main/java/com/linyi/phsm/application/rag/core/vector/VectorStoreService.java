package com.linyi.phsm.application.rag.core.vector;

import com.linyi.phsm.domain.ingestion.model.chunk.VectorChunk;

import java.util.List;

/**
 * 向量存储服务接口
 */
public interface VectorStoreService {

    /**
     * 批量建立文档的向量索引
     *
     * @param collectionName 向量空间名称（知识库 collectionName）
     * @param docId          文档唯一标识
     * @param chunks         文档切片列表，须包含已计算好的 embedding
     */
    void indexDocumentChunks(String collectionName, String docId, List<VectorChunk> chunks);

    /**
     * 更新单个 chunk 的向量索引
     *
     * @param collectionName 向量空间名称（知识库 collectionName）
     * @param docId          文档唯一标识
     * @param chunk          待更新的文档切片，须包含最新的 embedding
     */
    void updateChunk(String collectionName, String docId, VectorChunk chunk);

    /**
     * 删除文档的所有向量索引
     *
     * @param collectionName 向量空间名称（知识库 collectionName）
     * @param docId          文档唯一标识
     */
    void deleteDocumentVectors(String collectionName, String docId);

    /**
     * 删除指定的单个 chunk 向量索引
     *
     * @param collectionName 向量空间名称（知识库 collectionName）
     * @param chunkId        chunk 的唯一标识
     */
    void deleteChunkById(String collectionName, String chunkId);

    /**
     * 批量删除指定 chunk 的向量索引
     *
     * @param collectionName 向量空间名称（知识库 collectionName）
     * @param chunkIds       chunk 唯一标识列表
     */
    void deleteChunksByIds(String collectionName, List<String> chunkIds);
}
