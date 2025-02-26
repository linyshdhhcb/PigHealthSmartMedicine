package com.linyi.pig.controller;

import com.linyi.pig.common.model.PageResult;
import com.linyi.pig.common.model.Result;
import com.linyi.pig.entity.Articles;
import com.linyi.pig.entity.vo.articles.ArticlesAddVo;
import com.linyi.pig.entity.vo.articles.ArticlesQueryVo;
import com.linyi.pig.entity.vo.articles.ArticlesUpdateVo;
import com.linyi.pig.service.ArticlesService;
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
* @ClassName: ArticlesController
* @Version: 1.0
* @Description: 文章 控制层
*/

@Tag(name = "文章管理模块")
@Slf4j
@Validated
@RestController
@RequestMapping("/articles")
@SuppressWarnings({"unchecked", "rawtypes"})
public class ArticlesController{

    @Autowired
    private ArticlesService articlesService;

    /**
     * 分页查询文章
     *
     * @param articlesQueryVo 分页查询实体
     * @return Result<PageResult<Articles>> 返回分页数据
     */
    @Operation(summary = "分页查询文章")
    @PostMapping("/articlesPage")
    public Result<PageResult<Articles>> articlesPage(@RequestBody ArticlesQueryVo articlesQueryVo) {
        return Result.success(articlesService.articlesPage(articlesQueryVo));
    }

    /**
     * 新增文章
     *
     * @param articlesAddVo 新增实体
     * @return Result<Boolean> 返回结果(true/false)
     */
    @Operation(summary = "新增文章")
    @PostMapping("/articlesAdd")
    public Result<Boolean> articlesAdd(@RequestBody ArticlesAddVo articlesAddVo) {
        return Result.success(articlesService.articlesAdd(articlesAddVo));
    }

    /**
     * 根据主键ID删除文章
     *
     * @param id 主键id
     * @return Result<Boolean> 返回结果(true/false)
     */
    @Operation(summary = "根据主键ID删除文章")
    @DeleteMapping("articlesDelete")
    public Result<Boolean> articlesDelete(@RequestParam Serializable id) {
        return Result.success(articlesService.removeById(id));
    }

    /**
    * 根据主键ID批量删除文章
    *
    * @param ids 主键id集合
    * @return Result<Boolean> 返回结果(true/false)
    */
    @Operation(summary = "根据主键ID批量删除文章")
    @DeleteMapping("articlesListDelete")
    public Result<Boolean> articlesListDelete(@RequestParam List<Serializable> ids) {
        return Result.success(articlesService.removeByIds(ids));
        }

    /**
     * 根据主键ID修改文章
     *
     * @param articlesUpdateVo 修改实体
     * @return Result<Boolean> 返回结果(true/false)
     */
    @Operation(summary = "根据主键ID修改文章")
    @PutMapping("articlesUpdate")
    public Result<Boolean> articlesUpdate(@RequestBody ArticlesUpdateVo articlesUpdateVo) {
        return Result.success(articlesService.articlesUpdate(articlesUpdateVo));
    }

    /**
     * 根据主键ID查询文章
     *
     * @param id 主键id
     * @return Result<Articles> 返回文章
     */
    @Operation(summary = "根据主键ID查询文章")
    @GetMapping("/getInfo")
    public Result<Articles> articlesUpdate(@RequestParam Serializable id) {
        return Result.success(articlesService.getById(id));
    }

}
