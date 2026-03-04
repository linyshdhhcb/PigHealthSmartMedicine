

package com.nageoffer.ai.ragent.rag.core.retrieve.channel;

/**
 * 检索通道类型枚举
 */
public enum SearchChannelType {

    /**
     * 向量全局检索
     * 在所有知识库中进行向量检索
     */
    VECTOR_GLOBAL,

    /**
     * 意图定向检索
     * 基于意图识别结果，在特定知识库中检索
     */
    INTENT_DIRECTED,

    /**
     * ES 关键词检索
     * 基于 Elasticsearch 的关键词分词检索
     */
    KEYWORD_ES,

    /**
     * 混合检索
     * 结合多种检索策略
     */
    HYBRID
}
