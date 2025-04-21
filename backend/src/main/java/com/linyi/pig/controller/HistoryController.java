package com.linyi.pig.controller;

import com.linyi.pig.common.model.PageResult;
import com.linyi.pig.common.model.Result;
import com.linyi.pig.entity.History;
import com.linyi.pig.entity.vo.history.HistoryAddVo;
import com.linyi.pig.entity.vo.history.HistoryQueryVo;
import com.linyi.pig.entity.vo.history.HistoryUpdateVo;
import com.linyi.pig.service.HistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;


/**
* @Author: linyi
* @Date: 2025-02-25 17:38:38
* @ClassName: HistoryController
* @Version: 1.0
* @Description: 操作记录 控制层
*/

@Tag(name = "操作记录管理模块")
@Slf4j
@Validated
@RestController
@RequestMapping("/history")
@SuppressWarnings({"unchecked", "rawtypes"})
public class HistoryController{

    @Autowired
    private HistoryService historyService;

    /**
     * 分页查询操作记录
     *
     * @param historyQueryVo 分页查询实体
     * @return Result<PageResult<History>> 返回分页数据
     */
    @Operation(summary = "分页查询操作记录")
    @PostMapping("/historyPage")
    public Result<PageResult<History>> historyPage(@RequestBody HistoryQueryVo historyQueryVo) {
        return Result.success(historyService.historyPage(historyQueryVo));
    }

    /**
     * 新增操作记录
     *
     * @param historyAddVo 新增实体
     * @return Result<Boolean> 返回结果(true/false)
     */
    @Operation(summary = "新增操作记录")
    @PostMapping("/historyAdd")
    public Result<Boolean> historyAdd(@RequestBody HistoryAddVo historyAddVo) {
        return Result.success(historyService.historyAdd(historyAddVo));
    }

    /**
     * 根据主键ID删除操作记录
     *
     * @param id 主键id
     * @return Result<Boolean> 返回结果(true/false)
     */
    @Operation(summary = "根据主键ID删除操作记录")
    @DeleteMapping("historyDelete")
    public Result<Boolean> historyDelete(@RequestParam Serializable id) {
        return Result.success(historyService.removeById(id));
    }

    /**
    * 根据主键ID批量删除操作记录
    *
    * @param ids 主键id集合
    * @return Result<Boolean> 返回结果(true/false)
    */
    @Operation(summary = "根据主键ID批量删除操作记录")
    @DeleteMapping("historyListDelete")
    public Result<Boolean> historyListDelete(@RequestParam List<Serializable> ids) {
        return Result.success(historyService.removeByIds(ids));
        }

    /**
     * 根据主键ID修改操作记录
     *
     * @param historyUpdateVo 修改实体
     * @return Result<Boolean> 返回结果(true/false)
     */
    @Operation(summary = "根据主键ID修改操作记录")
    @PutMapping("historyUpdate")
    public Result<Boolean> historyUpdate(@RequestBody HistoryUpdateVo historyUpdateVo) {
        return Result.success(historyService.historyUpdate(historyUpdateVo));
    }

    /**
     * 根据主键ID查询操作记录
     *
     * @param id 主键id
     * @return Result<History> 返回操作记录
     */
    @Operation(summary = "根据主键ID查询操作记录")
    @GetMapping("/getInfo")
    public Result<History> historyUpdate(@RequestParam Serializable id) {
        return Result.success(historyService.getById(id));
    }

}
