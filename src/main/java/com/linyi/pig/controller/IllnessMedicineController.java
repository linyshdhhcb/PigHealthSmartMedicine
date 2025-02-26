package com.linyi.pig.controller;

import com.linyi.pig.common.model.PageResult;
import com.linyi.pig.common.model.Result;
import com.linyi.pig.entity.IllnessMedicine;
import com.linyi.pig.entity.vo.illnessMedicine.IllnessMedicineAddVo;
import com.linyi.pig.entity.vo.illnessMedicine.IllnessMedicineQueryVo;
import com.linyi.pig.entity.vo.illnessMedicine.IllnessMedicineUpdateVo;
import com.linyi.pig.service.IllnessMedicineService;
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
* @ClassName: IllnessMedicineController
* @Version: 1.0
* @Description: 疾病-药物 控制层
*/

@Tag(name = "疾病-药物管理模块")
@Slf4j
@Validated
@RestController
@RequestMapping("/illnessMedicine")
@SuppressWarnings({"unchecked", "rawtypes"})
public class IllnessMedicineController{

    @Autowired
    private IllnessMedicineService illnessMedicineService;

    /**
     * 分页查询疾病-药物
     *
     * @param illnessMedicineQueryVo 分页查询实体
     * @return Result<PageResult<IllnessMedicine>> 返回分页数据
     */
    @Operation(summary = "分页查询疾病-药物")
    @PostMapping("/illnessMedicinePage")
    public Result<PageResult<IllnessMedicine>> illnessMedicinePage(@RequestBody IllnessMedicineQueryVo illnessMedicineQueryVo) {
        return Result.success(illnessMedicineService.illnessMedicinePage(illnessMedicineQueryVo));
    }

    /**
     * 新增疾病-药物
     *
     * @param illnessMedicineAddVo 新增实体
     * @return Result<Boolean> 返回结果(true/false)
     */
    @Operation(summary = "新增疾病-药物")
    @PostMapping("/illnessMedicineAdd")
    public Result<Boolean> illnessMedicineAdd(@RequestBody IllnessMedicineAddVo illnessMedicineAddVo) {
        return Result.success(illnessMedicineService.illnessMedicineAdd(illnessMedicineAddVo));
    }

    /**
     * 根据主键ID删除疾病-药物
     *
     * @param id 主键id
     * @return Result<Boolean> 返回结果(true/false)
     */
    @Operation(summary = "根据主键ID删除疾病-药物")
    @DeleteMapping("illnessMedicineDelete")
    public Result<Boolean> illnessMedicineDelete(@RequestParam Serializable id) {
        return Result.success(illnessMedicineService.removeById(id));
    }

    /**
    * 根据主键ID批量删除疾病-药物
    *
    * @param ids 主键id集合
    * @return Result<Boolean> 返回结果(true/false)
    */
    @Operation(summary = "根据主键ID批量删除疾病-药物")
    @DeleteMapping("illnessMedicineListDelete")
    public Result<Boolean> illnessMedicineListDelete(@RequestParam List<Serializable> ids) {
        return Result.success(illnessMedicineService.removeByIds(ids));
        }

    /**
     * 根据主键ID修改疾病-药物
     *
     * @param illnessMedicineUpdateVo 修改实体
     * @return Result<Boolean> 返回结果(true/false)
     */
    @Operation(summary = "根据主键ID修改疾病-药物")
    @PutMapping("illnessMedicineUpdate")
    public Result<Boolean> illnessMedicineUpdate(@RequestBody IllnessMedicineUpdateVo illnessMedicineUpdateVo) {
        return Result.success(illnessMedicineService.illnessMedicineUpdate(illnessMedicineUpdateVo));
    }

    /**
     * 根据主键ID查询疾病-药物
     *
     * @param id 主键id
     * @return Result<IllnessMedicine> 返回疾病-药物
     */
    @Operation(summary = "根据主键ID查询疾病-药物")
    @GetMapping("/getInfo")
    public Result<IllnessMedicine> illnessMedicineUpdate(@RequestParam Serializable id) {
        return Result.success(illnessMedicineService.getById(id));
    }

}
