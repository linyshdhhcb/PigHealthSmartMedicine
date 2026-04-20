package com.linyi.phsm.application.ingestion.node;

import com.linyi.phsm.domain.ingestion.model.context.IngestionContext;
import com.linyi.phsm.domain.ingestion.model.pipeline.NodeConfig;
import com.linyi.phsm.domain.ingestion.model.result.NodeResult;

/**
 * 摄取节点接口，定义了数据摄取流程中的基本单元
 * 每个节点负责执行特定的处理逻辑
 */
public interface IngestionNode {

    /**
     * 获取节点类型标识
     *
     * @return 节点类型的字符串表示
     */
    String getNodeType();

    /**
     * 执行节点的具体逻辑
     *
     * @param context 摄取过程中的上下文信息，包含共享状态和数据
     * @param config  当前节点的配置信息
     * @return 节点执行的结果
     */
    NodeResult execute(IngestionContext context, NodeConfig config);
}
