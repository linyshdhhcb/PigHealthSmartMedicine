

package com.nageoffer.ai.ragent.rag.controller.vo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * RAG Trace 节点明细
 */
@Data
@Builder
public class RagTraceNodeVO {

    private String traceId;

    private String nodeId;

    private String parentNodeId;

    private Integer depth;

    private String nodeType;

    private String nodeName;

    private String className;

    private String methodName;

    private String status;

    private String errorMessage;

    private Long durationMs;

    private Date startTime;

    private Date endTime;
}
