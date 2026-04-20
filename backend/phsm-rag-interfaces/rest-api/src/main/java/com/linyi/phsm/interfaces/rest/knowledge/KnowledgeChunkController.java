package com.linyi.phsm.interfaces.rest.knowledge;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.linyi.phsm.framework.convention.Result;
import com.linyi.phsm.framework.web.Results;
import com.linyi.phsm.domain.knowledge.model.request.KnowledgeChunkBatchRequest;
import com.linyi.phsm.domain.knowledge.model.request.KnowledgeChunkCreateRequest;
import com.linyi.phsm.domain.knowledge.model.request.KnowledgeChunkPageRequest;
import com.linyi.phsm.domain.knowledge.model.request.KnowledgeChunkUpdateRequest;
import com.linyi.phsm.domain.knowledge.model.vo.KnowledgeChunkVO;
import com.linyi.phsm.application.knowledge.service.KnowledgeChunkService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 知识库 Chunk 管理接口
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/knowledge-base/docs")
@Validated
public class KnowledgeChunkController {

    private final KnowledgeChunkService knowledgeChunkService;

    /**
     * 分页查询 Chunk 列表
     */
    @GetMapping("/{doc-id}/chunks")
    public Result<IPage<KnowledgeChunkVO>> pageQuery(@PathVariable("doc-id") String docId,
                                                     @Validated KnowledgeChunkPageRequest requestParam) {
        return Results.success(knowledgeChunkService.pageQuery(docId, requestParam));
    }

    /**
     * 新增 Chunk
     */
    @PostMapping("/{doc-id}/chunks")
    public Result<KnowledgeChunkVO> create(@PathVariable("doc-id") String docId,
                                           @RequestBody KnowledgeChunkCreateRequest request) {
        return Results.success(knowledgeChunkService.create(docId, request));
    }

    /**
     * 更新 Chunk 内容
     */
    @PutMapping("/{doc-id}/chunks/{chunk-id}")
    public Result<Void> update(@PathVariable("doc-id") String docId,
                               @PathVariable("chunk-id") String chunkId,
                               @RequestBody KnowledgeChunkUpdateRequest request) {
        knowledgeChunkService.update(docId, chunkId, request);
        return Results.success();
    }

    /**
     * 删除 Chunk
     */
    @DeleteMapping("/{doc-id}/chunks/{chunk-id}")
    public Result<Void> delete(@PathVariable("doc-id") String docId,
                               @PathVariable("chunk-id") String chunkId) {
        knowledgeChunkService.delete(docId, chunkId);
        return Results.success();
    }

    /**
     * 启用或禁用单条 Chunk
     */
    @PatchMapping("/{doc-id}/chunks/{chunk-id}/enable")
    public Result<Void> enable(@PathVariable("doc-id") String docId,
                               @PathVariable("chunk-id") String chunkId,
                               @RequestParam("value") boolean enabled) {
        knowledgeChunkService.enableChunk(docId, chunkId, enabled);
        return Results.success();
    }

    /**
     * 批量启用或禁用 Chunk
     */
    @PatchMapping("/{doc-id}/chunks/batch-enable")
    public Result<Void> batchEnable(@PathVariable("doc-id") String docId,
                                    @RequestParam("value") boolean enabled,
                                    @RequestBody(required = false) KnowledgeChunkBatchRequest request) {
        knowledgeChunkService.batchToggleEnabled(docId, request, enabled);
        return Results.success();
    }
}
