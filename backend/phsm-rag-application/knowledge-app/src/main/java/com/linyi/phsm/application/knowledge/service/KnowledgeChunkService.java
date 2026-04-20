package com.linyi.phsm.application.knowledge.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.linyi.phsm.domain.knowledge.model.request.KnowledgeChunkBatchRequest;
import com.linyi.phsm.domain.knowledge.model.request.KnowledgeChunkCreateRequest;
import com.linyi.phsm.domain.knowledge.model.request.KnowledgeChunkPageRequest;
import com.linyi.phsm.domain.knowledge.model.request.KnowledgeChunkUpdateRequest;
import com.linyi.phsm.domain.knowledge.model.vo.KnowledgeChunkVO;

import java.util.List;

/**
 * 知识库分片服务接口
 */
public interface KnowledgeChunkService {

    /**
     * 分页查询指定文档的分片列表
     *
     * @param docId        文档 ID
     * @param requestParam 分页查询参数
     * @return 分片分页信息
     */
    IPage<KnowledgeChunkVO> pageQuery(String docId, KnowledgeChunkPageRequest requestParam);

    /**
     * 为指定文档新增分片
     *
     * @param docId        文档 ID
     * @param requestParam 新增分片请求参数
     * @return 新增的分片视图对象
     */
    KnowledgeChunkVO create(String docId, KnowledgeChunkCreateRequest requestParam);

    /**
     * 批量新增文档分片（默认不写入向量库）
     *
     * @param docId         文档 ID
     * @param requestParams 批量新增分片请求参数列表
     */
    void batchCreate(String docId, List<KnowledgeChunkCreateRequest> requestParams);

    /**
     * 批量新增文档分片（可选同步写入向量库）
     *
     * @param docId         文档 ID
     * @param requestParams 批量新增分片请求参数列表
     * @param writeVector   是否同步写入向量库
     */
    void batchCreate(String docId, List<KnowledgeChunkCreateRequest> requestParams, boolean writeVector);

    /**
     * 更新指定文档的特定分片内容
     *
     * @param docId        文档 ID
     * @param chunkId      分片 ID
     * @param requestParam 更新分片请求参数
     */
    void update(String docId, String chunkId, KnowledgeChunkUpdateRequest requestParam);

    /**
     * 删除指定文档的特定分片
     *
     * @param docId   文档 ID
     * @param chunkId 分片 ID
     */
    void delete(String docId, String chunkId);

    /**
     * 启用或禁用单个分片
     *
     * @param docId   文档 ID
     * @param chunkId 分片 ID
     * @param enabled 是否启用
     */
    void enableChunk(String docId, String chunkId, boolean enabled);

    /**
     * 批量启用或禁用文档分片
     *
     * @param docId        文档 ID
     * @param requestParam 批量处理请求参数（为空表示操作全部）
     * @param enabled      true=启用，false=禁用
     */
    void batchToggleEnabled(String docId, KnowledgeChunkBatchRequest requestParam, boolean enabled);

    /**
     * 根据文档 ID 批量更新所有分片的启用状态
     *
     * @param docId   文档 ID
     * @param kbId    知识库 ID（用于日志，避免重复查询）
     * @param enabled 是否启用
     */
    void updateEnabledByDocId(String docId, String kbId, boolean enabled);

    /**
     * 根据文档 ID 查询所有分片列表
     *
     * @param docId 文档 ID
     * @return 分片列表
     */
    List<KnowledgeChunkVO> listByDocId(String docId);

    /**
     * 删除指定文档的所有分片
     *
     * @param docId 文档 ID
     */
    void deleteByDocId(String docId);
}
