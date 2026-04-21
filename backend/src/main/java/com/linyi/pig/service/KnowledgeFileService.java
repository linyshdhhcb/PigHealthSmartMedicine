package com.linyi.pig.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linyi.pig.common.model.PageResult;
import com.linyi.pig.entity.KnowledgeFile;
import com.linyi.pig.entity.vo.knowledge.KnowledgeFileQueryVo;
import com.linyi.pig.entity.vo.knowledge.KnowledgeFileUpdateVo;
import com.linyi.pig.entity.vo.knowledge.KnowledgeQaResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface KnowledgeFileService extends IService<KnowledgeFile> {
    KnowledgeFile saveToKnowledge(MultipartFile file, Integer createBy, Long kbId);

    PageResult<KnowledgeFile> page(KnowledgeFileQueryVo vo);

    boolean updateRemark(KnowledgeFileUpdateVo vo);

    boolean removeWithFileById(Long id);

    boolean removeWithFileByIds(java.util.List<Long> ids);

    KnowledgeQaResult qaByKbId(Long kbId, String question, int topK);
}
