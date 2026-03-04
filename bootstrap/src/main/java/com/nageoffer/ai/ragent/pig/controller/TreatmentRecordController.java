package com.nageoffer.ai.ragent.pig.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nageoffer.ai.ragent.framework.convention.Result;
import com.nageoffer.ai.ragent.framework.web.Results;
import com.nageoffer.ai.ragent.pig.controller.vo.TreatmentRecordVO;
import com.nageoffer.ai.ragent.pig.service.TreatmentRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 治疗记录Controller
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/treatment-record")
@SaCheckLogin
public class TreatmentRecordController {

    private final TreatmentRecordService treatmentRecordService;

    /**
     * 分页查询治疗记录列表
     */
    @GetMapping("/list")
    @SaCheckPermission("case:view")
    public Result<Page<TreatmentRecordVO>> listTreatmentRecords(@RequestParam(required = false) Long caseId,
                                                                @RequestParam(required = false) Long pigId,
                                                                @RequestParam(defaultValue = "1") Integer pageNum,
                                                                @RequestParam(defaultValue = "10") Integer pageSize) {
        return Results.success(treatmentRecordService.listTreatmentRecords(caseId, pigId, pageNum, pageSize));
    }

    /**
     * 根据ID查询治疗记录详情
     */
    @GetMapping("/{id}")
    @SaCheckPermission("case:view")
    public Result<TreatmentRecordVO> getTreatmentRecordById(@PathVariable Long id) {
        return Results.success(treatmentRecordService.getTreatmentRecordById(id));
    }

    /**
     * 创建治疗记录
     */
    @PostMapping
    @SaCheckPermission("case:update")
    public Result<Long> createTreatmentRecord(@RequestBody TreatmentRecordVO treatmentRecordVO) {
        return Results.success(treatmentRecordService.createTreatmentRecord(treatmentRecordVO));
    }

    /**
     * 更新治疗记录
     */
    @PutMapping
    @SaCheckPermission("case:update")
    public Result<Void> updateTreatmentRecord(@RequestBody TreatmentRecordVO treatmentRecordVO) {
        treatmentRecordService.updateTreatmentRecord(treatmentRecordVO);
        return Results.success();
    }

    /**
     * 删除治疗记录
     */
    @DeleteMapping("/{id}")
    @SaCheckPermission("case:delete")
    public Result<Void> deleteTreatmentRecord(@PathVariable Long id) {
        treatmentRecordService.deleteTreatmentRecord(id);
        return Results.success();
    }
}
