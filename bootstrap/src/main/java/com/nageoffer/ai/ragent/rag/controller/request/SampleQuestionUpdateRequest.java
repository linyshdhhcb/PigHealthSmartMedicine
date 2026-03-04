

package com.nageoffer.ai.ragent.rag.controller.request;

import lombok.Data;

/**
 * 示例问题更新请求
 */
@Data
public class SampleQuestionUpdateRequest {

    /**
     * 展示标题（可选）
     */
    private String title;

    /**
     * 描述或提示（可选）
     */
    private String description;

    /**
     * 示例问题内容
     */
    private String question;
}
