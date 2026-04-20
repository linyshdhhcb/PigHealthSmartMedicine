package com.linyi.phsm.application.rag.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.linyi.phsm.framework.context.UserContext;
import com.linyi.phsm.framework.model.ChatMessage;
import com.linyi.phsm.framework.model.ChatRequest;
import com.linyi.phsm.framework.trace.RagTraceContext;
import com.linyi.phsm.infrastructure.ai.chat.LLMService;
import com.linyi.phsm.infrastructure.ai.stream.StreamCallback;
import com.linyi.phsm.infrastructure.ai.stream.StreamCancellationHandle;
import com.linyi.phsm.application.rag.aop.ChatRateLimit;
import com.linyi.phsm.application.rag.core.guidance.GuidanceDecision;
import com.linyi.phsm.application.rag.core.guidance.IntentGuidanceService;
import com.linyi.phsm.application.rag.core.intent.IntentResolver;
import com.linyi.phsm.application.rag.core.memory.ConversationMemoryService;
import com.linyi.phsm.application.rag.core.prompt.PromptContext;
import com.linyi.phsm.application.rag.core.prompt.PromptTemplateLoader;
import com.linyi.phsm.application.rag.core.prompt.RAGPromptService;
import com.linyi.phsm.application.rag.core.retrieve.RetrievalEngine;
import com.linyi.phsm.application.rag.core.rewrite.QueryRewriteService;
import com.linyi.phsm.application.rag.core.rewrite.RewriteResult;
import com.linyi.phsm.domain.rag.model.dto.IntentGroup;
import com.linyi.phsm.domain.rag.model.dto.RetrievalContext;
import com.linyi.phsm.domain.rag.model.dto.SubQuestionIntent;
import com.linyi.phsm.application.rag.service.RAGChatService;
import com.linyi.phsm.application.rag.service.handler.StreamCallbackFactory;
import com.linyi.phsm.application.rag.service.handler.StreamTaskManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;

import static com.linyi.phsm.domain.rag.model.constant.RAGConstant.CHAT_SYSTEM_PROMPT_PATH;
import static com.linyi.phsm.domain.rag.model.constant.RAGConstant.DEFAULT_TOP_K;

