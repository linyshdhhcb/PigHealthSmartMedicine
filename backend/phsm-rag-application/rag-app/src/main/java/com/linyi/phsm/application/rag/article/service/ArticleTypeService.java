package com.linyi.phsm.application.rag.article.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.linyi.phsm.domain.rag.model.request.ArticleTypeCreateRequest;
import com.linyi.phsm.domain.rag.model.request.ArticleTypePageRequest;
import com.linyi.phsm.domain.rag.model.request.ArticleTypeUpdateRequest;
import com.linyi.phsm.domain.rag.model.vo.ArticleTypeVO;

public interface ArticleTypeService {

    String create(ArticleTypeCreateRequest requestParam);

    void update(String id, ArticleTypeUpdateRequest requestParam);

    void delete(String id);

    ArticleTypeVO queryById(String id);

    IPage<ArticleTypeVO> pageQuery(ArticleTypePageRequest requestParam);
}
