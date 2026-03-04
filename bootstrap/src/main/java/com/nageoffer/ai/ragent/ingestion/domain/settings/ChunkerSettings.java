

package com.nageoffer.ai.ragent.ingestion.domain.settings;

import com.nageoffer.ai.ragent.core.chunk.ChunkingMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分块器设置实体类
 * 定义文档分块节点的配置参数，包括分块策略、块大小、重叠大小等
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChunkerSettings {

    /**
     * 分块策略
     * 如固定大小、按句子、按段落、语义切分等
     */
    private ChunkingMode strategy;

    /**
     * 块的目标大小（字符数或token数）
     */
    private Integer chunkSize;

    /**
     * 相邻块之间的重叠大小
     * 用于保持上下文连贯性
     */
    private Integer overlapSize;

    /**
     * 自定义分割符
     * 用于指定文本切分的边界字符
     */
    private String separator;
}
