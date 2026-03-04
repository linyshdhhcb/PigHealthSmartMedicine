

package com.nageoffer.ai.ragent.knowledge.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nageoffer.ai.ragent.knowledge.controller.request.KnowledgeDocumentUploadRequest;
import com.nageoffer.ai.ragent.knowledge.controller.request.KnowledgeDocumentUpdateRequest;
import com.nageoffer.ai.ragent.knowledge.controller.vo.KnowledgeDocumentVO;
import com.nageoffer.ai.ragent.knowledge.controller.vo.KnowledgeDocumentChunkLogVO;
import com.nageoffer.ai.ragent.knowledge.controller.vo.KnowledgeDocumentSearchVO;
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
     * 开始文档分片处理
     *
     * @param docId 文档 ID
     */
    void startChunk(String docId);

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
     * @param kbId    知识库 ID
     * @param page    分页参数
     * @param status  状态筛选
     * @param keyword 关键词搜索
     * @return 文档分页结果
     */
    IPage<KnowledgeDocumentVO> page(String kbId, Page<KnowledgeDocumentVO> page, String status, String keyword);

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
