

package com.nageoffer.ai.ragent.ingestion.controller.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;

/**
 * 摄取任务节点视图对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngestionTaskNodeVO {

    /**
     * ID
     */
    private String id;

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 流水线ID
     */
    private String pipelineId;

    /**
     * 节点ID
     */
    private String nodeId;

    /**
     * 节点类型
     * 如 fetcher、parser、chunker 等
     */
    private String nodeType;

    /**
     * 节点排序
     */
    private Integer nodeOrder;

    /**
     * 状态 (如: success, failed, skipped)
     */
    private String status;

    /**
     * 耗时（毫秒）
     */
    private Long durationMs;

    /**
     * 消息
     */
    private String message;

    /**
     * 错误消息
     */
    private String errorMessage;

    /**
     * 输出结果
     */
    private Map<String, Object> output;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
