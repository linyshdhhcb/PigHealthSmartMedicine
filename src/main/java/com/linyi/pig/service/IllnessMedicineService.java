package com.linyi.pig.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linyi.pig.common.model.PageResult;
import com.linyi.pig.entity.IllnessMedicine;
import com.linyi.pig.entity.vo.illnessMedicine.IllnessMedicineAddVo;
import com.linyi.pig.entity.vo.illnessMedicine.IllnessMedicineQueryVo;
import com.linyi.pig.entity.vo.illnessMedicine.IllnessMedicineUpdateVo;

/**
* @Author: linyi
* @Date: 2025-02-25 17:38:38
* @ClassName: IllnessMedicineService
* @Version: 1.0
* @Description: 疾病-药物 服务层
*/
public interface IllnessMedicineService extends IService<IllnessMedicine> {
    /**
     * 分页查询
     *
     * @param illnessMedicineQueryVo 分页查询实体
     * @return PageResult<IllnessMedicine>
     */
    PageResult<IllnessMedicine> illnessMedicinePage(IllnessMedicineQueryVo illnessMedicineQueryVo);

    /**
     * 新增
     *
     * @param illnessMedicineAddVo 新增实体
     * @return Boolean
     */
    Boolean illnessMedicineAdd(IllnessMedicineAddVo illnessMedicineAddVo);

    /**
     * 修改
     *
     * @param illnessMedicineUpdateVo 修改实体
     * @return Boolean
     */
    Boolean illnessMedicineUpdate(IllnessMedicineUpdateVo illnessMedicineUpdateVo);
}
