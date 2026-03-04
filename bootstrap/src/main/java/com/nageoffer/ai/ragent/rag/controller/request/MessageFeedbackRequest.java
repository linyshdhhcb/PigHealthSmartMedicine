

package com.nageoffer.ai.ragent.rag.controller.request;

import lombok.Data;

/**
 * 会话消息反馈请求
 */
@Data
public class MessageFeedbackRequest {

    /**
     * 反馈值：1=点赞，-1=点踩
     */
    private Integer vote;

    /**
     * 反馈原因（可选）
     */
    private String reason;

    /**
     * 补充说明（可选）
     */
    private String comment;
}
