package com.linyi.pig.controller;

import com.linyi.pig.common.model.PageResult;
import com.linyi.pig.common.model.Result;
import com.linyi.pig.entity.Feedback;
import com.linyi.pig.entity.vo.feedback.FeedbackAddVo;
import com.linyi.pig.entity.vo.feedback.FeedbackQueryVo;
import com.linyi.pig.entity.vo.feedback.FeedbackUpdateVo;
import com.linyi.pig.service.FeedbackService;
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
* @ClassName: FeedbackController
* @Version: 1.0
* @Description: 反馈 控制层
*/

@Tag(name = "反馈管理模块")
@Slf4j
@Validated
@RestController
@RequestMapping("/feedback")
@SuppressWarnings({"unchecked", "rawtypes"})
public class FeedbackController{

    @Autowired
    private FeedbackService feedbackService;

    /**
     * 分页查询反馈
     *
     * @param feedbackQueryVo 分页查询实体
     * @return Result<PageResult<Feedback>> 返回分页数据
     */
    @Operation(summary = "分页查询反馈")
    @PostMapping("/feedbackPage")
    public Result<PageResult<Feedback>> feedbackPage(@RequestBody FeedbackQueryVo feedbackQueryVo) {
        return Result.success(feedbackService.feedbackPage(feedbackQueryVo));
    }

    /**
     * 新增反馈
     *
     * @param feedbackAddVo 新增实体
     * @return Result<Boolean> 返回结果(true/false)
     */
    @Operation(summary = "新增反馈")
    @PostMapping("/feedbackAdd")
    public Result<Boolean> feedbackAdd(@RequestBody FeedbackAddVo feedbackAddVo) {
        return Result.success(feedbackService.feedbackAdd(feedbackAddVo));
    }

    /**
     * 根据主键ID删除反馈
     *
     * @param id 主键id
     * @return Result<Boolean> 返回结果(true/false)
     */
    @Operation(summary = "根据主键ID删除反馈")
    @DeleteMapping("feedbackDelete")
    public Result<Boolean> feedbackDelete(@RequestParam Serializable id) {
        return Result.success(feedbackService.removeById(id));
    }

    /**
    * 根据主键ID批量删除反馈
    *
    * @param ids 主键id集合
    * @return Result<Boolean> 返回结果(true/false)
    */
    @Operation(summary = "根据主键ID批量删除反馈")
    @DeleteMapping("feedbackListDelete")
    public Result<Boolean> feedbackListDelete(@RequestParam List<Serializable> ids) {
        return Result.success(feedbackService.removeByIds(ids));
        }

    /**
     * 根据主键ID修改反馈
     *
     * @param feedbackUpdateVo 修改实体
     * @return Result<Boolean> 返回结果(true/false)
     */
    @Operation(summary = "根据主键ID修改反馈")
    @PutMapping("feedbackUpdate")
    public Result<Boolean> feedbackUpdate(@RequestBody FeedbackUpdateVo feedbackUpdateVo) {
        return Result.success(feedbackService.feedbackUpdate(feedbackUpdateVo));
    }

    /**
     * 根据主键ID查询反馈
     *
     * @param id 主键id
     * @return Result<Feedback> 返回反馈
     */
    @Operation(summary = "根据主键ID查询反馈")
    @GetMapping("/getInfo")
    public Result<Feedback> feedbackUpdate(@RequestParam Serializable id) {
        return Result.success(feedbackService.getById(id));
    }

}
