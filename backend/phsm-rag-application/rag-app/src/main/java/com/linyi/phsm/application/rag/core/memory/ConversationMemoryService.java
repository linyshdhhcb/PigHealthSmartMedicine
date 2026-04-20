package com.linyi.phsm.application.rag.core.memory;

import com.linyi.phsm.framework.model.ChatMessage;

import java.util.List;

/**
 * 对话记忆服务接口
 * 负责管理和存储用户对话历史记录，提供对话上下文的加载、追加等功能
 */
public interface ConversationMemoryService {

    /**
     * 加载对话历史记录
     *
     * @param conversationId 对话ID
     * @param userId         用户ID
     * @return 对话历史消息列表（包含摘要和历史记录）
     */
    List<ChatMessage> load(String conversationId, String userId);

    /**
     * 追加消息到对话历史
     *
     * @param conversationId 对话ID
     * @param userId         用户ID
     * @param message        要追加的消息
     * @return 消息ID
     */
    String append(String conversationId, String userId, ChatMessage message);

    /**
     * 加载历史并追加新消息（便捷方法）
     * <p>
     * 适用于需要同时获取历史和追加消息的场景，避免重复调用 load() 和 append()
     * </p>
     *
     * @param conversationId 对话ID
     * @param userId         用户ID
     * @param message        要追加的消息
     * @return 包含追加前的历史记录
     */
    default List<ChatMessage> loadAndAppend(String conversationId, String userId, ChatMessage message) {
        List<ChatMessage> history = load(conversationId, userId);
        append(conversationId, userId, message);
        return history;
    }
}
