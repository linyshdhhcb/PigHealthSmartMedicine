package com.linyi.phsm.domain.rag.model.vo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * RAG Trace 运行记录
 */
@Data
@Builder
public class RagTraceRunVO {

    private String traceId;

    private String traceName;

    private String entryMethod;

    private String conversationId;

    private String taskId;

    private String userId;

    private String username;

    private String status;

    private String errorMessage;

    private Long durationMs;

    private Date startTime;

    private Date endTime;
}
