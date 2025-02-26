package com.linyi.pig.controller;

import com.linyi.pig.common.model.PageResult;
import com.linyi.pig.common.model.Result;
import com.linyi.pig.entity.ArticleTypes;
import com.linyi.pig.entity.vo.articleTypes.ArticleTypesAddVo;
import com.linyi.pig.entity.vo.articleTypes.ArticleTypesQueryVo;
import com.linyi.pig.entity.vo.articleTypes.ArticleTypesUpdateVo;
import com.linyi.pig.service.ArticleTypesService;
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
* @ClassName: ArticleTypesController
* @Version: 1.0
* @Description: 文章类型 控制层
*/

@Tag(name = "文章类型管理模块")
@Slf4j
@Validated
@RestController
@RequestMapping("/articleTypes")
@SuppressWarnings({"unchecked", "rawtypes"})
public class ArticleTypesController{

    @Autowired
    private ArticleTypesService articleTypesService;

    /**
     * 分页查询文章类型
     *
     * @param articleTypesQueryVo 分页查询实体
     * @return Result<PageResult<ArticleTypes>> 返回分页数据
     */
    @Operation(summary = "分页查询文章类型")
    @PostMapping("/articleTypesPage")
    public Result<PageResult<ArticleTypes>> articleTypesPage(@RequestBody ArticleTypesQueryVo articleTypesQueryVo) {
        return Result.success(articleTypesService.articleTypesPage(articleTypesQueryVo));
    }

    /**
     * 新增文章类型
     *
     * @param articleTypesAddVo 新增实体
     * @return Result<Boolean> 返回结果(true/false)
     */
    @Operation(summary = "新增文章类型")
    @PostMapping("/articleTypesAdd")
    public Result<Boolean> articleTypesAdd(@RequestBody ArticleTypesAddVo articleTypesAddVo) {
        return Result.success(articleTypesService.articleTypesAdd(articleTypesAddVo));
    }

    /**
     * 根据主键ID删除文章类型
     *
     * @param id 主键id
     * @return Result<Boolean> 返回结果(true/false)
     */
    @Operation(summary = "根据主键ID删除文章类型")
    @DeleteMapping("articleTypesDelete")
    public Result<Boolean> articleTypesDelete(@RequestParam Serializable id) {
        return Result.success(articleTypesService.removeById(id));
    }

    /**
    * 根据主键ID批量删除文章类型
    *
    * @param ids 主键id集合
    * @return Result<Boolean> 返回结果(true/false)
    */
    @Operation(summary = "根据主键ID批量删除文章类型")
    @DeleteMapping("articleTypesListDelete")
    public Result<Boolean> articleTypesListDelete(@RequestParam List<Serializable> ids) {
        return Result.success(articleTypesService.removeByIds(ids));
        }

    /**
     * 根据主键ID修改文章类型
     *
     * @param articleTypesUpdateVo 修改实体
     * @return Result<Boolean> 返回结果(true/false)
     */
    @Operation(summary = "根据主键ID修改文章类型")
    @PutMapping("articleTypesUpdate")
    public Result<Boolean> articleTypesUpdate(@RequestBody ArticleTypesUpdateVo articleTypesUpdateVo) {
        return Result.success(articleTypesService.articleTypesUpdate(articleTypesUpdateVo));
    }

    /**
     * 根据主键ID查询文章类型
     *
     * @param id 主键id
     * @return Result<ArticleTypes> 返回文章类型
     */
    @Operation(summary = "根据主键ID查询文章类型")
    @GetMapping("/getInfo")
    public Result<ArticleTypes> articleTypesUpdate(@RequestParam Serializable id) {
        return Result.success(articleTypesService.getById(id));
    }

}
