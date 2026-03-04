

package com.nageoffer.ai.ragent.infra.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 模型能力枚举类
 * 定义了AI模型支持的各种能力类型
 */
@Getter
@RequiredArgsConstructor
public enum ModelCapability {

    /**
     * 聊天对话能力
     * 支持与用户进行自然语言对话交互
     */
    CHAT("Chat"),

    /**
     * 向量嵌入能力
     * 将文本转换为向量表示，用于语义搜索和相似度计算
     */
    EMBEDDING("Embedding"),

    /**
     * 重排序能力
     * 对搜索结果进行重新排序，提高相关性
     */
    RERANK("Rerank");

    /**
     * 能力的显示名称
     */
    private final String displayName;
}
