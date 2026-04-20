package com.linyi.phsm.application.rag.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.linyi.phsm.domain.rag.model.request.RagTraceRunPageRequest;
import com.linyi.phsm.domain.rag.model.vo.RagTraceDetailVO;
import com.linyi.phsm.domain.rag.model.vo.RagTraceNodeVO;
import com.linyi.phsm.domain.rag.model.vo.RagTraceRunVO;

import java.util.List;

/**
 * RAG Trace 查询服务
 */
public interface RagTraceQueryService {

    IPage<RagTraceRunVO> pageRuns(RagTraceRunPageRequest request);

    RagTraceDetailVO detail(String traceId);

    List<RagTraceNodeVO> listNodes(String traceId);
}
