package com.linyi.pig.controller;

import com.linyi.pig.common.model.PageResult;
import com.linyi.pig.common.model.Result;
import com.linyi.pig.entity.Sessions;
import com.linyi.pig.service.SessionsService;
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
* @Date: 2025-02-26 08:42:14
* @ClassName: SessionsController
* @Version: 1.0
* @Description:  控制层
*/

@Tag(name = "管理模块")
@Slf4j
@Validated
@RestController
@RequestMapping("/sessions")
@SuppressWarnings({"unchecked", "rawtypes"})
public class SessionsController{

    @Autowired
    private SessionsService sessionsService;

    /**
     * 分页查询
     *
     * @param sessionsQueryVo 分页查询实体
     * @return Result<PageResult<Sessions>> 返回分页数据
     */
    @Operation(summary = "分页查询")
    @PostMapping("/sessionsPage")
    public Result<PageResult<Sessions>> sessionsPage(@RequestBody SessionsQueryVo sessionsQueryVo) {
        return Result.success(sessionsService.sessionsPage(sessionsQueryVo));
    }

    /**
     * 新增
     *
     * @param sessionsAddVo 新增实体
     * @return Result<Boolean> 返回结果(true/false)
     */
    @Operation(summary = "新增")
    @PostMapping("/sessionsAdd")
    public Result<Boolean> sessionsAdd(@RequestBody SessionsAddVo sessionsAddVo) {
        return Result.success(sessionsService.sessionsAdd(sessionsAddVo));
    }

    /**
     * 根据主键ID删除
     *
     * @param id 主键id
     * @return Result<Boolean> 返回结果(true/false)
     */
    @Operation(summary = "根据主键ID删除")
    @DeleteMapping("sessionsDelete")
    public Result<Boolean> sessionsDelete(@RequestParam Serializable id) {
        return Result.success(sessionsService.removeById(id));
    }

    /**
    * 根据主键ID批量删除
    *
    * @param ids 主键id集合
    * @return Result<Boolean> 返回结果(true/false)
    */
    @Operation(summary = "根据主键ID批量删除")
    @DeleteMapping("sessionsListDelete")
    public Result<Boolean> sessionsListDelete(@RequestParam List<Serializable> ids) {
        return Result.success(sessionsService.removeByIds(ids));
        }

    /**
     * 根据主键ID修改
     *
     * @param sessionsUpdateVo 修改实体
     * @return Result<Boolean> 返回结果(true/false)
     */
    @Operation(summary = "根据主键ID修改")
    @PutMapping("sessionsUpdate")
    public Result<Boolean> sessionsUpdate(@RequestBody SessionsUpdateVo sessionsUpdateVo) {
        return Result.success(sessionsService.sessionsUpdate(sessionsUpdateVo));
    }

    /**
     * 根据主键ID查询
     *
     * @param id 主键id
     * @return Result<Sessions> 返回
     */
    @Operation(summary = "根据主键ID查询")
    @GetMapping("/getInfo")
    public Result<Sessions> sessionsUpdate(@RequestParam Serializable id) {
        return Result.success(sessionsService.getById(id));
    }

}
