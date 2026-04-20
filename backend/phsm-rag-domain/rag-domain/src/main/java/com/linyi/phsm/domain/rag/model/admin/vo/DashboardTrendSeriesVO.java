package com.linyi.phsm.domain.rag.model.admin.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DashboardTrendSeriesVO {

    private String name;

    private List<DashboardTrendPointVO> data;
}
