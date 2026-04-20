package com.linyi.phsm.domain.ingestion.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 摄取任务状态枚举
 * 定义文档摄取任务的执行状态
 * 状态值使用小写 snake_case，如 pending、running、completed
 */
@Getter
@RequiredArgsConstructor
public enum IngestionStatus {

    /**
     * 等待中 - 任务已创建但尚未开始执行
     */
    PENDING("pending"),

    /**
     * 运行中 - 任务正在执行中
     */
    RUNNING("running"),

    /**
     * 失败 - 任务执行失败
     */
    FAILED("failed"),

    /**
     * 已完成 - 任务执行成功完成
     */
    COMPLETED("completed");

    /**
     * 状态值（小写 snake_case）
     */
    private final String value;

    /**
     * 根据字符串值解析状态
     */
    @JsonCreator
    public static IngestionStatus fromValue(String value) {
        if (value == null) {
            return null;
        }
        String normalized = normalize(value);
        for (IngestionStatus status : values()) {
            if (status.value.equalsIgnoreCase(normalized) || status.name().equalsIgnoreCase(normalized)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown ingestion status: " + value);
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
