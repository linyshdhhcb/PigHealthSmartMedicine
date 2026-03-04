package com.nageoffer.ai.ragent.pig.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nageoffer.ai.ragent.framework.convention.Result;
import com.nageoffer.ai.ragent.framework.web.Results;
import com.nageoffer.ai.ragent.pig.controller.vo.DrugVO;
import com.nageoffer.ai.ragent.pig.service.DrugService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 兽药信息Controller
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/drug")
public class DrugController {

    private final DrugService drugService;

    /**
     * 分页查询兽药列表
     */
    @GetMapping("/list")
    public Result<Page<DrugVO>> listDrugs(@RequestParam(required = false) String name,
                                          @RequestParam(required = false) String drugType,
                                          @RequestParam(defaultValue = "1") Integer pageNum,
                                          @RequestParam(defaultValue = "10") Integer pageSize) {
        return Results.success(drugService.listDrugs(name, drugType, pageNum, pageSize));
    }

    /**
     * 根据ID查询兽药详情
     */
    @GetMapping("/{id}")
    public Result<DrugVO> getDrugById(@PathVariable Long id) {
        return Results.success(drugService.getDrugById(id));
    }

    /**
     * 搜索兽药（按名称或适应症）
     */
    @GetMapping("/search")
    public Result<Page<DrugVO>> searchDrugs(@RequestParam String keyword,
                                            @RequestParam(defaultValue = "1") Integer pageNum,
                                            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Results.success(drugService.searchDrugs(keyword, pageNum, pageSize));
    }
}
