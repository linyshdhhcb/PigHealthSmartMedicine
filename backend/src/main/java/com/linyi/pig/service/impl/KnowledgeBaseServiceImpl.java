package com.linyi.pig.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linyi.pig.common.model.PageResult;
import com.linyi.pig.entity.KnowledgeBase;
import com.linyi.pig.entity.KnowledgeFile;
import com.linyi.pig.entity.vo.knowledge.KnowledgeBaseQueryVo;
import com.linyi.pig.mapper.KnowledgeBaseMapper;
import com.linyi.pig.mapper.KnowledgeFileMapper;
import com.linyi.pig.service.KnowledgeBaseService;
import com.linyi.pig.service.MilvusVectorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KnowledgeBaseServiceImpl extends ServiceImpl<KnowledgeBaseMapper, KnowledgeBase> implements KnowledgeBaseService {

    private final KnowledgeFileMapper knowledgeFileMapper;
    private final MilvusVectorService milvusVectorService;

    @Override
    public PageResult<KnowledgeBase> page(KnowledgeBaseQueryVo vo) {
        LambdaQueryWrapper<KnowledgeBase> qw = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(vo.getName())) {
            qw.like(KnowledgeBase::getName, vo.getName());
        }
        Page<KnowledgeBase> page = new Page<>(vo.getPageNum(), vo.getPageSize());
        Page<KnowledgeBase> res = this.baseMapper.selectPage(page, qw);
        return new PageResult<>(res.getRecords(), res.getTotal(), vo.getPageNum(), vo.getPageSize(), res.getPages());
    }

    @Override
    @Transactional
    public boolean removeWithCollectionById(Long id) {
        KnowledgeBase kb = getById(id);
        if (kb == null) {
            return false;
        }
        List<KnowledgeFile> docs = knowledgeFileMapper.selectList(
                new LambdaQueryWrapper<KnowledgeFile>().eq(KnowledgeFile::getKbId, id)
        );
        for (KnowledgeFile doc : docs) {
            milvusVectorService.deleteByDocId(doc.getId());
        }
        knowledgeFileMapper.delete(
                new LambdaQueryWrapper<KnowledgeFile>().eq(KnowledgeFile::getKbId, id)
        );
        milvusVectorService.dropCollection(kb.getCollectionName());
        return removeById(id);
    }
}
