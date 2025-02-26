package com.linyi.pig.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linyi.pig.common.model.PageResult;
import com.linyi.pig.entity.Articles;
import com.linyi.pig.entity.vo.articles.ArticlesAddVo;
import com.linyi.pig.entity.vo.articles.ArticlesQueryVo;
import com.linyi.pig.entity.vo.articles.ArticlesUpdateVo;

/**
* @Author: linyi
* @Date: 2025-02-26 08:42:14
* @ClassName: ArticlesService
* @Version: 1.0
* @Description: 文章 服务层
*/
public interface ArticlesService extends IService<Articles> {
    /**
     * 分页查询
     *
     * @param articlesQueryVo 分页查询实体
     * @return PageResult<Articles>
     */
    PageResult<Articles> articlesPage(ArticlesQueryVo articlesQueryVo);

    /**
     * 新增
     *
     * @param articlesAddVo 新增实体
     * @return Boolean
     */
    Boolean articlesAdd(ArticlesAddVo articlesAddVo);

    /**
     * 修改
     *
     * @param articlesUpdateVo 修改实体
     * @return Boolean
     */
    Boolean articlesUpdate(ArticlesUpdateVo articlesUpdateVo);
}
