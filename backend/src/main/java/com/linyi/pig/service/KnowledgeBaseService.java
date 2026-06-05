package com.linyi.pig.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linyi.pig.common.model.PageResult;
import com.linyi.pig.entity.KnowledgeBase;
import com.linyi.pig.entity.vo.knowledge.KnowledgeBaseQueryVo;

public interface KnowledgeBaseService extends IService<KnowledgeBase> {
    PageResult<KnowledgeBase> page(KnowledgeBaseQueryVo vo);

    boolean removeWithCollectionById(Long id);
}
