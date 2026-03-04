

package com.nageoffer.ai.ragent.infra.rerank;

import com.nageoffer.ai.ragent.framework.convention.RetrievedChunk;
import com.nageoffer.ai.ragent.infra.model.ModelTarget;

import java.util.List;

/**
 * Rerank客户端接口
 * 用于对检索到的文档片段进行重新排序，以提高检索结果的相关性
 */
public interface RerankClient {

    /**
     * 获取Rerank服务提供商名称
     *
     * @return 提供商标识，如 "bailian"、"jina" 等
     */
    String provider();

    /**
     * 对检索到的文档片段进行重新排序
     *
     * @param query      用户查询文本
     * @param candidates 待排序的候选文档片段列表
     * @param topN       返回前N个最相关的结果
     * @param target     目标模型配置信息
     * @return 重新排序后的文档片段列表，按相关性从高到低排序
     */
    List<RetrievedChunk> rerank(String query, List<RetrievedChunk> candidates, int topN, ModelTarget target);
}
