

package com.nageoffer.ai.ragent.admin.controller.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardOverviewKpiVO {

    private Long value;

    private Long delta;

    private Double deltaPct;
}
