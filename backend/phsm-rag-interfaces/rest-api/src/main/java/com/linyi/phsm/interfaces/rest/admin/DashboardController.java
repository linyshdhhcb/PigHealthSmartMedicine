package com.linyi.phsm.interfaces.rest.admin;

import com.linyi.phsm.domain.rag.model.admin.vo.DashboardOverviewVO;
import com.linyi.phsm.domain.rag.model.admin.vo.DashboardPerformanceVO;
import com.linyi.phsm.domain.rag.model.admin.vo.DashboardTrendsVO;
import com.linyi.phsm.application.rag.admin.service.DashboardService;
import com.linyi.phsm.framework.convention.Result;
import com.linyi.phsm.framework.web.Results;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/overview")
    public Result<DashboardOverviewVO> overview(@RequestParam(required = false) String window) {
        return Results.success(dashboardService.loadOverview(window));
    }

    @GetMapping("/performance")
    public Result<DashboardPerformanceVO> performance(@RequestParam(required = false) String window) {
        return Results.success(dashboardService.loadPerformance(window));
    }

    @GetMapping("/trends")
    public Result<DashboardTrendsVO> trends(@RequestParam String metric,
                                            @RequestParam(required = false) String window,
                                            @RequestParam(required = false) String granularity) {
        return Results.success(dashboardService.loadTrends(metric, window, granularity));
    }
}
