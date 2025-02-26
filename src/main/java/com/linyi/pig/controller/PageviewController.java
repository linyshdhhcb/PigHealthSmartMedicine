package com.linyi.pig.controller;

import com.linyi.pig.common.model.PageResult;
import com.linyi.pig.common.model.Result;
import com.linyi.pig.entity.Pageview;
import com.linyi.pig.entity.vo.pageview.PageviewAddVo;
import com.linyi.pig.entity.vo.pageview.PageviewQueryVo;
import com.linyi.pig.entity.vo.pageview.PageviewUpdateVo;
import com.linyi.pig.service.PageviewService;
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
* @Date: 2025-02-25 17:38:38
* @ClassName: PageviewController
* @Version: 1.0
* @Description: 浏览量 控制层
*/

@Tag(name = "浏览量管理模块")
@Slf4j
@Validated
@RestController
@RequestMapping("/pageview")
@SuppressWarnings({"unchecked", "rawtypes"})
public class PageviewController{

    @Autowired
    private PageviewService pageviewService;

    /**
     * 分页查询浏览量
     *
     * @param pageviewQueryVo 分页查询实体
     * @return Result<PageResult<Pageview>> 返回分页数据
     */
    @Operation(summary = "分页查询浏览量")
    @PostMapping("/pageviewPage")
    public Result<PageResult<Pageview>> pageviewPage(@RequestBody PageviewQueryVo pageviewQueryVo) {
        return Result.success(pageviewService.pageviewPage(pageviewQueryVo));
    }

    /**
     * 新增浏览量
     *
     * @param pageviewAddVo 新增实体
     * @return Result<Boolean> 返回结果(true/false)
     */
    @Operation(summary = "新增浏览量")
    @PostMapping("/pageviewAdd")
    public Result<Boolean> pageviewAdd(@RequestBody PageviewAddVo pageviewAddVo) {
        return Result.success(pageviewService.pageviewAdd(pageviewAddVo));
    }

    /**
     * 根据主键ID删除浏览量
     *
     * @param id 主键id
     * @return Result<Boolean> 返回结果(true/false)
     */
    @Operation(summary = "根据主键ID删除浏览量")
    @DeleteMapping("pageviewDelete")
    public Result<Boolean> pageviewDelete(@RequestParam Serializable id) {
        return Result.success(pageviewService.removeById(id));
    }

    /**
    * 根据主键ID批量删除浏览量
    *
    * @param ids 主键id集合
    * @return Result<Boolean> 返回结果(true/false)
    */
    @Operation(summary = "根据主键ID批量删除浏览量")
    @DeleteMapping("pageviewListDelete")
    public Result<Boolean> pageviewListDelete(@RequestParam List<Serializable> ids) {
        return Result.success(pageviewService.removeByIds(ids));
        }

    /**
     * 根据主键ID修改浏览量
     *
     * @param pageviewUpdateVo 修改实体
     * @return Result<Boolean> 返回结果(true/false)
     */
    @Operation(summary = "根据主键ID修改浏览量")
    @PutMapping("pageviewUpdate")
    public Result<Boolean> pageviewUpdate(@RequestBody PageviewUpdateVo pageviewUpdateVo) {
        return Result.success(pageviewService.pageviewUpdate(pageviewUpdateVo));
    }

    /**
     * 根据主键ID查询浏览量
     *
     * @param id 主键id
     * @return Result<Pageview> 返回浏览量
     */
    @Operation(summary = "根据主键ID查询浏览量")
    @GetMapping("/getInfo")
    public Result<Pageview> pageviewUpdate(@RequestParam Serializable id) {
        return Result.success(pageviewService.getById(id));
    }

}
