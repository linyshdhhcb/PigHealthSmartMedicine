

package com.nageoffer.ai.ragent.admin.controller.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardPerformanceVO {

    private String window;

    private Long avgLatencyMs;

    private Long p95LatencyMs;

    private Double successRate;

    private Double errorRate;

    private Double noDocRate;

    private Double slowRate;
}
