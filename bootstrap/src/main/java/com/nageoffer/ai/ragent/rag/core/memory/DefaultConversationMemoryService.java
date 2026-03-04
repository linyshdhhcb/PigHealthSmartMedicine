

package com.nageoffer.ai.ragent.rag.core.memory;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.nageoffer.ai.ragent.framework.convention.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class DefaultConversationMemoryService implements ConversationMemoryService {

    private final ConversationMemoryStore memoryStore;
    private final ConversationMemorySummaryService summaryService;

    public DefaultConversationMemoryService(ConversationMemoryStore memoryStore,
                                            ConversationMemorySummaryService summaryService) {
        this.memoryStore = memoryStore;
        this.summaryService = summaryService;
    }

    @Override
    public List<ChatMessage> load(String conversationId, String userId) {
        // 参数校验
        if (StrUtil.isBlank(conversationId) || StrUtil.isBlank(userId)) {
            return List.of();
        }

        long startTime = System.currentTimeMillis();
        try {
            // 并行加载摘要和历史记录
            CompletableFuture<ChatMessage> summaryFuture = CompletableFuture.supplyAsync(
                    () -> loadSummaryWithFallback(conversationId, userId)
            );
            CompletableFuture<List<ChatMessage>> historyFuture = CompletableFuture.supplyAsync(
                    () -> loadHistoryWithFallback(conversationId, userId)
            );

            // 等待所有任务完成后合并结果
            return CompletableFuture.allOf(summaryFuture, historyFuture)
                    .thenApply(v -> {
                        ChatMessage summary = summaryFuture.join();
                        List<ChatMessage> history = historyFuture.join();
                        log.debug("加载对话记忆 - conversationId: {}, userId: {}, 摘要: {}, 历史消息数: {}, 耗时: {}ms",
                                conversationId, userId, summary != null, history.size(), System.currentTimeMillis() - startTime);
                        return attachSummary(summary, history);
                    })
                    .join();
        } catch (Exception e) {
            log.error("加载对话记忆失败 - conversationId: {}, userId: {}", conversationId, userId, e);
            return List.of();
        }
    }

    /**
     * 加载摘要，失败时返回 null
     */
    private ChatMessage loadSummaryWithFallback(String conversationId, String userId) {
        try {
            return summaryService.loadLatestSummary(conversationId, userId);
        } catch (Exception e) {
            log.warn("加载摘要失败，将跳过摘要 - conversationId: {}, userId: {}", conversationId, userId, e);
            return null;
        }
    }

    /**
     * 加载历史记录，失败时返回空列表
     */
    private List<ChatMessage> loadHistoryWithFallback(String conversationId, String userId) {
        try {
            List<ChatMessage> history = memoryStore.loadHistory(conversationId, userId);
            return history != null ? history : List.of();
        } catch (Exception e) {
            log.error("加载历史记录失败 - conversationId: {}, userId: {}", conversationId, userId, e);
            return List.of();
        }
    }

    @Override
    public Long append(String conversationId, String userId, ChatMessage message) {
        if (StrUtil.isBlank(conversationId) || StrUtil.isBlank(userId)) {
            return null;
        }
        Long messageId = memoryStore.append(conversationId, userId, message);
        summaryService.compressIfNeeded(conversationId, userId, message);
        return messageId;
    }

    private List<ChatMessage> attachSummary(ChatMessage summary, List<ChatMessage> messages) {
        // 确保返回值不为 null
        if (CollUtil.isEmpty(messages)) {
            return List.of();
        }
        if (summary == null) {
            return messages;
        }
        List<ChatMessage> result = new ArrayList<>();
        result.add(summaryService.decorateIfNeeded(summary));
        result.addAll(messages);
        return result;
    }
}
