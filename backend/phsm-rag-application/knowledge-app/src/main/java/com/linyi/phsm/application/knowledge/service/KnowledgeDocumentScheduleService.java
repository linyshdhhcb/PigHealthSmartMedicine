package com.linyi.phsm.application.knowledge.service;

import com.linyi.phsm.domain.knowledge.model.entity.KnowledgeDocumentDO;

/**
 * 知识库文档定时任务服务
 */
public interface KnowledgeDocumentScheduleService {

    /**
     * 根据文档信息创建或更新定时任务记录
     *
     * @param documentDO 文档实体
     */
    void upsertSchedule(KnowledgeDocumentDO documentDO);

    /**
     * 当文档启用/禁用时同步定时任务（仅更新已存在的任务）
     *
     * @param documentDO 文档实体
     */
    void syncScheduleIfExists(KnowledgeDocumentDO documentDO);

    /**
     * 删除文档关联的定时任务及执行记录
     *
     * @param docId 文档ID
     */
    void deleteByDocId(String docId);
}
