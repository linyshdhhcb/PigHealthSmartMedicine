package com.linyi.pig.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linyi.pig.common.model.PageResult;
import com.linyi.pig.entity.Medicine;
import com.linyi.pig.entity.vo.medicine.MedicineAddVo;
import com.linyi.pig.entity.vo.medicine.MedicineQueryVo;
import com.linyi.pig.entity.vo.medicine.MedicineUpdateVo;
import com.linyi.pig.mapper.MedicineMapper;
import com.linyi.pig.service.MedicineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
* @Author: linyi
* @Date: 2025-02-25 17:38:38
* @ClassName: MedicineServiceImpl
* @Version: 1.0
* @Description: 药品 服务实现层
*/
@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
@SuppressWarnings({"unchecked", "rawtypes"})
public class MedicineServiceImpl extends ServiceImpl<MedicineMapper, Medicine> implements MedicineService {

    @Autowired
    private MedicineMapper medicineMapper;

    @Override
    public PageResult<Medicine> medicinePage(MedicineQueryVo medicineQueryVo) {
        LambdaQueryWrapper<Medicine> queryWrapper = new LambdaQueryWrapper<>();
        //TODO 需要补充条件查询

        //分页数据
        Page<Medicine> page = new Page<>(medicineQueryVo.getPageNum(),medicineQueryVo.getPageSize());
        //查询数据
        Page<Medicine> pageNew = medicineMapper.selectPage(page, queryWrapper);
        //返回分页数据
        return new PageResult<>(pageNew.getRecords(), pageNew.getTotal(), pageNew.getPages(), medicineQueryVo.getPageNum(), medicineQueryVo.getPageSize());
    }

    @Override
    public Boolean medicineAdd(MedicineAddVo medicineAddVo){
        //创建实体对象
        Medicine medicine = new Medicine();
        //复制属性
        BeanUtils.copyProperties(medicineAddVo, medicine);
        //插入数据
        return medicineMapper.insert(medicine) > 0 ? true : false;
    }

    @Override
    public Boolean medicineUpdate(MedicineUpdateVo medicineUpdateVo){
        //根据ID查询数据
        Medicine byId=this.getById(medicineUpdateVo.getId());
        //判断数据是否存在
        if(Optional.ofNullable(byId).isEmpty()){
            log.error("数据不存在");
            return false;
        }
        //复制属性
        BeanUtils.copyProperties(medicineUpdateVo, byId);
        //修改数据
        return medicineMapper.updateById(byId) > 0 ? true : false;
    }
}
