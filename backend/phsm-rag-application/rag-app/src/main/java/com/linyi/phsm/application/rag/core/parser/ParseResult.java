package com.linyi.phsm.application.rag.core.parser;

import java.util.Map;

/**
 * 文档解析结果
 *
 * @param text     解析后的文本内容
 * @param metadata 文档元数据（可选）
 */
public record ParseResult(String text, Map<String, Object> metadata) {

    /**
     * 创建只包含文本的解析结果
     */
    public static ParseResult ofText(String text) {
        return new ParseResult(text, Map.of());
    }

    /**
     * 创建包含文本和元数据的解析结果
     */
    public static ParseResult of(String text, Map<String, Object> metadata) {
        return new ParseResult(text, metadata != null ? metadata : Map.of());
    }
}
