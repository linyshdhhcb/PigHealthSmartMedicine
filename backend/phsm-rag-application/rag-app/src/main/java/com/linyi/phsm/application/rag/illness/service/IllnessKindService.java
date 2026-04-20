package com.linyi.phsm.application.rag.illness.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.linyi.phsm.domain.rag.model.request.IllnessKindCreateRequest;
import com.linyi.phsm.domain.rag.model.request.IllnessKindPageRequest;
import com.linyi.phsm.domain.rag.model.request.IllnessKindUpdateRequest;
import com.linyi.phsm.domain.rag.model.vo.IllnessKindVO;

public interface IllnessKindService {

    String create(IllnessKindCreateRequest requestParam);

    void update(String id, IllnessKindUpdateRequest requestParam);

    void delete(String id);

    IllnessKindVO queryById(String id);

    IPage<IllnessKindVO> pageQuery(IllnessKindPageRequest requestParam);
}
