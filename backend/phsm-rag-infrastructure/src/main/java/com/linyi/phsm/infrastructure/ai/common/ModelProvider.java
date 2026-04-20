package com.linyi.phsm.infrastructure.ai.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 模型提供商枚举
 * 统一管理提供商名称，避免散落的字符串常量
 */
@Getter
@RequiredArgsConstructor
public enum ModelProvider {

    /**
     * Ollama 本地模型服务
     */
    OLLAMA("ollama"),

    /**
     * 阿里云百炼大模型平台
     */
    BAI_LIAN("bailian"),

    /**
     * 硅基流动 AI 模型服务
     */
    SILICON_FLOW("siliconflow"),

    /**
     * 空实现，用于测试或占位
     */
    NOOP("noop");

    private final String id;

    public boolean matches(String provider) {
        return provider != null && provider.equalsIgnoreCase(id);
    }
}
