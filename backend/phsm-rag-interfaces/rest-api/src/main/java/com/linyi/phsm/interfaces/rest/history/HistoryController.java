package com.linyi.phsm.interfaces.rest.history;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.linyi.phsm.application.rag.history.service.HistoryService;
import com.linyi.phsm.domain.rag.model.request.HistoryCreateRequest;
import com.linyi.phsm.domain.rag.model.request.HistoryPageRequest;
import com.linyi.phsm.domain.rag.model.vo.HistoryVO;
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
@RequestMapping("/histories")
public class HistoryController {

    private final HistoryService historyService;

    @PostMapping("/record")
    public Result<Void> record(@Valid @RequestBody HistoryCreateRequest requestParam) {
        StpUtil.checkLogin();
        historyService.record(requestParam);
        return Results.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable String id) {
        StpUtil.checkLogin();
        historyService.delete(id);
        return Results.success();
    }

    @DeleteMapping("/clear")
    public Result<Void> clear() {
        StpUtil.checkLogin();
        historyService.clearMine();
        return Results.success();
    }

    @GetMapping("/{id}")
    public Result<HistoryVO> queryById(@PathVariable String id) {
        StpUtil.checkLogin();
        return Results.success(historyService.queryById(id));
    }

    @GetMapping
    public Result<IPage<HistoryVO>> pageQuery(HistoryPageRequest requestParam) {
        StpUtil.checkLogin();
        return Results.success(historyService.pageQuery(requestParam));
    }
}
