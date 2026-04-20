package com.linyi.phsm.interfaces.rest.article;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.linyi.phsm.application.rag.article.service.ArticleService;
import com.linyi.phsm.domain.rag.model.request.ArticleCreateRequest;
import com.linyi.phsm.domain.rag.model.request.ArticlePageRequest;
import com.linyi.phsm.domain.rag.model.request.ArticleUpdateRequest;
import com.linyi.phsm.domain.rag.model.vo.ArticleVO;
import com.linyi.phsm.framework.convention.Result;
import com.linyi.phsm.framework.web.Results;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping
    public Result<String> create(@Valid @RequestBody ArticleCreateRequest requestParam) {
        StpUtil.checkRole("admin");
        return Results.success(articleService.create(requestParam));
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable String id, @Valid @RequestBody ArticleUpdateRequest requestParam) {
        StpUtil.checkRole("admin");
        articleService.update(id, requestParam);
        return Results.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable String id) {
        StpUtil.checkRole("admin");
        articleService.delete(id);
        return Results.success();
    }

    @GetMapping("/{id}")
    public Result<ArticleVO> queryById(@PathVariable String id) {
        StpUtil.checkRole("admin");
        return Results.success(articleService.queryById(id));
    }

    @GetMapping
    public Result<IPage<ArticleVO>> pageQuery(ArticlePageRequest requestParam) {
        StpUtil.checkRole("admin");
        return Results.success(articleService.pageQuery(requestParam));
    }
}
