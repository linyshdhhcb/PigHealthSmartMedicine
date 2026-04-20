package com.linyi.phsm.application.rag.service.handler;

import com.linyi.phsm.infrastructure.ai.AiModelProperties;
import com.linyi.phsm.application.rag.core.memory.ConversationMemoryService;
import com.linyi.phsm.application.rag.service.ConversationGroupService;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * StreamChatEventHandler 构建参数
 * 使用参数对象模式，将多个参数封装成一个对象
 */
@Getter
@Builder
public class StreamChatHandlerParams {

    /**
     * SSE 发射器
     */
    private final SseEmitter emitter;

    /**
     * 会话ID
     */
    private final String conversationId;

    /**
     * 任务ID
     */
    private final String taskId;

    /**
     * 模型配置
     */
    private final AiModelProperties modelProperties;

    /**
     * 记忆服务
     */
    private final ConversationMemoryService memoryService;

    /**
     * 会话组服务
     */
    private final ConversationGroupService conversationGroupService;

    /**
     * 任务管理器
     */
    private final StreamTaskManager taskManager;
}
