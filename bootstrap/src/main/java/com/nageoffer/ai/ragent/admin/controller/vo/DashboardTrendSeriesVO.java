

package com.nageoffer.ai.ragent.admin.controller.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DashboardTrendSeriesVO {

    private String name;

    private List<DashboardTrendPointVO> data;
}
