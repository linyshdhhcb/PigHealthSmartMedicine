package com.linyi.pig.controller;

import com.linyi.pig.common.model.PageResult;
import com.linyi.pig.common.model.Result;
import com.linyi.pig.entity.Medicine;
import com.linyi.pig.entity.vo.medicine.MedicineAddVo;
import com.linyi.pig.entity.vo.medicine.MedicineQueryVo;
import com.linyi.pig.entity.vo.medicine.MedicineUpdateVo;
import com.linyi.pig.service.MedicineService;
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
* @ClassName: MedicineController
* @Version: 1.0
* @Description: 药品 控制层
*/

@Tag(name = "药品管理模块")
@Slf4j
@Validated
@RestController
@RequestMapping("/medicine")
@SuppressWarnings({"unchecked", "rawtypes"})
public class MedicineController{

    @Autowired
    private MedicineService medicineService;

    /**
     * 分页查询药品
     *
     * @param medicineQueryVo 分页查询实体
     * @return Result<PageResult<Medicine>> 返回分页数据
     */
    @Operation(summary = "分页查询药品")
    @PostMapping("/medicinePage")
    public Result<PageResult<Medicine>> medicinePage(@RequestBody MedicineQueryVo medicineQueryVo) {
        return Result.success(medicineService.medicinePage(medicineQueryVo));
    }

    /**
     * 新增药品
     *
     * @param medicineAddVo 新增实体
     * @return Result<Boolean> 返回结果(true/false)
     */
    @Operation(summary = "新增药品")
    @PostMapping("/medicineAdd")
    public Result<Boolean> medicineAdd(@RequestBody MedicineAddVo medicineAddVo) {
        return Result.success(medicineService.medicineAdd(medicineAddVo));
    }

    /**
     * 根据主键ID删除药品
     *
     * @param id 主键id
     * @return Result<Boolean> 返回结果(true/false)
     */
    @Operation(summary = "根据主键ID删除药品")
    @DeleteMapping("medicineDelete")
    public Result<Boolean> medicineDelete(@RequestParam Serializable id) {
        return Result.success(medicineService.removeById(id));
    }

    /**
    * 根据主键ID批量删除药品
    *
    * @param ids 主键id集合
    * @return Result<Boolean> 返回结果(true/false)
    */
    @Operation(summary = "根据主键ID批量删除药品")
    @DeleteMapping("medicineListDelete")
    public Result<Boolean> medicineListDelete(@RequestParam List<Serializable> ids) {
        return Result.success(medicineService.removeByIds(ids));
        }

    /**
     * 根据主键ID修改药品
     *
     * @param medicineUpdateVo 修改实体
     * @return Result<Boolean> 返回结果(true/false)
     */
    @Operation(summary = "根据主键ID修改药品")
    @PutMapping("medicineUpdate")
    public Result<Boolean> medicineUpdate(@RequestBody MedicineUpdateVo medicineUpdateVo) {
        return Result.success(medicineService.medicineUpdate(medicineUpdateVo));
    }

    /**
     * 根据主键ID查询药品
     *
     * @param id 主键id
     * @return Result<Medicine> 返回药品
     */
    @Operation(summary = "根据主键ID查询药品")
    @GetMapping("/getInfo")
    public Result<Medicine> medicineUpdate(@RequestParam Serializable id) {
        return Result.success(medicineService.getById(id));
    }

}
