package com.nageoffer.ai.ragent.pig.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nageoffer.ai.ragent.framework.convention.Result;
import com.nageoffer.ai.ragent.framework.web.Results;
import com.nageoffer.ai.ragent.pig.controller.vo.HealthMonitorVO;
import com.nageoffer.ai.ragent.pig.service.HealthMonitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 健康监测Controller
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/health-monitor")
@SaCheckLogin
public class HealthMonitorController {

    private final HealthMonitorService healthMonitorService;

    /**
     * 分页查询健康监测记录列表
     */
    @GetMapping("/list")
    @SaCheckPermission("pig:view")
    public Result<Page<HealthMonitorVO>> listHealthMonitors(@RequestParam(required = false) Long pigId,
                                                            @RequestParam(defaultValue = "1") Integer pageNum,
                                                            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Results.success(healthMonitorService.listHealthMonitors(pigId, pageNum, pageSize));
    }

    /**
     * 根据ID查询健康监测记录详情
     */
    @GetMapping("/{id}")
    @SaCheckPermission("pig:view")
    public Result<HealthMonitorVO> getHealthMonitorById(@PathVariable Long id) {
        return Results.success(healthMonitorService.getHealthMonitorById(id));
    }

    /**
     * 创建健康监测记录
     */
    @PostMapping
    @SaCheckPermission("pig:update")
    public Result<Long> createHealthMonitor(@RequestBody HealthMonitorVO healthMonitorVO) {
        return Results.success(healthMonitorService.createHealthMonitor(healthMonitorVO));
    }

    /**
     * 更新健康监测记录
     */
    @PutMapping
    @SaCheckPermission("pig:update")
    public Result<Void> updateHealthMonitor(@RequestBody HealthMonitorVO healthMonitorVO) {
        healthMonitorService.updateHealthMonitor(healthMonitorVO);
        return Results.success();
    }

    /**
     * 删除健康监测记录
     */
    @DeleteMapping("/{id}")
    @SaCheckPermission("pig:delete")
    public Result<Void> deleteHealthMonitor(@PathVariable Long id) {
        healthMonitorService.deleteHealthMonitor(id);
        return Results.success();
    }
}
