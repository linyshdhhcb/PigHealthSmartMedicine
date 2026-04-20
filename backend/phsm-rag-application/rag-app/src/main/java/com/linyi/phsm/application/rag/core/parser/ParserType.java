package com.linyi.phsm.application.rag.core.parser;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 文档解析器类型枚举
 */
@Getter
@RequiredArgsConstructor
public enum ParserType {

    /**
     * Tika 解析器（支持 PDF、Word、Excel、PPT 等多种格式）
     */
    TIKA("Tika"),

    /**
     * Markdown 解析器
     */
    MARKDOWN("Markdown");

    /**
     * 解析器类型名称
     */
    private final String type;
}
