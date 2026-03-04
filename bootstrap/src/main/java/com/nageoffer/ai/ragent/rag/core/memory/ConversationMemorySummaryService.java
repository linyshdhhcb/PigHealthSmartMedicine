

package com.nageoffer.ai.ragent.rag.core.memory;

import com.nageoffer.ai.ragent.framework.convention.ChatMessage;

public interface ConversationMemorySummaryService {

    void compressIfNeeded(String conversationId, String userId, ChatMessage message);

    ChatMessage loadLatestSummary(String conversationId, String userId);

    ChatMessage decorateIfNeeded(ChatMessage summary);
}
