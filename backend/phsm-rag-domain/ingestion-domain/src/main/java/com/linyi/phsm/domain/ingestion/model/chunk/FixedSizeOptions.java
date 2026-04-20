package com.linyi.phsm.domain.ingestion.model.chunk;

import java.util.Map;

/**
 * 固定大小切分配置
 *
 * @param chunkSize   目标块大小（字符数）
 * @param overlapSize 相邻块重叠大小（字符数）
 */
public record FixedSizeOptions(
        int chunkSize,
        int overlapSize
) implements ChunkingOptions {

    @Override
    public Map<String, Integer> toConfigMap() {
        return Map.of("chunkSize", chunkSize, "overlapSize", overlapSize);
    }
}
