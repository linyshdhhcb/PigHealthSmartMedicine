package com.linyi.phsm.interfaces.rest.feedback;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.linyi.phsm.application.rag.feedback.service.FeedbackService;
import com.linyi.phsm.domain.rag.model.request.FeedbackCreateRequest;
import com.linyi.phsm.domain.rag.model.request.FeedbackPageRequest;
import com.linyi.phsm.domain.rag.model.vo.FeedbackVO;
import com.linyi.phsm.framework.convention.Result;
import com.linyi.phsm.framework.web.Results;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feedbacks")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    public Result<String> submit(@Valid @RequestBody FeedbackCreateRequest requestParam) {
        StpUtil.checkLogin();
        return Results.success(feedbackService.submit(requestParam));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable String id) {
        StpUtil.checkRole("admin");
        feedbackService.delete(id);
        return Results.success();
    }

    @GetMapping("/{id}")
    public Result<FeedbackVO> queryById(@PathVariable String id) {
        StpUtil.checkRole("admin");
        return Results.success(feedbackService.queryById(id));
    }

    @GetMapping
    public Result<IPage<FeedbackVO>> pageQuery(FeedbackPageRequest requestParam) {
        StpUtil.checkRole("admin");
        return Results.success(feedbackService.pageQuery(requestParam));
    }
}
