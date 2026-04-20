package com.linyi.phsm.domain.rag.model.request;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * RAG Trace 运行记录分页请求
 */
@Data
public class RagTraceRunPageRequest extends Page {

    private String traceId;

    private String conversationId;

    private String taskId;

    private String status;
}
