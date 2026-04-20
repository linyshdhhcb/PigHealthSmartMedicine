package com.linyi.phsm.domain.rag.model.admin.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardOverviewVO {

    private String window;

    private String compareWindow;

    private Long updatedAt;

    private DashboardOverviewGroupVO kpis;
}
