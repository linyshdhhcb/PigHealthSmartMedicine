package com.linyi.pig.controller;

import com.linyi.pig.common.model.PageResult;
import com.linyi.pig.common.model.Result;
import com.linyi.pig.entity.NewsArticles;
import com.linyi.pig.entity.vo.newsArticles.NewsArticlesAddVo;
import com.linyi.pig.entity.vo.newsArticles.NewsArticlesQueryVo;
import com.linyi.pig.entity.vo.newsArticles.NewsArticlesUpdateVo;
import com.linyi.pig.service.NewsArticlesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;


/**
* @Author: linyi
* @Date: 2025-02-26 08:42:14
* @ClassName: NewsArticlesController
* @Version: 1.0
* @Description: 新闻资讯 控制层
*/

@Tag(name = "新闻资讯管理模块")
@Slf4j
@Validated
@RestController
@RequestMapping("/newsArticles")
@SuppressWarnings({"unchecked", "rawtypes"})
public class NewsArticlesController{

    @Autowired
    private NewsArticlesService newsArticlesService;

    /**
     * 分页查询新闻资讯
     *
     * @param newsArticlesQueryVo 分页查询实体
     * @return Result<PageResult<NewsArticles>> 返回分页数据
     */
    @Operation(summary = "分页查询新闻资讯")
    @PostMapping("/newsArticlesPage")
    public Result<PageResult<NewsArticles>> newsArticlesPage(@RequestBody NewsArticlesQueryVo newsArticlesQueryVo) {
        return Result.success(newsArticlesService.newsArticlesPage(newsArticlesQueryVo));
    }

    /**
     * 新增新闻资讯
     *
     * @param newsArticlesAddVo 新增实体
     * @return Result<Boolean> 返回结果(true/false)
     */
    @Operation(summary = "新增新闻资讯")
    @PostMapping("/newsArticlesAdd")
    public Result<Boolean> newsArticlesAdd(@RequestBody NewsArticlesAddVo newsArticlesAddVo) {
        return Result.success(newsArticlesService.newsArticlesAdd(newsArticlesAddVo));
    }

    /**
     * 根据主键ID删除新闻资讯
     *
     * @param id 主键id
     * @return Result<Boolean> 返回结果(true/false)
     */
    @Operation(summary = "根据主键ID删除新闻资讯")
    @DeleteMapping("newsArticlesDelete")
    public Result<Boolean> newsArticlesDelete(@RequestParam Serializable id) {
        return Result.success(newsArticlesService.removeById(id));
    }

    /**
    * 根据主键ID批量删除新闻资讯
    *
    * @param ids 主键id集合
    * @return Result<Boolean> 返回结果(true/false)
    */
    @Operation(summary = "根据主键ID批量删除新闻资讯")
    @DeleteMapping("newsArticlesListDelete")
    public Result<Boolean> newsArticlesListDelete(@RequestParam List<Serializable> ids) {
        return Result.success(newsArticlesService.removeByIds(ids));
        }

    /**
     * 根据主键ID修改新闻资讯
     *
     * @param newsArticlesUpdateVo 修改实体
     * @return Result<Boolean> 返回结果(true/false)
     */
    @Operation(summary = "根据主键ID修改新闻资讯")
    @PutMapping("newsArticlesUpdate")
    public Result<Boolean> newsArticlesUpdate(@RequestBody NewsArticlesUpdateVo newsArticlesUpdateVo) {
        return Result.success(newsArticlesService.newsArticlesUpdate(newsArticlesUpdateVo));
    }

    /**
     * 根据主键ID查询新闻资讯
     *
     * @param id 主键id
     * @return Result<NewsArticles> 返回新闻资讯
     */
    @Operation(summary = "根据主键ID查询新闻资讯")
    @GetMapping("/getInfo")
    public Result<NewsArticles> newsArticlesUpdate(@RequestParam Serializable id) {
        return Result.success(newsArticlesService.getById(id));
    }

}
