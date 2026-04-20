package com.linyi.phsm.domain.ingestion.model.chunk;

import java.util.Map;

/**
 * 分块配置 sealed interface
 * 通过具体 record 实现类型安全的配置传递，消除魔法字符串
 *
 * @see FixedSizeOptions 固定大小切分配置
 * @see TextBoundaryOptions 文本边界切分配置（结构感知等）
 */
public sealed interface ChunkingOptions permits FixedSizeOptions, TextBoundaryOptions {

    /**
     * 将配置导出为 Map，用于 API 返回和配置校验
     */
    Map<String, Integer> toConfigMap();
}
