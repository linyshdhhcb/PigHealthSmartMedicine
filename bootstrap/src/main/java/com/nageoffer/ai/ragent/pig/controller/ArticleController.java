package com.nageoffer.ai.ragent.pig.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nageoffer.ai.ragent.framework.convention.Result;
import com.nageoffer.ai.ragent.framework.web.Results;
import com.nageoffer.ai.ragent.pig.controller.vo.ArticleVO;
import com.nageoffer.ai.ragent.pig.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 文章信息Controller
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/article")
public class ArticleController {

    private final ArticleService articleService;

    /**
     * 分页查询文章列表
     */
    @GetMapping("/list")
    public Result<Page<ArticleVO>> listArticles(@RequestParam(required = false) String category,
                                                @RequestParam(required = false) String status,
                                                @RequestParam(defaultValue = "1") Integer pageNum,
                                                @RequestParam(defaultValue = "10") Integer pageSize) {
        return Results.success(articleService.listArticles(category, status, pageNum, pageSize));
    }

    /**
     * 根据ID查询文章详情
     */
    @GetMapping("/{id}")
    public Result<ArticleVO> getArticleById(@PathVariable Long id) {
        // 增加浏览次数
        articleService.incrementViewCount(id);
        return Results.success(articleService.getArticleById(id));
    }

    /**
     * 创建文章（需要兽医或管理员权限）
     */
    @PostMapping
    @SaCheckRole(value = {"admin", "veterinarian"}, mode = )
    public Result<Long> createArticle(@RequestBody ArticleVO articleVO) {
        return Results.success(articleService.createArticle(articleVO));
    }

    /**
     * 更新文章（需要兽医或管理员权限）
     */
    @PutMapping
    @SaCheckRole(value = {"admin", "veterinarian"}, mode = )
    public Result<Void> updateArticle(@RequestBody ArticleVO articleVO) {
        articleService.updateArticle(articleVO);
        return Results.success();
    }

    /**
     * 删除文章（需要管理员权限）
     */
    @DeleteMapping("/{id}")
    @SaCheckRole("admin")
    public Result<Void> deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return Results.success();
    }

    /**
     * 发布文章（需要兽医或管理员权限）
     */
    @PostMapping("/{id}/publish")
    @SaCheckRole(value = {"admin", "veterinarian"}, mode = )
    public Result<Void> publishArticle(@PathVariable Long id) {
        articleService.publishArticle(id);
        return Results.success();
    }

    /**
     * 点赞文章
     */
    @PostMapping("/{id}/like")
    public Result<Void> likeArticle(@PathVariable Long id) {
        articleService.likeArticle(id);
        return Results.success();
    }
}