/**
 * RAG 对话服务默认实现
 * <p>
 * 核心流程：
 * 记忆加载 -> 改写拆分 -> 意图解析 -> 歧义引导 -> 检索(MCP+KB) -> Prompt 组装 -> 流式输出
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RAGChatServiceImpl implements RAGChatService {

    private final LLMService llmService;
    private final RAGPromptService promptBuilder;
    private final PromptTemplateLoader promptTemplateLoader;
    private final ConversationMemoryService memoryService;
    private final StreamTaskManager taskManager;
    private final IntentGuidanceService guidanceService;
    private final StreamCallbackFactory callbackFactory;
    private final QueryRewriteService queryRewriteService;
    private final IntentResolver intentResolver;
    private final RetrievalEngine retrievalEngine;

    @Override
    @ChatRateLimit
    public void streamChat(String question, String conversationId, Boolean deepThinking, SseEmitter emitter) {
        String actualConversationId = StrUtil.isBlank(conversationId) ? IdUtil.getSnowflakeNextIdStr() : conversationId;
        String taskId = StrUtil.isBlank(RagTraceContext.getTaskId())
                ? IdUtil.getSnowflakeNextIdStr()
                : RagTraceContext.getTaskId();
        log.info("开始流式对话，会话ID：{}，任务ID：{}", actualConversationId, taskId);
        boolean thinkingEnabled = Boolean.TRUE.equals(deepThinking);

        StreamCallback callback = callbackFactory.createChatEventHandler(emitter, actualConversationId, taskId);
        try {
            String userId = UserContext.getUserId();
            List<ChatMessage> history = memoryService.loadAndAppend(actualConversationId, userId, ChatMessage.user(question));

            RewriteResult rewriteResult = queryRewriteService.rewriteWithSplit(question, history);
            List<SubQuestionIntent> subIntents = intentResolver.resolve(rewriteResult);

            GuidanceDecision guidanceDecision = guidanceService.detectAmbiguity(rewriteResult.rewrittenQuestion(), subIntents);
            if (guidanceDecision.isPrompt()) {
                callback.onContent(guidanceDecision.getPrompt());
                callback.onComplete();
                return;
            }

            boolean allSystemOnly = subIntents.stream()
                    .allMatch(si -> intentResolver.isSystemOnly(si.nodeScores()));
            if (allSystemOnly) {
                String customPrompt = subIntents.stream()
                        .flatMap(si -> si.nodeScores().stream())
                        .map(ns -> ns.getNode().getPromptTemplate())
                        .filter(StrUtil::isNotBlank)
                        .findFirst()
                        .orElse(null);
                StreamCancellationHandle handle = streamSystemResponse(rewriteResult.rewrittenQuestion(), history, customPrompt, callback);
                taskManager.bindHandle(taskId, handle);
                return;
            }

            RetrievalContext ctx = retrievalEngine.retrieve(subIntents, DEFAULT_TOP_K);
            if (ctx.isEmpty()) {
                String emptyReply = "未检索到与问题相关的文档内容。";
                callback.onContent(emptyReply);
                callback.onComplete();
                return;
            }

            // 聚合所有意图用于 prompt 规划
            IntentGroup mergedGroup = intentResolver.mergeIntentGroup(subIntents);

            StreamCancellationHandle handle = streamLLMResponse(
                    rewriteResult,
                    ctx,
                    mergedGroup,
                    history,
                    thinkingEnabled,
                    callback
            );
            taskManager.bindHandle(taskId, handle);
        } catch (Exception e) {
            log.error("流式对话处理异常，会话ID：{}，任务ID：{}", actualConversationId, taskId, e);
            callback.onError(e);
        }
    }

    @Override
    public void stopTask(String taskId) {
        taskManager.cancel(taskId);
    }

    // ==================== LLM 响应 ====================

    private StreamCancellationHandle streamSystemResponse(String question, List<ChatMessage> history,
                                                          String customPrompt, StreamCallback callback) {
        String systemPrompt = StrUtil.isNotBlank(customPrompt)
                ? customPrompt
                : promptTemplateLoader.load(CHAT_SYSTEM_PROMPT_PATH);

        List<ChatMessage> messages = new ArrayList<>();
        messages.add(ChatMessage.system(systemPrompt));
        if (CollUtil.isNotEmpty(history)) {
            messages.addAll(history);
        }
        messages.add(ChatMessage.user(question));

        ChatRequest req = ChatRequest.builder()
                .messages(messages)
                .temperature(0.7D)
                .thinking(false)
                .build();
        return llmService.streamChat(req, callback);
    }

    private StreamCancellationHandle streamLLMResponse(RewriteResult rewriteResult, RetrievalContext ctx,
                                                       IntentGroup intentGroup, List<ChatMessage> history,
                                                       boolean deepThinking, StreamCallback callback) {
        PromptContext promptContext = PromptContext.builder()
                .question(rewriteResult.rewrittenQuestion())
                .mcpContext(ctx.getMcpContext())
                .kbContext(ctx.getKbContext())
                .mcpIntents(intentGroup.mcpIntents())
                .kbIntents(intentGroup.kbIntents())
                .intentChunks(ctx.getIntentChunks())
                .build();

        List<ChatMessage> messages = promptBuilder.buildStructuredMessages(
                promptContext,
                history,
                rewriteResult.rewrittenQuestion(),
                rewriteResult.subQuestions()  // 传入子问题列表
        );
        ChatRequest chatRequest = ChatRequest.builder()
                .messages(messages)
                .thinking(deepThinking)
                .temperature(ctx.hasMcp() ? 0.3D : 0D)  // MCP 场景稍微放宽温度
                .topP(ctx.hasMcp() ? 0.8D : 1D)
                .build();

        return llmService.streamChat(chatRequest, callback);
    }
}
