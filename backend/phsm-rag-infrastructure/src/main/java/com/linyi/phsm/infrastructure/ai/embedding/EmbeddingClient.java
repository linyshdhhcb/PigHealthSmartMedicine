package com.linyi.phsm.infrastructure.ai.embedding;

import com.linyi.phsm.infrastructure.ai.routing.ModelTarget;

import java.util.List;

/**
 * 文本嵌入客户端接口
 * 用于将文本转换为向量表示，支持单个文本和批量文本的嵌入操作
 */
public interface EmbeddingClient {

    /**
     * 获取嵌入服务提供商名称
     *
     * @return 提供商标识字符串
     */
    String provider();

    /**
     * 将单个文本转换为嵌入向量
     *
     * @param text   待嵌入的文本内容
     * @param target 目标模型配置
     * @return 文本的向量表示，以浮点数列表形式返回
     */
    List<Float> embed(String text, ModelTarget target);

    /**
     * 批量将多个文本转换为嵌入向量
     *
     * @param texts  待嵌入的文本列表
     * @param target 目标模型配置
     * @return 文本向量列表，每个文本对应一个向量（浮点数列表）
     */
    List<List<Float>> embedBatch(List<String> texts, ModelTarget target);
}
