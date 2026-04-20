package com.linyi.phsm.application.rag.feedback.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.linyi.phsm.domain.rag.model.request.FeedbackCreateRequest;
import com.linyi.phsm.domain.rag.model.request.FeedbackPageRequest;
import com.linyi.phsm.domain.rag.model.vo.FeedbackVO;

public interface FeedbackService {

    String submit(FeedbackCreateRequest requestParam);

    void delete(String id);

    FeedbackVO queryById(String id);

    IPage<FeedbackVO> pageQuery(FeedbackPageRequest requestParam);
}
