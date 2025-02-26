package com.linyi.pig.controller;

import com.linyi.pig.common.model.PageResult;
import com.linyi.pig.common.model.Result;
import com.linyi.pig.entity.IllnessKind;
import com.linyi.pig.entity.vo.illnessKind.IllnessKindAddVo;
import com.linyi.pig.entity.vo.illnessKind.IllnessKindQueryVo;
import com.linyi.pig.entity.vo.illnessKind.IllnessKindUpdateVo;
import com.linyi.pig.service.IllnessKindService;
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
* @ClassName: IllnessKindController
* @Version: 1.0
* @Description: 疾病种类 控制层
*/

@Tag(name = "疾病种类管理模块")
@Slf4j
@Validated
@RestController
@RequestMapping("/illnessKind")
@SuppressWarnings({"unchecked", "rawtypes"})
public class IllnessKindController{

    @Autowired
    private IllnessKindService illnessKindService;

    /**
     * 分页查询疾病种类
     *
     * @param illnessKindQueryVo 分页查询实体
     * @return Result<PageResult<IllnessKind>> 返回分页数据
     */
    @Operation(summary = "分页查询疾病种类")
    @PostMapping("/illnessKindPage")
    public Result<PageResult<IllnessKind>> illnessKindPage(@RequestBody IllnessKindQueryVo illnessKindQueryVo) {
        return Result.success(illnessKindService.illnessKindPage(illnessKindQueryVo));
    }

    /**
     * 新增疾病种类
     *
     * @param illnessKindAddVo 新增实体
     * @return Result<Boolean> 返回结果(true/false)
     */
    @Operation(summary = "新增疾病种类")
    @PostMapping("/illnessKindAdd")
    public Result<Boolean> illnessKindAdd(@RequestBody IllnessKindAddVo illnessKindAddVo) {
        return Result.success(illnessKindService.illnessKindAdd(illnessKindAddVo));
    }

    /**
     * 根据主键ID删除疾病种类
     *
     * @param id 主键id
     * @return Result<Boolean> 返回结果(true/false)
     */
    @Operation(summary = "根据主键ID删除疾病种类")
    @DeleteMapping("illnessKindDelete")
    public Result<Boolean> illnessKindDelete(@RequestParam Serializable id) {
        return Result.success(illnessKindService.removeById(id));
    }

    /**
    * 根据主键ID批量删除疾病种类
    *
    * @param ids 主键id集合
    * @return Result<Boolean> 返回结果(true/false)
    */
    @Operation(summary = "根据主键ID批量删除疾病种类")
    @DeleteMapping("illnessKindListDelete")
    public Result<Boolean> illnessKindListDelete(@RequestParam List<Serializable> ids) {
        return Result.success(illnessKindService.removeByIds(ids));
        }

    /**
     * 根据主键ID修改疾病种类
     *
     * @param illnessKindUpdateVo 修改实体
     * @return Result<Boolean> 返回结果(true/false)
     */
    @Operation(summary = "根据主键ID修改疾病种类")
    @PutMapping("illnessKindUpdate")
    public Result<Boolean> illnessKindUpdate(@RequestBody IllnessKindUpdateVo illnessKindUpdateVo) {
        return Result.success(illnessKindService.illnessKindUpdate(illnessKindUpdateVo));
    }

    /**
     * 根据主键ID查询疾病种类
     *
     * @param id 主键id
     * @return Result<IllnessKind> 返回疾病种类
     */
    @Operation(summary = "根据主键ID查询疾病种类")
    @GetMapping("/getInfo")
    public Result<IllnessKind> illnessKindUpdate(@RequestParam Serializable id) {
        return Result.success(illnessKindService.getById(id));
    }

}
