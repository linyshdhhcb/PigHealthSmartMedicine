

package com.nageoffer.ai.ragent.knowledge.controller.vo;

import lombok.Data;

/**
 * 文档搜索返回对象
 */
@Data
public class KnowledgeDocumentSearchVO {

    /**
     * 文档ID
     */
    private String id;

    /**
     * 知识库ID
     */
    private Long kbId;

    /**
     * 文档名称
     */
    private String docName;

    /**
     * 知识库名称
     */
    private String kbName;
}
