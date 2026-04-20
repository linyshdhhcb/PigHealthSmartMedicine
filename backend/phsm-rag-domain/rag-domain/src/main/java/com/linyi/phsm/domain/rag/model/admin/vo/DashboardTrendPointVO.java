package com.linyi.phsm.domain.rag.model.admin.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardTrendPointVO {

    private Long ts;

    private Double value;
}
