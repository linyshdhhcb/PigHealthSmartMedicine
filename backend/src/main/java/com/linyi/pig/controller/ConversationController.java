package com.linyi.pig.controller;

import com.linyi.pig.common.model.PageResult;
import com.linyi.pig.common.model.Result;
import com.linyi.pig.entity.Conversation;
import com.linyi.pig.entity.vo.conversation.ConversationAddVo;
import com.linyi.pig.entity.vo.conversation.ConversationQueryVo;
import com.linyi.pig.entity.vo.conversation.ConversationRagResultVo;
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

@Tag(name = "对话管理模块")
@Slf4j
@Validated
@RestController
@RequestMapping("/conversation")
public class ConversationController {

    @Autowired
    private ConversationService conversationService;

    @Operation(summary = "分页查询对话")
    @PostMapping("/conversationPage")
    public Result<PageResult<Conversation>> conversationPage(@RequestBody ConversationQueryVo conversationQueryVo) {
        return Result.success(conversationService.conversationPage(conversationQueryVo));
    }

    @Operation(summary = "新增对话")
    @PostMapping("/conversationAdd")
    public Result<Boolean> conversationAdd(@RequestBody ConversationAddVo conversationAddVo) {
        return Result.success(conversationService.conversationAdd(conversationAddVo));
    }

    @Operation(summary = "根据主键ID删除对话")
    @DeleteMapping("conversationDelete")
    public Result<Boolean> conversationDelete(@RequestParam Serializable id) {
        return Result.success(conversationService.removeById(id));
    }

    @Operation(summary = "根据主键ID批量删除对话")
    @DeleteMapping("conversationListDelete")
    public Result<Boolean> conversationListDelete(@RequestParam List<Serializable> ids) {
        return Result.success(conversationService.removeByIds(ids));
    }

    @Operation(summary = "根据主键ID修改对话")
    @PutMapping("conversationUpdate")
    public Result<Boolean> conversationUpdate(@RequestBody ConversationUpdateVo conversationUpdateVo) {
        return Result.success(conversationService.conversationUpdate(conversationUpdateVo));
    }

    @Operation(summary = "根据主键ID查询对话")
    @GetMapping("/getInfo")
    public Result<Conversation> conversationUpdate(@RequestParam Serializable id) {
        return Result.success(conversationService.getById(id));
    }

    @Operation(summary = "要根据几次历史对话记录")
    @GetMapping("/getHistory")
    public Result<List<Conversation>> getHistoryNum(@RequestParam Integer num) {
        return Result.success(conversationService.getHistoryNum(num));
    }

    @Operation(summary = "根据会话ID获取历史记录")
    @GetMapping("/listBySession")
    public Result<List<Conversation>> listBySession(@RequestParam Long sessionId) {
        return Result.success(conversationService.listBySessionId(sessionId));
    }

    @Operation(summary = "通过知识库问答链路获取回答")
    @GetMapping("/getOllama")
    public Result<ConversationRagResultVo> getOllama(@RequestParam String prompt,
            @RequestParam(required = false) Long kbId,
            @RequestParam(required = false) Long sessionId) {
        return Result.success(conversationService.getKnowledgeAnswerResult(prompt, kbId, sessionId));
    }

    @Operation(summary = "调用大模型API")
    @GetMapping("/getApiLLM")
    public Result<Conversation> getApiLLM(@RequestParam String prompt) {
        return Result.success(conversationService.getApiLLM(prompt));
    }
}
