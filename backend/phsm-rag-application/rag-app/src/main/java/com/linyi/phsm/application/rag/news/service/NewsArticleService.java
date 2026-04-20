package com.linyi.phsm.application.rag.news.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.linyi.phsm.domain.rag.model.request.NewsArticleCreateRequest;
import com.linyi.phsm.domain.rag.model.request.NewsArticlePageRequest;
import com.linyi.phsm.domain.rag.model.request.NewsArticleUpdateRequest;
import com.linyi.phsm.domain.rag.model.vo.NewsArticleVO;

public interface NewsArticleService {

    String create(NewsArticleCreateRequest requestParam);

    void update(String id, NewsArticleUpdateRequest requestParam);

    void delete(String id);

    NewsArticleVO queryById(String id);

    IPage<NewsArticleVO> pageQuery(NewsArticlePageRequest requestParam);
}
