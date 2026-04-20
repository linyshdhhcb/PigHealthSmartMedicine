package com.linyi.phsm.domain.rag.model.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 消息点赞/点踩反馈事件
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageFeedbackEvent implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 消息ID
     */
    private String messageId;

    /**
     * 用户ID
     */
    private String userId;

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

    /**
     * 用户提交时间戳（毫秒），用于多节点消费时保证最终一致性
     */
    private long submitTime;
}
