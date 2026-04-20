package com.linyi.phsm.interfaces.rest.rag;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.linyi.phsm.framework.convention.Result;
import com.linyi.phsm.framework.web.Results;
import com.linyi.phsm.domain.rag.model.request.RagTraceRunPageRequest;
import com.linyi.phsm.domain.rag.model.vo.RagTraceDetailVO;
import com.linyi.phsm.domain.rag.model.vo.RagTraceNodeVO;
import com.linyi.phsm.domain.rag.model.vo.RagTraceRunVO;
import com.linyi.phsm.application.rag.service.RagTraceQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * RAG Trace 查询接口
 */
@RestController
@RequestMapping("/rag")
@RequiredArgsConstructor
public class RagTraceController {

    private final RagTraceQueryService ragTraceQueryService;

    /**
     * 分页查询链路运行记录
     */
    @GetMapping("/traces/runs")
    public Result<IPage<RagTraceRunVO>> pageRuns(RagTraceRunPageRequest request) {
        return Results.success(ragTraceQueryService.pageRuns(request));
    }

    /**
     * 查询链路详情（包含节点）
     */
    @GetMapping("/traces/runs/{traceId}")
    public Result<RagTraceDetailVO> detail(@PathVariable String traceId) {
        return Results.success(ragTraceQueryService.detail(traceId));
    }

    /**
     * 仅查询链路节点
     */
    @GetMapping("/traces/runs/{traceId}/nodes")
    public Result<List<RagTraceNodeVO>> nodes(@PathVariable String traceId) {
        return Results.success(ragTraceQueryService.listNodes(traceId));
    }
}
