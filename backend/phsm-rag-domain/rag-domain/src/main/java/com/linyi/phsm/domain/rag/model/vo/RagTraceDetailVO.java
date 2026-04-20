package com.linyi.phsm.domain.rag.model.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * RAG Trace 详情
 */
@Data
@Builder
public class RagTraceDetailVO {

    private RagTraceRunVO run;

    private List<RagTraceNodeVO> nodes;
}
