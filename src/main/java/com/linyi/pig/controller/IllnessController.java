package com.linyi.pig.controller;

import com.linyi.pig.common.model.PageResult;
import com.linyi.pig.common.model.Result;
import com.linyi.pig.entity.Illness;
import com.linyi.pig.entity.vo.illness.IllnessAddVo;
import com.linyi.pig.entity.vo.illness.IllnessQueryVo;
import com.linyi.pig.entity.vo.illness.IllnessUpdateVo;
import com.linyi.pig.service.IllnessService;
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
* @ClassName: IllnessController
* @Version: 1.0
* @Description: 疾病 控制层
*/

@Tag(name = "疾病管理模块")
@Slf4j
@Validated
@RestController
@RequestMapping("/illness")
@SuppressWarnings({"unchecked", "rawtypes"})
public class IllnessController{

    @Autowired
    private IllnessService illnessService;

    /**
     * 分页查询疾病
     *
     * @param illnessQueryVo 分页查询实体
     * @return Result<PageResult<Illness>> 返回分页数据
     */
    @Operation(summary = "分页查询疾病")
    @PostMapping("/illnessPage")
    public Result<PageResult<Illness>> illnessPage(@RequestBody IllnessQueryVo illnessQueryVo) {
        return Result.success(illnessService.illnessPage(illnessQueryVo));
    }

    /**
     * 新增疾病
     *
     * @param illnessAddVo 新增实体
     * @return Result<Boolean> 返回结果(true/false)
     */
    @Operation(summary = "新增疾病")
    @PostMapping("/illnessAdd")
    public Result<Boolean> illnessAdd(@RequestBody IllnessAddVo illnessAddVo) {
        return Result.success(illnessService.illnessAdd(illnessAddVo));
    }

    /**
     * 根据主键ID删除疾病
     *
     * @param id 主键id
     * @return Result<Boolean> 返回结果(true/false)
     */
    @Operation(summary = "根据主键ID删除疾病")
    @DeleteMapping("illnessDelete")
    public Result<Boolean> illnessDelete(@RequestParam Serializable id) {
        return Result.success(illnessService.removeById(id));
    }

    /**
    * 根据主键ID批量删除疾病
    *
    * @param ids 主键id集合
    * @return Result<Boolean> 返回结果(true/false)
    */
    @Operation(summary = "根据主键ID批量删除疾病")
    @DeleteMapping("illnessListDelete")
    public Result<Boolean> illnessListDelete(@RequestParam List<Serializable> ids) {
        return Result.success(illnessService.removeByIds(ids));
        }

    /**
     * 根据主键ID修改疾病
     *
     * @param illnessUpdateVo 修改实体
     * @return Result<Boolean> 返回结果(true/false)
     */
    @Operation(summary = "根据主键ID修改疾病")
    @PutMapping("illnessUpdate")
    public Result<Boolean> illnessUpdate(@RequestBody IllnessUpdateVo illnessUpdateVo) {
        return Result.success(illnessService.illnessUpdate(illnessUpdateVo));
    }

    /**
     * 根据主键ID查询疾病
     *
     * @param id 主键id
     * @return Result<Illness> 返回疾病
     */
    @Operation(summary = "根据主键ID查询疾病")
    @GetMapping("/getInfo")
    public Result<Illness> illnessUpdate(@RequestParam Serializable id) {
        return Result.success(illnessService.getById(id));
    }

}
