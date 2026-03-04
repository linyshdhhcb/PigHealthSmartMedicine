

package com.nageoffer.ai.ragent.rag.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.nageoffer.ai.ragent.rag.dao.entity.RagTraceNodeDO;
import com.nageoffer.ai.ragent.rag.dao.entity.RagTraceRunDO;
import com.nageoffer.ai.ragent.rag.dao.mapper.RagTraceNodeMapper;
import com.nageoffer.ai.ragent.rag.dao.mapper.RagTraceRunMapper;
import com.nageoffer.ai.ragent.rag.service.RagTraceRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * RAG Trace 记录服务实现
 */
@Service
@RequiredArgsConstructor
public class RagTraceRecordServiceImpl implements RagTraceRecordService {

    private final RagTraceRunMapper runMapper;
    private final RagTraceNodeMapper nodeMapper;

    @Override
    public void startRun(RagTraceRunDO run) {
        runMapper.insert(run);
    }

    @Override
    public void finishRun(String traceId, String status, String errorMessage, Date endTime, long durationMs) {
        RagTraceRunDO update = RagTraceRunDO.builder()
                .status(status)
                .errorMessage(errorMessage)
                .endTime(endTime)
                .durationMs(durationMs)
                .build();
        runMapper.update(update, Wrappers.lambdaUpdate(RagTraceRunDO.class)
                .eq(RagTraceRunDO::getTraceId, traceId));
    }

    @Override
    public void startNode(RagTraceNodeDO node) {
        nodeMapper.insert(node);
    }

    @Override
    public void finishNode(String traceId, String nodeId, String status, String errorMessage, Date endTime, long durationMs) {
        RagTraceNodeDO update = RagTraceNodeDO.builder()
                .status(status)
                .errorMessage(errorMessage)
                .endTime(endTime)
                .durationMs(durationMs)
                .build();
        nodeMapper.update(update, Wrappers.lambdaUpdate(RagTraceNodeDO.class)
                .eq(RagTraceNodeDO::getTraceId, traceId)
                .eq(RagTraceNodeDO::getNodeId, nodeId));
    }
}
