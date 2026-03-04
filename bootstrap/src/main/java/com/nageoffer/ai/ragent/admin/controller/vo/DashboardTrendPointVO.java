

package com.nageoffer.ai.ragent.admin.controller.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardTrendPointVO {

    private Long ts;

    private Double value;
}
