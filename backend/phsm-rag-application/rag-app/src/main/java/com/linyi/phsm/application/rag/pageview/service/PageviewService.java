package com.linyi.phsm.application.rag.pageview.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.linyi.phsm.domain.rag.model.request.PageviewPageRequest;
import com.linyi.phsm.domain.rag.model.vo.HotIllnessVO;
import com.linyi.phsm.domain.rag.model.vo.PageviewStatisticsVO;
import com.linyi.phsm.domain.rag.model.vo.PageviewVO;

import java.util.List;

public interface PageviewService {

    IPage<PageviewVO> pageQuery(PageviewPageRequest requestParam);

    PageviewVO queryById(String id);

    void increment(String illnessId);

    List<HotIllnessVO> hotIllnesses(int limit);

    PageviewStatisticsVO statistics();
}
