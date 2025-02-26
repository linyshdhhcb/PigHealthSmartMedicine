package com.linyi.pig.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linyi.pig.common.model.PageResult;
import com.linyi.pig.entity.IllnessKind;
import com.linyi.pig.entity.vo.illnessKind.IllnessKindAddVo;
import com.linyi.pig.entity.vo.illnessKind.IllnessKindQueryVo;
import com.linyi.pig.entity.vo.illnessKind.IllnessKindUpdateVo;
import com.linyi.pig.mapper.IllnessKindMapper;
import com.linyi.pig.service.IllnessKindService;
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
* @ClassName: IllnessKindServiceImpl
* @Version: 1.0
* @Description: 疾病种类 服务实现层
*/
@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
@SuppressWarnings({"unchecked", "rawtypes"})
public class IllnessKindServiceImpl extends ServiceImpl<IllnessKindMapper, IllnessKind> implements IllnessKindService {

    @Autowired
    private IllnessKindMapper illnessKindMapper;

    @Override
    public PageResult<IllnessKind> illnessKindPage(IllnessKindQueryVo illnessKindQueryVo) {
        LambdaQueryWrapper<IllnessKind> queryWrapper = new LambdaQueryWrapper<>();
        //TODO 需要补充条件查询

        //分页数据
        Page<IllnessKind> page = new Page<>(illnessKindQueryVo.getPageNum(),illnessKindQueryVo.getPageSize());
        //查询数据
        Page<IllnessKind> pageNew = illnessKindMapper.selectPage(page, queryWrapper);
        //返回分页数据
        return new PageResult<>(pageNew.getRecords(), pageNew.getTotal(), pageNew.getPages(), illnessKindQueryVo.getPageNum(), illnessKindQueryVo.getPageSize());
    }

    @Override
    public Boolean illnessKindAdd(IllnessKindAddVo illnessKindAddVo){
        //创建实体对象
        IllnessKind illnessKind = new IllnessKind();
        //复制属性
        BeanUtils.copyProperties(illnessKindAddVo, illnessKind);
        //插入数据
        return illnessKindMapper.insert(illnessKind) > 0 ? true : false;
    }

    @Override
    public Boolean illnessKindUpdate(IllnessKindUpdateVo illnessKindUpdateVo){
        //根据ID查询数据
        IllnessKind byId=this.getById(illnessKindUpdateVo.getId());
        //判断数据是否存在
        if(Optional.ofNullable(byId).isEmpty()){
            log.error("数据不存在");
            return false;
        }
        //复制属性
        BeanUtils.copyProperties(illnessKindUpdateVo, byId);
        //修改数据
        return illnessKindMapper.updateById(byId) > 0 ? true : false;
    }
}
