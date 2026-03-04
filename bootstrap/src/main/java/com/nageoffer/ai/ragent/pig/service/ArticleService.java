package com.nageoffer.ai.ragent.pig.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nageoffer.ai.ragent.pig.controller.vo.ArticleVO;

/**
 * 文章信息服务接口
 */
public interface ArticleService {

    /**
     * 分页查询文章列表
     */
    Page<ArticleVO> listArticles(String category, String status, Integer pageNum, Integer pageSize);

    /**
     * 根据ID查询文章详情
     */
    ArticleVO getArticleById(Long id);

    /**
     * 创建文章
     */
    Long createArticle(ArticleVO articleVO);

    /**
     * 更新文章
     */
    void updateArticle(ArticleVO articleVO);

    /**
     * 删除文章
     */
    void deleteArticle(Long id);

    /**
     * 发布文章
     */
    void publishArticle(Long id);

    /**
     * 增加浏览次数
     */
    void incrementViewCount(Long id);

    /**
     * 点赞文章
     */
    void likeArticle(Long id);
}
