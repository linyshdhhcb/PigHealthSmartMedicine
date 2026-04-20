package com.linyi.phsm.domain.ingestion.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 文档源类型枚举
 * 定义支持的文档来源类型，用于标识文档的获取方式
 * 类型值使用小写 snake_case，如 file、url、feishu、s3
 */
@Getter
@RequiredArgsConstructor
public enum SourceType {

    /**
     * 本地文件 - 文档来源为本地文件系统
     */
    FILE("file"),

    /**
     * URL地址 - 文档来源为网络 URL
     */
    URL("url"),

    /**
     * 飞书文档 - 文档来源为飞书文档
     */
    FEISHU("feishu"),

    /**
     * S3对象存储 - 文档来源为S3兼容的对象存储（如RustFS）
     */
    S3("s3");

    /**
     * 类型值（小写 snake_case）
     */
    private final String value;

    /**
     * 根据字符串值解析类型
     */
    @JsonCreator
    public static SourceType fromValue(String value) {
        if (value == null) {
            return null;
        }
        String normalized = normalize(value);
        for (SourceType type : values()) {
            if (type.value.equalsIgnoreCase(normalized) || type.name().equalsIgnoreCase(normalized)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown source type: " + value);
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
