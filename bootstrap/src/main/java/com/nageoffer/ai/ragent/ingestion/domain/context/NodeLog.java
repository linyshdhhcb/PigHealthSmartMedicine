

package com.nageoffer.ai.ragent.ingestion.domain.context;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 管道节点日志实体类
 * <p>
 * 记录摄取管道中各个节点的执行信息，包括执行时长、状态、输出等
 * 用于管道执行过程的监控和问题排查
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NodeLog {

    /**
     * 节点的唯一标识符
     */
    private String nodeId;

    /**
     * 节点类型
     * 如 fetcher、parser、chunker 等
     */
    private String nodeType;

    /**
     * 节点执行的日志消息
     */
    private String message;

    /**
     * 节点执行耗时（毫秒）
     */
    private long durationMs;

    /**
     * 节点是否执行成功
     */
    private boolean success;

    /**
     * 节点执行失败时的错误信息
     */
    private String error;

    /**
     * 节点的输出数据
     * 存储节点处理后产生的结构化数据
     */
    private java.util.Map<String, Object> output;
}
