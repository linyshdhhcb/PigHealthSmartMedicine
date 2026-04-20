package com.linyi.phsm.domain.rag.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageviewStatisticsVO {

    private Long totalPageviews;
    private Long todayPageviews;
    private List<HotIllnessVO> topIllnesses;
}
