package com.linyi.pig.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linyi.pig.common.model.PageResult;
import com.linyi.pig.entity.Illness;
import com.linyi.pig.entity.vo.illness.IllnessAddVo;
import com.linyi.pig.entity.vo.illness.IllnessQueryVo;
import com.linyi.pig.entity.vo.illness.IllnessUpdateVo;
import com.linyi.pig.mapper.IllnessMapper;
import com.linyi.pig.service.IllnessService;
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
* @ClassName: IllnessServiceImpl
* @Version: 1.0
* @Description: 疾病 服务实现层
*/
@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
@SuppressWarnings({"unchecked", "rawtypes"})
public class IllnessServiceImpl extends ServiceImpl<IllnessMapper, Illness> implements IllnessService {

    @Autowired
    private IllnessMapper illnessMapper;

    @Override
    public PageResult<Illness> illnessPage(IllnessQueryVo illnessQueryVo) {
        LambdaQueryWrapper<Illness> queryWrapper = new LambdaQueryWrapper<>();
        //TODO 需要补充条件查询

        //分页数据
        Page<Illness> page = new Page<>(illnessQueryVo.getPageNum(),illnessQueryVo.getPageSize());
        //查询数据
        Page<Illness> pageNew = illnessMapper.selectPage(page, queryWrapper);
        //返回分页数据
        return new PageResult<>(pageNew.getRecords(), pageNew.getTotal(), pageNew.getPages(), illnessQueryVo.getPageNum(), illnessQueryVo.getPageSize());
    }

    @Override
    public Boolean illnessAdd(IllnessAddVo illnessAddVo){
        //创建实体对象
        Illness illness = new Illness();
        //复制属性
        BeanUtils.copyProperties(illnessAddVo, illness);
        //插入数据
        return illnessMapper.insert(illness) > 0 ? true : false;
    }

    @Override
    public Boolean illnessUpdate(IllnessUpdateVo illnessUpdateVo){
        //根据ID查询数据
        Illness byId=this.getById(illnessUpdateVo.getId());
        //判断数据是否存在
        if(Optional.ofNullable(byId).isEmpty()){
            log.error("数据不存在");
            return false;
        }
        //复制属性
        BeanUtils.copyProperties(illnessUpdateVo, byId);
        //修改数据
        return illnessMapper.updateById(byId) > 0 ? true : false;
    }
}
