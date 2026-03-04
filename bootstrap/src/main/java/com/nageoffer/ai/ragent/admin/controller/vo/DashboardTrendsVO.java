

package com.nageoffer.ai.ragent.admin.controller.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DashboardTrendsVO {

    private String metric;

    private String window;

    private String granularity;

    private List<DashboardTrendSeriesVO> series;
}
