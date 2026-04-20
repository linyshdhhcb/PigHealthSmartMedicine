package com.linyi.phsm.domain.rag.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 会话创建请求
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationCreateRequest {

    /**
     * 会话ID
     */
    private String conversationId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户问题
     */
    private String question;

    /**
     * 最后更新时间
     */
    private Date lastTime;

}
