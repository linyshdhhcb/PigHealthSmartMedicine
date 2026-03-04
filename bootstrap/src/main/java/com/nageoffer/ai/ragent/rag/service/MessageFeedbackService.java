

package com.nageoffer.ai.ragent.rag.service;

import com.nageoffer.ai.ragent.rag.controller.request.MessageFeedbackRequest;

import java.util.List;
import java.util.Map;

public interface MessageFeedbackService {

    /**
     * 提交会话消息反馈
     *
     * @param messageId 消息ID
     * @param request   反馈内容
     */
    void submitFeedback(String messageId, MessageFeedbackRequest request);

    /**
     * 查询用户在一批消息上的反馈值
     *
     * @param userId     用户ID
     * @param messageIds 消息ID列表
     * @return messageId -> vote
     */
    Map<Long, Integer> getUserVotes(String userId, List<Long> messageIds);
}
