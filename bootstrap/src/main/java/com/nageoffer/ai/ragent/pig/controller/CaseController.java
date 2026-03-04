package com.nageoffer.ai.ragent.pig.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nageoffer.ai.ragent.framework.convention.Result;
import com.nageoffer.ai.ragent.framework.web.Results;
import com.nageoffer.ai.ragent.pig.controller.vo.CaseVO;
import com.nageoffer.ai.ragent.pig.service.CaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 病例信息Controller
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/case")
public class CaseController {

    private final CaseService caseService;

    /**
     * 分页查询病例列表
     */
    @GetMapping("/list")
    public Result<Page<CaseVO>> listCases(@RequestParam(required = false) Long pigId,
                                          @RequestParam(required = false) Long userId,
                                          @RequestParam(required = false) String status,
                                          @RequestParam(defaultValue = "1") Integer pageNum,
                                          @RequestParam(defaultValue = "10") Integer pageSize) {
        return Results.success(caseService.listCases(pigId, userId, status, pageNum, pageSize));
    }

    /**
     * 根据ID查询病例详情
     */
    @GetMapping("/{id}")
    public Result<CaseVO> getCaseById(@PathVariable Long id) {
        return Results.success(caseService.getCaseById(id));
    }

    /**
     * 根据对话ID查询病例
     */
    @GetMapping("/conversation/{conversationId}")
    public Result<CaseVO> getCaseByConversationId(@PathVariable String conversationId) {
        return Results.success(caseService.getCaseByConversationId(conversationId));
    }

    /**
     * 创建病例
     */
    @PostMapping
    public Result<Long> createCase(@RequestBody CaseVO caseVO) {
        return Results.success(caseService.createCase(caseVO));
    }

    /**
     * 更新病例信息
     */
    @PutMapping
    public Result<Void> updateCase(@RequestBody CaseVO caseVO) {
        caseService.updateCase(caseVO);
        return Results.success();
    }

    /**
     * 删除病例
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteCase(@PathVariable Long id) {
        caseService.deleteCase(id);
        return Results.success();
    }
}
