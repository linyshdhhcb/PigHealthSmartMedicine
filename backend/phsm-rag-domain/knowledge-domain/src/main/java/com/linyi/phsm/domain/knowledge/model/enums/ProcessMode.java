package com.linyi.phsm.domain.knowledge.model.enums;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 文档处理模式枚举
 */
@Getter
@RequiredArgsConstructor
public enum ProcessMode {

    /**
     * 分块策略模式（默认）
     */
    CHUNK("chunk"),

    /**
     * Pipeline 管道模式
     */
    PIPELINE("pipeline");

    /**
     * 处理模式值
     */
    private final String value;

    /**
     * 根据值获取枚举
     *
     * @param value 处理模式值
     * @return 对应的枚举，如果未找到返回 null
     */
    public static ProcessMode fromValue(String value) {
        if (value == null) {
            return null;
        }
        String normalized = value.trim().toLowerCase();
        for (ProcessMode mode : values()) {
            if (mode.value.equals(normalized)) {
                return mode;
            }
        }
        return null;
    }

    /**
     * 解析处理模式，空值或非法值抛出异常
     */
    public static ProcessMode normalize(String value) {
        if (StrUtil.isBlank(value)) {
            throw new IllegalArgumentException("处理模式不能为空");
        }
        ProcessMode result = fromValue(value);
        if (result == null) {
            throw new IllegalArgumentException("不支持的处理模式: " + value);
        }
        return result;
    }
}
