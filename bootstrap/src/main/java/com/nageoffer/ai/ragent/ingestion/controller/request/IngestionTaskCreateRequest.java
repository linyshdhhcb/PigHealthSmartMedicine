

package com.nageoffer.ai.ragent.ingestion.controller.request;

import com.nageoffer.ai.ragent.rag.controller.request.DocumentSourceRequest;
import com.nageoffer.ai.ragent.rag.core.vector.VectorSpaceId;
import lombok.Data;

import java.util.Map;

/**
 * 摄取任务创建请求对象
 * 用于接收创建新摄取任务的请求参数，包括管道ID、文档源信息及元数据。
 */
@Data
public class IngestionTaskCreateRequest {

    /**
     * 执行本次摄取的管道ID
     */
    private String pipelineId;

    /**
     * 文档源信息
     */
    private DocumentSourceRequest source;

    /**
     * 摄取任务的元数据信息
     * 自定义的附加属性键值对
     */
    private Map<String, Object> metadata;

    /**
     * 向量空间ID，指定向量数据写入的目标集合
     * 如果不指定，则使用默认的向量空间
     */
    private VectorSpaceId vectorSpaceId;
}
