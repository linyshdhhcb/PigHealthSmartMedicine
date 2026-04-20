package com.linyi.phsm.interfaces.rest.pageview;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.linyi.phsm.application.rag.pageview.service.PageviewService;
import com.linyi.phsm.domain.rag.model.request.PageviewPageRequest;
import com.linyi.phsm.domain.rag.model.vo.HotIllnessVO;
import com.linyi.phsm.domain.rag.model.vo.PageviewStatisticsVO;
import com.linyi.phsm.domain.rag.model.vo.PageviewVO;
import com.linyi.phsm.framework.convention.Result;
import com.linyi.phsm.framework.web.Results;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pageviews")
public class PageviewController {

    private final PageviewService pageviewService;

    @PostMapping("/{illnessId}/increment")
    public Result<Void> increment(@PathVariable String illnessId) {
        pageviewService.increment(illnessId);
        return Results.success();
    }

    @GetMapping("/hot-illnesses")
    public Result<List<HotIllnessVO>> hotIllnesses(@RequestParam(defaultValue = "10") int limit) {
        StpUtil.checkRole("admin");
        return Results.success(pageviewService.hotIllnesses(limit));
    }

    @GetMapping("/statistics")
    public Result<PageviewStatisticsVO> statistics() {
        StpUtil.checkRole("admin");
        return Results.success(pageviewService.statistics());
    }

    @GetMapping("/{id}")
    public Result<PageviewVO> queryById(@PathVariable String id) {
        StpUtil.checkRole("admin");
        return Results.success(pageviewService.queryById(id));
    }

    @GetMapping
    public Result<IPage<PageviewVO>> pageQuery(PageviewPageRequest requestParam) {
        StpUtil.checkRole("admin");
        return Results.success(pageviewService.pageQuery(requestParam));
    }
}
