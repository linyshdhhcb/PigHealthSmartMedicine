package com.linyi.phsm.application.knowledge.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.linyi.phsm.domain.knowledge.model.request.KnowledgeBaseCreateRequest;
import com.linyi.phsm.domain.knowledge.model.request.KnowledgeBasePageRequest;
import com.linyi.phsm.domain.knowledge.model.request.KnowledgeBaseUpdateRequest;
import com.linyi.phsm.domain.knowledge.model.vo.KnowledgeBaseVO;

/**
 * 知识库服务接口
 */
public interface KnowledgeBaseService {

    /**
     * 创建知识库
     *
     * @param requestParam 创建知识库请求参数
     * @return 知识库ID
     */
    String create(KnowledgeBaseCreateRequest requestParam);

    /**
     * 更新知识库
     *
     * @param requestParam 更新知识库请求参数
     */
    void update(KnowledgeBaseUpdateRequest requestParam);

    /**
     * 重命名知识库
     *
     * @param kbId         知识库ID
     * @param requestParam 重命名请求参数
     */
    void rename(String kbId, KnowledgeBaseUpdateRequest requestParam);

    /**
     * 删除知识库
     *
     * @param kbId 知识库ID
     */
    void delete(String kbId);

    /**
     * 根据ID查询知识库详情
     *
     * @param kbId 知识库ID
     * @return 知识库详细信息
     */
    KnowledgeBaseVO queryById(String kbId);

    /**
     * 分页查询知识库
     *
     * @param requestParam 分页查询请求参数
     * @return 知识库分页结果
     */
    IPage<KnowledgeBaseVO> pageQuery(KnowledgeBasePageRequest requestParam);
}
