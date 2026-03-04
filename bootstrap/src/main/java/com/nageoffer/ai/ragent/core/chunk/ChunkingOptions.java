

package com.nageoffer.ai.ragent.core.chunk;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * 分块配置对象
 * 统一的分块参数配置，支持各种分块策略
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChunkingOptions {

    /**
     * 块的目标大小（字符数）
     */
    @Builder.Default
    private Integer chunkSize = 512;

    /**
     * 相邻块之间的重叠大小
     */
    @Builder.Default
    private Integer overlapSize = 128;

    /**
     * 自定义分割符（用于特定策略）
     */
    private String separator;

    /**
     * 扩展元数据（用于传递策略特定参数）
     */
    @Builder.Default
    private Map<String, Object> metadata = new HashMap<>();

    /**
     * 获取元数据值
     */
    @SuppressWarnings("unchecked")
    public <T> T getMetadata(String key, T defaultValue) {
        Object value = metadata.get(key);
        return value != null ? (T) value : defaultValue;
    }
}
