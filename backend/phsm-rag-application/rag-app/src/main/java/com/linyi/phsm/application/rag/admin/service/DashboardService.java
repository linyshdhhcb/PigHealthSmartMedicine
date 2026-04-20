package com.linyi.phsm.application.rag.admin.service;

import com.linyi.phsm.domain.rag.model.admin.vo.DashboardOverviewVO;
import com.linyi.phsm.domain.rag.model.admin.vo.DashboardPerformanceVO;
import com.linyi.phsm.domain.rag.model.admin.vo.DashboardTrendsVO;

public interface DashboardService {

    DashboardOverviewVO loadOverview(String window);

    DashboardPerformanceVO loadPerformance(String window);

    DashboardTrendsVO loadTrends(String metric, String window, String granularity);
}
