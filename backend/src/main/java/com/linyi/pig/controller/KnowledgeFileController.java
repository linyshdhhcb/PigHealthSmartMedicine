package com.linyi.pig.controller;

import com.linyi.pig.common.model.Result;
import com.linyi.pig.common.model.PageResult;
import com.linyi.pig.entity.KnowledgeFile;
import com.linyi.pig.entity.vo.knowledge.KnowledgeFileQueryVo;
import com.linyi.pig.entity.vo.knowledge.KnowledgeFileUpdateVo;
import com.linyi.pig.entity.vo.knowledge.KnowledgeQaRequest;
import com.linyi.pig.entity.vo.knowledge.KnowledgeQaResult;
import com.linyi.pig.service.KnowledgeFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "知识库文件管理")
@Validated
@RestController
@RequestMapping("/knowledge")
@RequiredArgsConstructor
public class KnowledgeFileController {

    private final KnowledgeFileService knowledgeFileService;

    @Operation(summary = "上传知识库文件到 resources/knowledge 并入库")
    @PostMapping("/upload")
    public Result<KnowledgeFile> upload(@RequestParam("file") MultipartFile file,
            @RequestParam(value = "createBy", required = false) Integer createBy,
            @RequestParam(value = "kbId", required = false) Long kbId) {
        KnowledgeFile saved = knowledgeFileService.saveToKnowledge(file, createBy, kbId);
        return Result.success(saved);
    }

    @Operation(summary = "分页查询知识库文件")
    @PostMapping("/page")
    public Result<PageResult<KnowledgeFile>> page(@RequestBody KnowledgeFileQueryVo vo) {
        return Result.success(knowledgeFileService.page(vo));
    }

    @Operation(summary = "根据ID查询")
    @GetMapping("/getInfo")
    public Result<KnowledgeFile> getInfo(@RequestParam("id") Long id) {
        return Result.success(knowledgeFileService.getById(id));
    }

    @Operation(summary = "根据ID删除")
    @DeleteMapping("/delete")
    public Result<Boolean> delete(@RequestParam("id") Long id) {
        return Result.success(knowledgeFileService.removeWithFileById(id));
    }

    @Operation(summary = "批量删除")
    @DeleteMapping("/deleteBatch")
    public Result<Boolean> deleteBatch(@RequestParam("ids") java.util.List<Long> ids) {
        return Result.success(knowledgeFileService.removeWithFileByIds(ids));
    }

    @Operation(summary = "更新备注")
    @PutMapping("/updateRemark")
    public Result<Boolean> updateRemark(@RequestBody KnowledgeFileUpdateVo vo) {
        return Result.success(knowledgeFileService.updateRemark(vo));
    }

    @Operation(summary = "按kbId检索并生成问答")
    @GetMapping("/qaByKbId")
    public Result<KnowledgeQaResult> qaByKbId(@RequestParam Long kbId,
            @RequestParam String question,
            @RequestParam(defaultValue = "5") int topK) {
        return Result.success(knowledgeFileService.qaByKbId(kbId, question, topK));
    }

    @Operation(summary = "按kbId JSON检索并生成问答")
    @PostMapping("/qaByKbId")
    public Result<KnowledgeQaResult> qaByKbIdJson(@RequestBody KnowledgeQaRequest request) {
        int topK = request.getTopK() == null ? 5 : request.getTopK();
        return Result.success(knowledgeFileService.qaByKbId(request.getKbId(), request.getQuestion(), topK));
    }
}
