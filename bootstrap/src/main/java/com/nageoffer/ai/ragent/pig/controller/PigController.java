package com.nageoffer.ai.ragent.pig.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nageoffer.ai.ragent.framework.convention.Result;
import com.nageoffer.ai.ragent.framework.web.Results;
import com.nageoffer.ai.ragent.pig.controller.vo.PigVO;
import com.nageoffer.ai.ragent.pig.service.PigService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 生猪信息Controller
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/pig")
public class PigController {

    private final PigService pigService;

    /**
     * 分页查询生猪列表
     */
    @GetMapping("/list")
    public Result<Page<PigVO>> listPigs(@RequestParam(required = false) Long farmId,
                                        @RequestParam(required = false) Long userId,
                                        @RequestParam(required = false) String healthStatus,
                                        @RequestParam(defaultValue = "1") Integer pageNum,
                                        @RequestParam(defaultValue = "10") Integer pageSize) {
        return Results.success(pigService.listPigs(farmId, userId, healthStatus, pageNum, pageSize));
    }

    /**
     * 根据ID查询生猪详情
     */
    @GetMapping("/{id}")
    public Result<PigVO> getPigById(@PathVariable Long id) {
        return Results.success(pigService.getPigById(id));
    }

    /**
     * 根据耳标号查询生猪
     */
    @GetMapping("/earTag/{earTagNumber}")
    public Result<PigVO> getPigByEarTag(@PathVariable String earTagNumber) {
        return Results.success(pigService.getPigByEarTag(earTagNumber));
    }

    /**
     * 创建生猪
     */
    @PostMapping
    public Result<Long> createPig(@RequestBody PigVO pigVO) {
        return Results.success(pigService.createPig(pigVO));
    }

    /**
     * 更新生猪信息
     */
    @PutMapping
    public Result<Void> updatePig(@RequestBody PigVO pigVO) {
        pigService.updatePig(pigVO);
        return Results.success();
    }

    /**
     * 删除生猪
     */
    @DeleteMapping("/{id}")
    public Result<Void> deletePig(@PathVariable Long id) {
        pigService.deletePig(id);
        return Results.success();
    }
}
