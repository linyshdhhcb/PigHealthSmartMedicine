package com.linyi.phsm.domain.ingestion.model.pipeline;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 摄取管道定义实体类
 * 定义一个完整的文档摄取管道，包含管道的基本信息和节点配置列表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PipelineDefinition {

    /**
     * 管道的唯一标识符
     */
    private String id;

    /**
     * 管道名称
     */
    private String name;

    /**
     * 管道描述信息
     */
    private String description;

    /**
     * 管道中的节点配置列表
     * 按执行顺序排列的节点配置
     */
    private List<NodeConfig> nodes;
}
