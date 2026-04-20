package com.linyi.phsm.domain.ingestion.model.request;

import lombok.Data;

import java.util.List;

/**
 * 摄取管道创建请求对象
 * 用于接收创建新摄取管道的请求参数，包括管道名称、描述及节点配置列表
 */
@Data
public class IngestionPipelineCreateRequest {

    /**
     * 管道名称
     */
    private String name;

    /**
     * 管道描述信息
     */
    private String description;

    /**
     * 管道节点配置列表
     */
    private List<IngestionPipelineNodeRequest> nodes;
}
