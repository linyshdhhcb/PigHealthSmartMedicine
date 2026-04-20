package com.linyi.phsm.interfaces.rest.knowledge;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.linyi.phsm.domain.knowledge.model.request.KnowledgeDocumentPageRequest;
import com.linyi.phsm.domain.knowledge.model.request.KnowledgeDocumentUploadRequest;
import com.linyi.phsm.domain.knowledge.model.request.KnowledgeDocumentUpdateRequest;
import com.linyi.phsm.domain.knowledge.model.vo.KnowledgeDocumentVO;
import com.linyi.phsm.domain.knowledge.model.vo.KnowledgeDocumentChunkLogVO;
import com.linyi.phsm.domain.knowledge.model.vo.KnowledgeDocumentSearchVO;
import com.linyi.phsm.framework.convention.Result;
import com.linyi.phsm.framework.web.Results;
import com.linyi.phsm.application.knowledge.service.KnowledgeDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 知识库文档管理控制器
 * 提供文档的上传、分块、删除、查询、启用/禁用等功能
 */
@RestController
@RequestMapping("/knowledge-base")
@RequiredArgsConstructor
@Validated
public class KnowledgeDocumentController {

    private final KnowledgeDocumentService documentService;

    /**
     * 上传文档：入库记录 + 文件落盘，返回文档ID
     */
    @PostMapping(value = "/{kb-id}/docs/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<KnowledgeDocumentVO> upload(@PathVariable("kb-id") String kbId,
                                              @RequestPart(value = "file", required = false) MultipartFile file,
                                              @ModelAttribute KnowledgeDocumentUploadRequest requestParam) {
        return Results.success(documentService.upload(kbId, requestParam, file));
    }

    /**
     * 开始分块：抽取文本 -> 分块 -> 嵌入并写入向量库
     */
    @PostMapping("/docs/{doc-id}/chunk")
    public Result<Void> startChunk(@PathVariable(value = "doc-id") String docId) {
        documentService.startChunk(docId);
        return Results.success();
    }

    /**
     * 删除文档：逻辑删除。可选同时删除向量库中该文档的所有 chunk
     */
    @DeleteMapping("/docs/{doc-id}")
    public Result<Void> delete(@PathVariable(value = "doc-id") String docId) {
        documentService.delete(docId);
        return Results.success();
    }

    /**
     * 查询文档详情
     */
    @GetMapping("/docs/{docId}")
    public Result<KnowledgeDocumentVO> get(@PathVariable String docId) {
        return Results.success(documentService.get(docId));
    }

    /**
     * 更新文档信息
     */
    @PutMapping("/docs/{docId}")
    public Result<Void> update(@PathVariable String docId,
                               @RequestBody KnowledgeDocumentUpdateRequest requestParam) {
        documentService.update(docId, requestParam);
        return Results.success();
    }

    /**
     * 分页查询文档列表（支持状态/关键字过滤）
     */
    @GetMapping("/{kb-id}/docs")
    public Result<IPage<KnowledgeDocumentVO>> page(@PathVariable(value = "kb-id") String kbId,
                                                   KnowledgeDocumentPageRequest requestParam) {
        return Results.success(documentService.page(kbId, requestParam));
    }

    /**
     * 搜索文档（全局检索建议）
     */
    @GetMapping("/docs/search")
    public Result<List<KnowledgeDocumentSearchVO>> search(@RequestParam(value = "keyword", required = false) String keyword,
                                                          @RequestParam(value = "limit", defaultValue = "8") int limit) {
        return Results.success(documentService.search(keyword, limit));
    }

    /**
     * 启用/禁用文档
     */
    @PatchMapping("/docs/{docId}/enable")
    public Result<Void> enable(@PathVariable String docId,
                               @RequestParam("value") boolean enabled) {
        documentService.enable(docId, enabled);
        return Results.success();
    }

    /**
     * 查询文档分块日志列表
     */
    @GetMapping("/docs/{docId}/chunk-logs")
    public Result<IPage<KnowledgeDocumentChunkLogVO>> getChunkLogs(@PathVariable String docId,
                                                                   Page<KnowledgeDocumentChunkLogVO> page) {
        return Results.success(documentService.getChunkLogs(docId, page));
    }
}
