package com.linyi.phsm.interfaces.rest.article;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.linyi.phsm.application.rag.article.service.ArticleTypeService;
import com.linyi.phsm.domain.rag.model.request.ArticleTypeCreateRequest;
import com.linyi.phsm.domain.rag.model.request.ArticleTypePageRequest;
import com.linyi.phsm.domain.rag.model.request.ArticleTypeUpdateRequest;
import com.linyi.phsm.domain.rag.model.vo.ArticleTypeVO;
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
@RequestMapping("/article-types")
public class ArticleTypeController {

    private final ArticleTypeService articleTypeService;

    @PostMapping
    public Result<String> create(@Valid @RequestBody ArticleTypeCreateRequest requestParam) {
        StpUtil.checkRole("admin");
        return Results.success(articleTypeService.create(requestParam));
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable String id, @Valid @RequestBody ArticleTypeUpdateRequest requestParam) {
        StpUtil.checkRole("admin");
        articleTypeService.update(id, requestParam);
        return Results.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable String id) {
        StpUtil.checkRole("admin");
        articleTypeService.delete(id);
        return Results.success();
    }

    @GetMapping("/{id}")
    public Result<ArticleTypeVO> queryById(@PathVariable String id) {
        StpUtil.checkRole("admin");
        return Results.success(articleTypeService.queryById(id));
    }

    @GetMapping
    public Result<IPage<ArticleTypeVO>> pageQuery(ArticleTypePageRequest requestParam) {
        StpUtil.checkRole("admin");
        return Results.success(articleTypeService.pageQuery(requestParam));
    }
}
