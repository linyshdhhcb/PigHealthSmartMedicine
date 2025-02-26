package com.linyi.pig.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linyi.pig.common.model.PageResult;
import com.linyi.pig.entity.ArticleTypes;
import com.linyi.pig.entity.vo.articleTypes.ArticleTypesAddVo;
import com.linyi.pig.entity.vo.articleTypes.ArticleTypesQueryVo;
import com.linyi.pig.entity.vo.articleTypes.ArticleTypesUpdateVo;
import com.linyi.pig.mapper.ArticleTypesMapper;
import com.linyi.pig.service.ArticleTypesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
* @Author: linyi
* @Date: 2025-02-26 08:42:14
* @ClassName: ArticleTypesServiceImpl
* @Version: 1.0
* @Description: 文章类型 服务实现层
*/
@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
@SuppressWarnings({"unchecked", "rawtypes"})
public class ArticleTypesServiceImpl extends ServiceImpl<ArticleTypesMapper, ArticleTypes> implements ArticleTypesService {

    @Autowired
    private ArticleTypesMapper articleTypesMapper;

    @Override
    public PageResult<ArticleTypes> articleTypesPage(ArticleTypesQueryVo articleTypesQueryVo) {
        LambdaQueryWrapper<ArticleTypes> queryWrapper = new LambdaQueryWrapper<>();
        //TODO 需要补充条件查询

        //分页数据
        Page<ArticleTypes> page = new Page<>(articleTypesQueryVo.getPageNum(),articleTypesQueryVo.getPageSize());
        //查询数据
        Page<ArticleTypes> pageNew = articleTypesMapper.selectPage(page, queryWrapper);
        //返回分页数据
        return new PageResult<>(pageNew.getRecords(), pageNew.getTotal(), pageNew.getPages(), articleTypesQueryVo.getPageNum(), articleTypesQueryVo.getPageSize());
    }

    @Override
    public Boolean articleTypesAdd(ArticleTypesAddVo articleTypesAddVo){
        //创建实体对象
        ArticleTypes articleTypes = new ArticleTypes();
        //复制属性
        BeanUtils.copyProperties(articleTypesAddVo, articleTypes);
        //插入数据
        return articleTypesMapper.insert(articleTypes) > 0 ? true : false;
    }

    @Override
    public Boolean articleTypesUpdate(ArticleTypesUpdateVo articleTypesUpdateVo){
        //根据ID查询数据
        ArticleTypes byId=this.getById(articleTypesUpdateVo.getId());
        //判断数据是否存在
        if(Optional.ofNullable(byId).isEmpty()){
            log.error("数据不存在");
            return false;
        }
        //复制属性
        BeanUtils.copyProperties(articleTypesUpdateVo, byId);
        //修改数据
        return articleTypesMapper.updateById(byId) > 0 ? true : false;
    }
}
