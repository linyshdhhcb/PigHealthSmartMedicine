package com.nageoffer.ai.ragent.pig.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nageoffer.ai.ragent.pig.controller.vo.CaseVO;

/**
 * 病例信息服务接口
 */
public interface CaseService {

    /**
     * 分页查询病例列表
     */
    Page<CaseVO> listCases(Long pigId, Long userId, String status, Integer pageNum, Integer pageSize);

    /**
     * 根据ID查询病例详情
     */
    CaseVO getCaseById(Long id);

    /**
     * 创建病例
     */
    Long createCase(CaseVO caseVO);

    /**
     * 更新病例信息
     */
    void updateCase(CaseVO caseVO);

    /**
     * 删除病例
     */
    void deleteCase(Long id);

    /**
     * 根据对话ID查询病例
     */
    CaseVO getCaseByConversationId(String conversationId);
}
