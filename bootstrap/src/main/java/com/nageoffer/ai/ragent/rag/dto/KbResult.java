

package com.nageoffer.ai.ragent.rag.dto;

import com.nageoffer.ai.ragent.framework.convention.RetrievedChunk;

import java.util.List;
import java.util.Map;

/**
 * KB 检索结果
 *
 * @param groupedContext 分组后的上下文文本
 * @param intentChunks   意图 ID -> 分片列表
 */
public record KbResult(String groupedContext, Map<String, List<RetrievedChunk>> intentChunks) {
    /**
     * 空结果
     */
    public static KbResult empty() {
        return new KbResult("", Map.of());
    }
}
