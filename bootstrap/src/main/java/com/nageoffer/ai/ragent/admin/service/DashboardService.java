

package com.nageoffer.ai.ragent.admin.service;

import com.nageoffer.ai.ragent.admin.controller.vo.DashboardOverviewVO;
import com.nageoffer.ai.ragent.admin.controller.vo.DashboardPerformanceVO;
import com.nageoffer.ai.ragent.admin.controller.vo.DashboardTrendsVO;

public interface DashboardService {

    DashboardOverviewVO loadOverview(String window);

    DashboardPerformanceVO loadPerformance(String window);

    DashboardTrendsVO loadTrends(String metric, String window, String granularity);
}
