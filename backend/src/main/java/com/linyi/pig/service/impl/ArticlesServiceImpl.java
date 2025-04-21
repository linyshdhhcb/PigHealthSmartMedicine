package com.linyi.pig.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linyi.pig.common.model.PageResult;
import com.linyi.pig.entity.Articles;
import com.linyi.pig.entity.vo.articles.ArticlesAddVo;
import com.linyi.pig.entity.vo.articles.ArticlesQueryVo;
import com.linyi.pig.entity.vo.articles.ArticlesUpdateVo;
import com.linyi.pig.mapper.ArticlesMapper;
import com.linyi.pig.service.ArticlesService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
* @Author: linyi
* @Date: 2025-02-26 08:42:14
* @ClassName: ArticlesServiceImpl
* @Version: 1.0
* @Description: 文章 服务实现层
*/
@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
@SuppressWarnings({"unchecked", "rawtypes"})
public class ArticlesServiceImpl extends ServiceImpl<ArticlesMapper, Articles> implements ArticlesService {

    @Autowired
    private ArticlesMapper articlesMapper;

    @Override
    public PageResult<Articles> articlesPage(ArticlesQueryVo articlesQueryVo) {
        LambdaQueryWrapper<Articles> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(articlesQueryVo.getTitle()),Articles::getTitle, articlesQueryVo.getTitle());
        queryWrapper.eq(StringUtils.isNotBlank(articlesQueryVo.getContent()),Articles::getContent, articlesQueryVo.getContent());
        queryWrapper.eq(StringUtils.isNotBlank(articlesQueryVo.getAuthor()),Articles::getAuthor, articlesQueryVo.getAuthor());
        queryWrapper.eq(Optional.ofNullable(articlesQueryVo.getTypeId()).isPresent(),Articles::getTypeId, articlesQueryVo.getTypeId());

        //分页数据
        Page<Articles> page = new Page<>(articlesQueryVo.getPageNum(),articlesQueryVo.getPageSize());
        //查询数据
        Page<Articles> pageNew = articlesMapper.selectPage(page, queryWrapper);
        //返回分页数据
        return new PageResult<>(pageNew.getRecords(), pageNew.getTotal(), pageNew.getPages(), articlesQueryVo.getPageNum(), articlesQueryVo.getPageSize());
    }

    @Override
    public Boolean articlesAdd(ArticlesAddVo articlesAddVo){
        //创建实体对象
        Articles articles = new Articles();
        //复制属性
        BeanUtils.copyProperties(articlesAddVo, articles);
        //插入数据
        return articlesMapper.insert(articles) > 0 ? true : false;
    }

    @Override
    public Boolean articlesUpdate(ArticlesUpdateVo articlesUpdateVo){
        //根据ID查询数据
        Articles byId=this.getById(articlesUpdateVo.getId());
        //判断数据是否存在
        if(Optional.ofNullable(byId).isEmpty()){
            log.error("数据不存在");
            return false;
        }
        //复制属性
        BeanUtils.copyProperties(articlesUpdateVo, byId);
        //修改数据
        return articlesMapper.updateById(byId) > 0 ? true : false;
    }
}
