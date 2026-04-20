package com.linyi.phsm.domain.ingestion.model.chunk;

import java.util.Map;

/**
 * 文本边界切分配置
 * 供结构感知切分等基于文本边界的切分策略共用
 *
 * @param targetChars  目标块大小（字符数）
 * @param overlapChars 相邻块重叠大小（字符数）
 * @param maxChars     块的硬上限（字符数）
 * @param minChars     块的最小下限（字符数），小于此值会与后续块合并
 */
public record TextBoundaryOptions(
        int targetChars,
        int overlapChars,
        int maxChars,
        int minChars
) implements ChunkingOptions {

    @Override
    public Map<String, Integer> toConfigMap() {
        return Map.of("targetChars", targetChars, "overlapChars", overlapChars,
                "maxChars", maxChars, "minChars", minChars);
    }
}
