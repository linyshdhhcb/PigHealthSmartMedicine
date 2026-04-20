package com.linyi.phsm.application.rag.article.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.linyi.phsm.domain.rag.model.request.ArticleCreateRequest;
import com.linyi.phsm.domain.rag.model.request.ArticlePageRequest;
import com.linyi.phsm.domain.rag.model.request.ArticleUpdateRequest;
import com.linyi.phsm.domain.rag.model.vo.ArticleVO;

public interface ArticleService {

    String create(ArticleCreateRequest requestParam);

    void update(String id, ArticleUpdateRequest requestParam);

    void delete(String id);

    ArticleVO queryById(String id);

    IPage<ArticleVO> pageQuery(ArticlePageRequest requestParam);
}
