package com.linyi.phsm.domain.ingestion.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 文本块富集类型枚举
 * 定义对文档分块进行富集处理的类型，用于增强分块的元数据和检索能力
 * 类型值使用小写 snake_case，如 keywords、summary
 */
@Getter
@RequiredArgsConstructor
public enum ChunkEnrichType {

    /**
     * 关键词提取 - 从文本块中提取关键词
     */
    KEYWORDS("keywords"),

    /**
     * 摘要生成 - 为文本块生成摘要
     */
    SUMMARY("summary"),

    /**
     * 元数据添加 - 为文本块添加额外的元数据信息
     */
    METADATA("metadata");

    /**
     * 类型值（小写 snake_case）
     */
    private final String value;

    /**
     * 根据字符串值解析类型
     */
    @JsonCreator
    public static ChunkEnrichType fromValue(String value) {
        if (value == null) {
            return null;
        }
        String normalized = normalize(value);
        for (ChunkEnrichType type : values()) {
            if (type.value.equalsIgnoreCase(normalized) || type.name().equalsIgnoreCase(normalized)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown chunk enrich type: " + value);
    }

    /**
     * 获取序列化值
     */
    @JsonValue
    public String getValue() {
        return value;
    }

    private static String normalize(String value) {
        String trimmed = value.trim();
        String lower = trimmed.toLowerCase();
        return lower.replace('-', '_');
    }
}
