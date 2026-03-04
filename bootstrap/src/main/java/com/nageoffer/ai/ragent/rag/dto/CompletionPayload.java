

package com.nageoffer.ai.ragent.rag.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 模型回复完成事件载荷
 *
 * @param messageId 消息ID（字符串，避免前端精度丢失）
 * @param title     会话标题（可选）
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CompletionPayload(String messageId, String title) {
}
