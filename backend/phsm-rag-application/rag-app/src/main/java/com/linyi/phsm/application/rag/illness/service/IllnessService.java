package com.linyi.phsm.application.rag.illness.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.linyi.phsm.domain.rag.model.request.IllnessCreateRequest;
import com.linyi.phsm.domain.rag.model.request.IllnessPageRequest;
import com.linyi.phsm.domain.rag.model.request.IllnessUpdateRequest;
import com.linyi.phsm.domain.rag.model.vo.IllnessVO;

public interface IllnessService {

    String create(IllnessCreateRequest requestParam);

    void update(String id, IllnessUpdateRequest requestParam);

    void delete(String id);

    IllnessVO queryById(String id);

    IPage<IllnessVO> pageQuery(IllnessPageRequest requestParam);
}
