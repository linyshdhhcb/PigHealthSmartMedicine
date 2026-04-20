package com.linyi.phsm.domain.rag.model.admin.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardOverviewGroupVO {

    private DashboardOverviewKpiVO totalUsers;

    private DashboardOverviewKpiVO activeUsers;

    private DashboardOverviewKpiVO totalSessions;

    private DashboardOverviewKpiVO sessions24h;

    private DashboardOverviewKpiVO totalMessages;

    private DashboardOverviewKpiVO messages24h;
}
