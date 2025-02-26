package com.linyi.pig.controller;

import com.linyi.pig.common.model.PageResult;
import com.linyi.pig.common.model.Result;
import com.linyi.pig.entity.Files;
import com.linyi.pig.service.FilesService;
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
* @ClassName: FilesController
* @Version: 1.0
* @Description: 文件信息 控制层
*/

@Tag(name = "文件信息管理模块")
@Slf4j
@Validated
@RestController
@RequestMapping("/files")
@SuppressWarnings({"unchecked", "rawtypes"})
public class FilesController{

    @Autowired
    private FilesService filesService;

    /**
     * 分页查询文件信息
     *
     * @param filesQueryVo 分页查询实体
     * @return Result<PageResult<Files>> 返回分页数据
     */
    @Operation(summary = "分页查询文件信息")
    @PostMapping("/filesPage")
    public Result<PageResult<Files>> filesPage(@RequestBody FilesQueryVo filesQueryVo) {
        return Result.success(filesService.filesPage(filesQueryVo));
    }

    /**
     * 新增文件信息
     *
     * @param filesAddVo 新增实体
     * @return Result<Boolean> 返回结果(true/false)
     */
    @Operation(summary = "新增文件信息")
    @PostMapping("/filesAdd")
    public Result<Boolean> filesAdd(@RequestBody FilesAddVo filesAddVo) {
        return Result.success(filesService.filesAdd(filesAddVo));
    }

    /**
     * 根据主键ID删除文件信息
     *
     * @param id 主键id
     * @return Result<Boolean> 返回结果(true/false)
     */
    @Operation(summary = "根据主键ID删除文件信息")
    @DeleteMapping("filesDelete")
    public Result<Boolean> filesDelete(@RequestParam Serializable id) {
        return Result.success(filesService.removeById(id));
    }

    /**
    * 根据主键ID批量删除文件信息
    *
    * @param ids 主键id集合
    * @return Result<Boolean> 返回结果(true/false)
    */
    @Operation(summary = "根据主键ID批量删除文件信息")
    @DeleteMapping("filesListDelete")
    public Result<Boolean> filesListDelete(@RequestParam List<Serializable> ids) {
        return Result.success(filesService.removeByIds(ids));
        }

    /**
     * 根据主键ID修改文件信息
     *
     * @param filesUpdateVo 修改实体
     * @return Result<Boolean> 返回结果(true/false)
     */
    @Operation(summary = "根据主键ID修改文件信息")
    @PutMapping("filesUpdate")
    public Result<Boolean> filesUpdate(@RequestBody FilesUpdateVo filesUpdateVo) {
        return Result.success(filesService.filesUpdate(filesUpdateVo));
    }

    /**
     * 根据主键ID查询文件信息
     *
     * @param id 主键id
     * @return Result<Files> 返回文件信息
     */
    @Operation(summary = "根据主键ID查询文件信息")
    @GetMapping("/getInfo")
    public Result<Files> filesUpdate(@RequestParam Serializable id) {
        return Result.success(filesService.getById(id));
    }

}
