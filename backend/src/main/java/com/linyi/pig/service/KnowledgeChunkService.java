package com.linyi.pig.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linyi.pig.common.model.PageResult;
import com.linyi.pig.entity.KnowledgeChunk;
import com.linyi.pig.entity.vo.knowledge.KnowledgeChunkQueryVo;

public interface KnowledgeChunkService extends IService<KnowledgeChunk> {
    PageResult<KnowledgeChunk> page(KnowledgeChunkQueryVo vo);
}
