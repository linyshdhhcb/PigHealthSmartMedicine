package com.linyi.phsm.domain.rag.model.request;

import lombok.Data;

/**
 * 示例问题创建请求
 */
@Data
public class SampleQuestionCreateRequest {

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
