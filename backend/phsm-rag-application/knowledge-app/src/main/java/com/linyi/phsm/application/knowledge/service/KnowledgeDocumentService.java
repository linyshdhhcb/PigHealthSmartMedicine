package com.linyi.phsm.application.knowledge.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.linyi.phsm.domain.knowledge.model.request.KnowledgeDocumentPageRequest;
import com.linyi.phsm.domain.knowledge.model.request.KnowledgeDocumentUploadRequest;
import com.linyi.phsm.domain.knowledge.model.request.KnowledgeDocumentUpdateRequest;
import com.linyi.phsm.domain.knowledge.model.vo.KnowledgeDocumentVO;
import com.linyi.phsm.domain.knowledge.model.vo.KnowledgeDocumentChunkLogVO;
import com.linyi.phsm.domain.knowledge.model.vo.KnowledgeDocumentSearchVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 知识库文档服务接口
 */
public interface KnowledgeDocumentService {

    /**
     * 上传文档
     *
     * @param kbId         知识库 ID
     * @param requestParam 请求对象参数
     * @param file         待上传的文件
     * @return 知识库文档视图对象
     */
    KnowledgeDocumentVO upload(String kbId, KnowledgeDocumentUploadRequest requestParam, MultipartFile file);

    /**
     * 开始文档分片处理（校验状态并发送 MQ 消息，立即返回）
     *
     * @param docId 文档 ID
     */
    void startChunk(String docId);

    /**
     * 执行文档分块（由 MQ 消费者调用）
     * 获取分布式锁 → 清理历史分块和向量 → 执行完整分块流程
     *
     * @param docId 文档 ID
     */
    void executeChunk(String docId);

    /**
     * 删除文档
     *
     * @param docId 文档 ID
     */
    void delete(String docId);

    /**
     * 获取文档详情
     *
     * @param docId 文档 ID
     * @return 知识库文档视图对象
     */
    KnowledgeDocumentVO get(String docId);

    /**
     * 更新文档信息
     *
     * @param docId        文档 ID
     * @param requestParam 更新请求参数
     */
    void update(String docId, KnowledgeDocumentUpdateRequest requestParam);

    /**
     * 分页查询文档
     *
     * @param kbId         知识库 ID
     * @param requestParam 筛选参数
     * @return 文档分页结果
     */
    IPage<KnowledgeDocumentVO> page(String kbId, KnowledgeDocumentPageRequest requestParam);

    /**
     * 启用或禁用文档
     *
     * @param docId   文档 ID
     * @param enabled 是否启用
     */
    void enable(String docId, boolean enabled);

    /**
     * 搜索文档（用于全局检索建议）
     *
     * @param keyword 关键词
     * @param limit   最大返回数量
     * @return 文档列表
     */
    List<KnowledgeDocumentSearchVO> search(String keyword, int limit);

    /**
     * 查询文档分块日志
     *
     * @param docId 文档 ID
     * @param page  分页参数
     * @return 分块日志分页结果
     */
    IPage<KnowledgeDocumentChunkLogVO> getChunkLogs(String docId, Page<KnowledgeDocumentChunkLogVO> page);
}
