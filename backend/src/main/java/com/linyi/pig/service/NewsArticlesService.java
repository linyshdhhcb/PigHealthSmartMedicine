package com.linyi.pig.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linyi.pig.common.model.PageResult;
import com.linyi.pig.entity.NewsArticles;
import com.linyi.pig.entity.vo.newsArticles.NewsArticlesAddVo;
import com.linyi.pig.entity.vo.newsArticles.NewsArticlesQueryVo;
import com.linyi.pig.entity.vo.newsArticles.NewsArticlesUpdateVo;

/**
* @Author: linyi
* @Date: 2025-02-26 08:42:14
* @ClassName: NewsArticlesService
* @Version: 1.0
* @Description: 新闻资讯 服务层
*/
public interface NewsArticlesService extends IService<NewsArticles> {
    /**
     * 分页查询
     *
     * @param newsArticlesQueryVo 分页查询实体
     * @return PageResult<NewsArticles>
     */
    PageResult<NewsArticles> newsArticlesPage(NewsArticlesQueryVo newsArticlesQueryVo);

    /**
     * 新增
     *
     * @param newsArticlesAddVo 新增实体
     * @return Boolean
     */
    Boolean newsArticlesAdd(NewsArticlesAddVo newsArticlesAddVo);

    /**
     * 修改
     *
     * @param newsArticlesUpdateVo 修改实体
     * @return Boolean
     */
    Boolean newsArticlesUpdate(NewsArticlesUpdateVo newsArticlesUpdateVo);
}
