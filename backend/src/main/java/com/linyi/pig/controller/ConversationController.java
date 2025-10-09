package com.linyi.pig.controller;

import com.linyi.pig.common.model.PageResult;
import com.linyi.pig.common.model.Result;
import com.linyi.pig.entity.Conversation;
import com.linyi.pig.entity.vo.conversation.ConversationAddVo;
import com.linyi.pig.entity.vo.conversation.ConversationQueryVo;
import com.linyi.pig.entity.vo.conversation.ConversationUpdateVo;
import com.linyi.pig.service.ConversationService;
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
* @Date: 2025-02-26 13:27:06
* @ClassName: ConversationController
* @Version: 1.0
* @Description: 对话 控制层
*/

@Tag(name = "对话管理模块")
@Slf4j
@Validated
@RestController
@RequestMapping("/conversation")
@SuppressWarnings({"unchecked", "rawtypes"})
public class ConversationController {

    @Autowired
    private ConversationService conversationService;



    /**
     * 分页查询对话
     *
     * @param conversationQueryVo 分页查询实体
     * @return Result<PageResult<Conversation>> 返回分页数据
     */
    @Operation(summary = "分页查询对话")
    @PostMapping("/conversationPage")
    public Result<PageResult<Conversation>> conversationPage(@RequestBody ConversationQueryVo conversationQueryVo) {
        return Result.success(conversationService.conversationPage(conversationQueryVo));
    }

    /**
     * 新增对话
     *
     * @param conversationAddVo 新增实体
     * @return Result<Boolean> 返回结果(true/false)
     */
    @Operation(summary = "新增对话")
    @PostMapping("/conversationAdd")
    public Result<Boolean> conversationAdd(@RequestBody ConversationAddVo conversationAddVo) {
        return Result.success(conversationService.conversationAdd(conversationAddVo));
    }

    /**
     * 根据主键ID删除对话
     *
     * @param id 主键id
     * @return Result<Boolean> 返回结果(true/false)
     */
    @Operation(summary = "根据主键ID删除对话")
    @DeleteMapping("conversationDelete")
    public Result<Boolean> conversationDelete(@RequestParam Serializable id) {
        return Result.success(conversationService.removeById(id));
    }

    /**
    * 根据主键ID批量删除对话
    *
    * @param ids 主键id集合
    * @return Result<Boolean> 返回结果(true/false)
    */
    @Operation(summary = "根据主键ID批量删除对话")
    @DeleteMapping("conversationListDelete")
    public Result<Boolean> conversationListDelete(@RequestParam List<Serializable> ids) {
        return Result.success(conversationService.removeByIds(ids));
        }

    /**
     * 根据主键ID修改对话
     *
     * @param conversationUpdateVo 修改实体
     * @return Result<Boolean> 返回结果(true/false)
     */
    @Operation(summary = "根据主键ID修改对话")
    @PutMapping("conversationUpdate")
    public Result<Boolean> conversationUpdate(@RequestBody ConversationUpdateVo conversationUpdateVo) {
        return Result.success(conversationService.conversationUpdate(conversationUpdateVo));
    }

    /**
     * 根据主键ID查询对话
     *
     * @param id 主键id
     * @return Result<Conversation> 返回对话
     */
    @Operation(summary = "根据主键ID查询对话")
    @GetMapping("/getInfo")
    public Result<Conversation> conversationUpdate(@RequestParam Serializable id) {
        return Result.success(conversationService.getById(id));
    }

    /**
     * 根据几次历史对话记录
     * @param num 次数
     * @return
     */
    @Operation(summary = "要根据几次历史对话记录")
    @GetMapping("/getHistory")
    public Result<List<Conversation>> getHistoryNum(@RequestParam Integer num) {
        return Result.success(conversationService.getHistoryNum(num));
    }

    /**
     * 通过ollama调用大模型
     * @param prompt
     * @return
     */
    @Operation(summary = "通过ollama调用大模型")
    @GetMapping("/getOllama")
    public Result<Conversation> getOllama(@RequestParam String prompt) {
       return Result.success(conversationService.getOllama(prompt));
    }

    /**
     * 通过API调用大模型
     * @param prompt
     * @return
     */
    @Operation(summary = "调用大模型API")
    @GetMapping("/getApiLLM")
    public Result<Conversation> getApiLLM(@RequestParam String prompt) {
        return Result.success(conversationService.getApiLLM(prompt));
    }



}
