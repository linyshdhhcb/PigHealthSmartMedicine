

package com.nageoffer.ai.ragent.core.chunk;

import java.util.List;

/**
 * 文本分块器核心接口
 * 定义统一的文本分块能力
 */
public interface ChunkingStrategy {

    /**
     * 获取分块器类型标识
     *
     * @return 分块器类型名称
     */
    ChunkingMode getType();

    /**
     * 对文本进行分块处理
     *
     * @param text   待分块的原始文本内容
     * @param config 分块配置参数
     * @return 分块后的结果列表
     */
    List<VectorChunk> chunk(String text, ChunkingOptions config);
}
