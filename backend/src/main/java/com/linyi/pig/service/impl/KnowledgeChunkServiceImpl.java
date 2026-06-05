package com.linyi.pig.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linyi.pig.common.model.PageResult;
import com.linyi.pig.entity.KnowledgeChunk;
import com.linyi.pig.entity.vo.knowledge.KnowledgeChunkQueryVo;
import com.linyi.pig.mapper.KnowledgeChunkMapper;
import com.linyi.pig.service.KnowledgeChunkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KnowledgeChunkServiceImpl extends ServiceImpl<KnowledgeChunkMapper, KnowledgeChunk> implements KnowledgeChunkService {

    @Override
    public PageResult<KnowledgeChunk> page(KnowledgeChunkQueryVo vo) {
        LambdaQueryWrapper<KnowledgeChunk> qw = new LambdaQueryWrapper<>();
        if (vo.getKbId() != null) {
            qw.eq(KnowledgeChunk::getKbId, vo.getKbId());
        }
        if (vo.getDocId() != null) {
            qw.eq(KnowledgeChunk::getDocId, vo.getDocId());
        }
        qw.orderByAsc(KnowledgeChunk::getDocId, KnowledgeChunk::getChunkIndex);
        Page<KnowledgeChunk> page = new Page<>(vo.getPageNum(), vo.getPageSize());
        Page<KnowledgeChunk> res = this.baseMapper.selectPage(page, qw);
        return new PageResult<>(res.getRecords(), res.getTotal(), vo.getPageNum(), vo.getPageSize(), res.getPages());
    }
}
