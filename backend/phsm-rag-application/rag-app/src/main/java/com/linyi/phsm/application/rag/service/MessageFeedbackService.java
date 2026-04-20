package com.linyi.phsm.application.rag.service;

import com.linyi.phsm.domain.rag.model.request.MessageFeedbackRequest;
import com.linyi.phsm.domain.rag.model.event.MessageFeedbackEvent;

import java.util.List;
import java.util.Map;

public interface MessageFeedbackService {

    /**
     * 提交会话消息反馈（同步，供内部直接调用）
     *
     * @param messageId 消息ID
     * @param request   反馈内容
     */
    void submitFeedback(String messageId, MessageFeedbackRequest request);

    /**
     * 提交会话消息反馈（异步，通过 MQ 持久化）
     *
     * @param messageId 消息ID
     * @param request   反馈内容
     */
    void submitFeedbackAsync(String messageId, MessageFeedbackRequest request);

    /**
     * 通过 MQ 事件异步持久化反馈（由消费者调用）
     *
     * @param event 反馈事件
     */
    void submitFeedbackByEvent(MessageFeedbackEvent event);

    /**
     * 查询用户在一批消息上的反馈值
     *
     * @param userId     用户ID
     * @param messageIds 消息ID列表
     * @return messageId -> vote
     */
    Map<String, Integer> getUserVotes(String userId, List<String> messageIds);
}
