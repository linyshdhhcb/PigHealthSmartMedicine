package com.linyi.phsm.application.rag.history.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.linyi.phsm.domain.rag.model.request.HistoryCreateRequest;
import com.linyi.phsm.domain.rag.model.request.HistoryPageRequest;
import com.linyi.phsm.domain.rag.model.vo.HistoryVO;

public interface HistoryService {

    void record(HistoryCreateRequest requestParam);

    void delete(String id);

    void clearMine();

    HistoryVO queryById(String id);

    IPage<HistoryVO> pageQuery(HistoryPageRequest requestParam);
}
