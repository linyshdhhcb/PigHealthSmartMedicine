package com.linyi.pig.controller;

import com.linyi.pig.common.model.Result;
import com.linyi.pig.entity.ConversationSession;
import com.linyi.pig.service.ConversationSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "会话主表管理")
@RestController
@RequestMapping("/conversation/session")
public class ConversationSessionController {

    @Autowired
    private ConversationSessionService conversationSessionService;

    @Operation(summary = "获取当前用户会话列表")
    @GetMapping("/list")
    public Result<List<ConversationSession>> list() {
        return Result.success(conversationSessionService.listMySessions());
    }

    @Operation(summary = "创建会话")
    @PostMapping("/create")
    public Result<ConversationSession> create(@RequestParam(required = false) String title,
            @RequestParam(required = false) String modelName) {
        return Result.success(conversationSessionService.createSession(title, modelName));
    }

    @Operation(summary = "关闭会话")
    @PostMapping("/close")
    public Result<Boolean> close(@RequestParam Long sessionId) {
        return Result.success(conversationSessionService.closeSession(sessionId));
    }
}
