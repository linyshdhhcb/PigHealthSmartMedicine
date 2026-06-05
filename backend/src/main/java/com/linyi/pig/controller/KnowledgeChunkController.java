package com.linyi.pig.controller;

import com.linyi.pig.common.model.PageResult;
import com.linyi.pig.common.model.Result;
import com.linyi.pig.entity.KnowledgeChunk;
import com.linyi.pig.entity.vo.knowledge.KnowledgeChunkQueryVo;
import com.linyi.pig.service.KnowledgeChunkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "知识库分块管理")
@Validated
@RestController
@RequestMapping("/knowledgeChunk")
@RequiredArgsConstructor
public class KnowledgeChunkController {

    private final KnowledgeChunkService knowledgeChunkService;

    @Operation(summary = "分页查询知识库分块")
    @PostMapping("/page")
    public Result<PageResult<KnowledgeChunk>> page(@RequestBody KnowledgeChunkQueryVo vo) {
        return Result.success(knowledgeChunkService.page(vo));
    }

    @Operation(summary = "根据ID查询分块")
    @GetMapping("/getInfo")
    public Result<KnowledgeChunk> getInfo(@RequestParam("id") Long id) {
        return Result.success(knowledgeChunkService.getById(id));
    }

    @Operation(summary = "根据ID删除分块")
    @DeleteMapping("/delete")
    public Result<Boolean> delete(@RequestParam("id") Long id) {
        return Result.success(knowledgeChunkService.removeById(id));
    }
}
