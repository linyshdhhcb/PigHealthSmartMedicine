package com.linyi.pig.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linyi.pig.common.model.PageResult;
import com.linyi.pig.entity.IllnessMedicine;
import com.linyi.pig.entity.vo.illnessMedicine.IllnessMedicineAddVo;
import com.linyi.pig.entity.vo.illnessMedicine.IllnessMedicineQueryVo;
import com.linyi.pig.entity.vo.illnessMedicine.IllnessMedicineUpdateVo;
import com.linyi.pig.mapper.IllnessMedicineMapper;
import com.linyi.pig.service.IllnessMedicineService;
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
* @ClassName: IllnessMedicineServiceImpl
* @Version: 1.0
* @Description: 疾病-药物 服务实现层
*/
@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
@SuppressWarnings({"unchecked", "rawtypes"})
public class IllnessMedicineServiceImpl extends ServiceImpl<IllnessMedicineMapper, IllnessMedicine> implements IllnessMedicineService {

    @Autowired
    private IllnessMedicineMapper illnessMedicineMapper;

    @Override
    public PageResult<IllnessMedicine> illnessMedicinePage(IllnessMedicineQueryVo illnessMedicineQueryVo) {
        LambdaQueryWrapper<IllnessMedicine> queryWrapper = new LambdaQueryWrapper<>();
        //TODO 需要补充条件查询

        //分页数据
        Page<IllnessMedicine> page = new Page<>(illnessMedicineQueryVo.getPageNum(),illnessMedicineQueryVo.getPageSize());
        //查询数据
        Page<IllnessMedicine> pageNew = illnessMedicineMapper.selectPage(page, queryWrapper);
        //返回分页数据
        return new PageResult<>(pageNew.getRecords(), pageNew.getTotal(), pageNew.getPages(), illnessMedicineQueryVo.getPageNum(), illnessMedicineQueryVo.getPageSize());
    }

    @Override
    public Boolean illnessMedicineAdd(IllnessMedicineAddVo illnessMedicineAddVo){
        //创建实体对象
        IllnessMedicine illnessMedicine = new IllnessMedicine();
        //复制属性
        BeanUtils.copyProperties(illnessMedicineAddVo, illnessMedicine);
        //插入数据
        return illnessMedicineMapper.insert(illnessMedicine) > 0 ? true : false;
    }

    @Override
    public Boolean illnessMedicineUpdate(IllnessMedicineUpdateVo illnessMedicineUpdateVo){
        //根据ID查询数据
        IllnessMedicine byId=this.getById(illnessMedicineUpdateVo.getId());
        //判断数据是否存在
        if(Optional.ofNullable(byId).isEmpty()){
            log.error("数据不存在");
            return false;
        }
        //复制属性
        BeanUtils.copyProperties(illnessMedicineUpdateVo, byId);
        //修改数据
        return illnessMedicineMapper.updateById(byId) > 0 ? true : false;
    }
}
