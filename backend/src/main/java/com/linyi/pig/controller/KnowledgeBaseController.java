package com.linyi.pig.controller;

import com.linyi.pig.common.model.PageResult;
import com.linyi.pig.common.model.Result;
import com.linyi.pig.entity.KnowledgeBase;
import com.linyi.pig.entity.vo.knowledge.KnowledgeBaseQueryVo;
import com.linyi.pig.service.KnowledgeBaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "知识库管理")
@Validated
@RestController
@RequestMapping("/knowledgeBase")
@RequiredArgsConstructor
public class KnowledgeBaseController {

    private final KnowledgeBaseService knowledgeBaseService;

    @Operation(summary = "分页查询知识库")
    @PostMapping("/page")
    public Result<PageResult<KnowledgeBase>> page(@RequestBody KnowledgeBaseQueryVo vo) {
        return Result.success(knowledgeBaseService.page(vo));
    }

    @Operation(summary = "根据ID查询知识库")
    @GetMapping("/getInfo")
    public Result<KnowledgeBase> getInfo(@RequestParam("id") Long id) {
        return Result.success(knowledgeBaseService.getById(id));
    }

    @Operation(summary = "新增知识库")
    @PostMapping("/add")
    public Result<KnowledgeBase> add(@RequestBody KnowledgeBase knowledgeBase) {
        if (knowledgeBase.getCreatedBy() == null || knowledgeBase.getCreatedBy().isEmpty()) {
            knowledgeBase.setCreatedBy("system");
        }
        knowledgeBaseService.save(knowledgeBase);
        return Result.success(knowledgeBase);
    }

    @Operation(summary = "修改知识库")
    @PutMapping("/update")
    public Result<Boolean> update(@RequestBody KnowledgeBase knowledgeBase) {
        return Result.success(knowledgeBaseService.updateById(knowledgeBase));
    }

    @Operation(summary = "删除知识库（含关联文档和向量）")
    @DeleteMapping("/delete")
    public Result<Boolean> delete(@RequestParam("id") Long id) {
        return Result.success(knowledgeBaseService.removeWithCollectionById(id));
    }
}
