

package com.nageoffer.ai.ragent.knowledge.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 文档来源类型枚举
 */
@Getter
@RequiredArgsConstructor
public enum SourceType {

    /**
     * 本地文件上传
     */
    FILE("file"),

    /**
     * 远程URL获取
     */
    URL("url");

    /**
     * 来源类型值
     */
    private final String value;

    /**
     * 根据值获取枚举
     *
     * @param value 来源类型值
     * @return 对应的枚举，如果未找到返回 null
     */
    public static SourceType fromValue(String value) {
        if (value == null) {
            return null;
        }
        String normalized = value.trim().toLowerCase();
        // 兼容多种文件类型的别名
        if ("file".equals(normalized) || "localfile".equals(normalized) || "local_file".equals(normalized)) {
            return FILE;
        }
        if ("url".equals(normalized)) {
            return URL;
        }
        return null;
    }
}
