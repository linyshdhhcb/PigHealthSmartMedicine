package com.linyi.pig.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linyi.pig.common.model.PageResult;
import com.linyi.pig.entity.Pageview;
import com.linyi.pig.entity.vo.pageview.PageviewAddVo;
import com.linyi.pig.entity.vo.pageview.PageviewQueryVo;
import com.linyi.pig.entity.vo.pageview.PageviewUpdateVo;
import com.linyi.pig.mapper.PageviewMapper;
import com.linyi.pig.service.PageviewService;
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
* @ClassName: PageviewServiceImpl
* @Version: 1.0
* @Description: 浏览量 服务实现层
*/
@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
@SuppressWarnings({"unchecked", "rawtypes"})
public class PageviewServiceImpl extends ServiceImpl<PageviewMapper, Pageview> implements PageviewService {

    @Autowired
    private PageviewMapper pageviewMapper;

    @Override
    public PageResult<Pageview> pageviewPage(PageviewQueryVo pageviewQueryVo) {
        LambdaQueryWrapper<Pageview> queryWrapper = new LambdaQueryWrapper<>();
        //TODO 需要补充条件查询

        //分页数据
        Page<Pageview> page = new Page<>(pageviewQueryVo.getPageNum(),pageviewQueryVo.getPageSize());
        //查询数据
        Page<Pageview> pageNew = pageviewMapper.selectPage(page, queryWrapper);
        //返回分页数据
        return new PageResult<>(pageNew.getRecords(), pageNew.getTotal(), pageNew.getPages(), pageviewQueryVo.getPageNum(), pageviewQueryVo.getPageSize());
    }

    @Override
    public Boolean pageviewAdd(PageviewAddVo pageviewAddVo){
        //创建实体对象
        Pageview pageview = new Pageview();
        //复制属性
        BeanUtils.copyProperties(pageviewAddVo, pageview);
        //插入数据
        return pageviewMapper.insert(pageview) > 0 ? true : false;
    }

    @Override
    public Boolean pageviewUpdate(PageviewUpdateVo pageviewUpdateVo){
        //根据ID查询数据
        Pageview byId=this.getById(pageviewUpdateVo.getId());
        //判断数据是否存在
        if(Optional.ofNullable(byId).isEmpty()){
            log.error("数据不存在");
            return false;
        }
        //复制属性
        BeanUtils.copyProperties(pageviewUpdateVo, byId);
        //修改数据
        return pageviewMapper.updateById(byId) > 0 ? true : false;
    }
}
