package com.linyi.phsm.application.rag.service;

import com.linyi.phsm.domain.rag.model.vo.ConversationMessageVO;
import com.linyi.phsm.domain.rag.model.enums.ConversationMessageOrder;
import com.linyi.phsm.domain.rag.model.bo.ConversationMessageBO;
import com.linyi.phsm.domain.rag.model.bo.ConversationSummaryBO;

import java.util.List;

public interface ConversationMessageService {

    /**
     * 新增对话消息
     *
     * @param conversationMessage 消息内容
     */
    String addMessage(ConversationMessageBO conversationMessage);

    /**
     * 获取对话消息列表（支持排序与数量限制）
     *
     * @param conversationId 对话ID
     * @param userId         用户ID
     * @param limit          限制数量
     * @param order          排序方式
     * @return 对话消息列表
     */
    List<ConversationMessageVO> listMessages(String conversationId, String userId, Integer limit, ConversationMessageOrder order);

    /**
     * 添加对话摘要
     *
     * @param conversationSummary 对话摘要内容
     */
    void addMessageSummary(ConversationSummaryBO conversationSummary);
}
