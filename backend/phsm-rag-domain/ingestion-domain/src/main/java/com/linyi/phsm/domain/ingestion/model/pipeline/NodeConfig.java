package com.linyi.phsm.domain.ingestion.model.pipeline;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 管道节点配置实体类
 * 定义摄取管道中单个节点的配置信息，包括节点标识、类型、设置参数以及执行条件等
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NodeConfig {

    /**
     * 节点的唯一标识符
     */
    private String nodeId;

    /**
     * 节点类型
     * 对应节点类型枚举值，如 fetcher、parser 等
     */
    private String nodeType;

    /**
     * 节点的配置参数
     * 不同类型的节点有不同的配置结构
     */
    private JsonNode settings;

    /**
     * 节点执行的条件表达式
     * 满足条件时才执行该节点
     */
    private JsonNode condition;

    /**
     * 下一个节点ID
     * 用于定义管道中节点的执行顺序
     */
    private String nextNodeId;
}
