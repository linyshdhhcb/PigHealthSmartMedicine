

package com.nageoffer.ai.ragent.core.chunk;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 文档分块策略枚举
 * 定义将文档内容切分成块的不同策略，适用于不同的文档类型和场景
 * 策略值使用小写 snake_case，如 fixed_size、sentence
 */
@Getter
@RequiredArgsConstructor
public enum ChunkingMode {

    /**
     * 固定大小切分 - 按固定字符数或token数切分
     */
    FIXED_SIZE("fixed_size"),

    /**
     * 对Markdown友好的切分 - 保留Markdown结构
     */
    STRUCTURE_AWARE("structure_aware"),

    /**
     * 按句子切分 - 以句子为单位进行切分
     */
    SENTENCE("sentence"),

    /**
     * 按段落切分 - 以段落为单位进行切分
     */
    PARAGRAPH("paragraph");

    /**
     * 策略值（小写 snake_case）
     */
    private final String value;

    /**
     * 根据字符串值解析策略
     */
    @JsonCreator
    public static ChunkingMode fromValue(String value) {
        if (value == null) {
            return null;
        }
        String normalized = normalize(value);
        for (ChunkingMode strategy : values()) {
            if (strategy.value.equalsIgnoreCase(normalized) || strategy.name().equalsIgnoreCase(normalized)) {
                return strategy;
            }
        }
        throw new IllegalArgumentException("Unknown chunk strategy: " + value);
    }

    private static String normalize(String value) {
        String trimmed = value.trim();
        String lower = trimmed.toLowerCase();
        return lower.replace('-', '_');
    }

    /**
     * 获取序列化值
     */
    @JsonValue
    public String getValue() {
        return value;
    }
}
