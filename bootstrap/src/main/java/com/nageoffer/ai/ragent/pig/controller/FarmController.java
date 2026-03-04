package com.nageoffer.ai.ragent.pig.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nageoffer.ai.ragent.framework.convention.Result;
import com.nageoffer.ai.ragent.framework.web.Results;
import com.nageoffer.ai.ragent.pig.controller.vo.FarmVO;
import com.nageoffer.ai.ragent.pig.service.FarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 养殖场管理Controller
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/farm")
@SaCheckLogin
public class FarmController {

    private final FarmService farmService;

    /**
     * 分页查询养殖场列表
     */
    @GetMapping("/list")
    @SaCheckPermission("farm:view")
    public Result<Page<FarmVO>> listFarms(@RequestParam(required = false) Long ownerId,
                                          @RequestParam(defaultValue = "1") Integer pageNum,
                                          @RequestParam(defaultValue = "10") Integer pageSize) {
        return Results.success(farmService.listFarms(ownerId, pageNum, pageSize));
    }

    /**
     * 根据ID查询养殖场详情
     */
    @GetMapping("/{id}")
    @SaCheckPermission("farm:view")
    public Result<FarmVO> getFarmById(@PathVariable Long id) {
        return Results.success(farmService.getFarmById(id));
    }

    /**
     * 创建养殖场
     */
    @PostMapping
    @SaCheckPermission("farm:create")
    public Result<Long> createFarm(@RequestBody FarmVO farmVO) {
        return Results.success(farmService.createFarm(farmVO));
    }

    /**
     * 更新养殖场信息
     */
    @PutMapping
    @SaCheckPermission("farm:update")
    public Result<Void> updateFarm(@RequestBody FarmVO farmVO) {
        farmService.updateFarm(farmVO);
        return Results.success();
    }

    /**
     * 删除养殖场
     */
    @DeleteMapping("/{id}")
    @SaCheckPermission("farm:delete")
    public Result<Void> deleteFarm(@PathVariable Long id) {
        farmService.deleteFarm(id);
        return Results.success();
    }
}
