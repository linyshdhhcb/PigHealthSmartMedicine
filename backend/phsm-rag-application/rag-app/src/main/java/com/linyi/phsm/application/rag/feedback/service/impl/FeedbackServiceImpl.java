package com.linyi.phsm.application.rag.feedback.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.linyi.phsm.application.rag.feedback.service.FeedbackService;
import com.linyi.phsm.domain.rag.model.entity.FeedbackDO;
import com.linyi.phsm.domain.rag.model.request.FeedbackCreateRequest;
import com.linyi.phsm.domain.rag.model.request.FeedbackPageRequest;
import com.linyi.phsm.domain.rag.model.vo.FeedbackVO;
import com.linyi.phsm.framework.context.UserContext;
import com.linyi.phsm.framework.exception.ClientException;
import com.linyi.phsm.infrastructure.persistence.rag.mapper.FeedbackMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackMapper feedbackMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String submit(FeedbackCreateRequest requestParam) {
        String uname = UserContext.requireUser().getUsername();
        FeedbackDO row = FeedbackDO.builder()
                .name(StrUtil.trim(requestParam.getName()))
                .email(StrUtil.trim(requestParam.getEmail()))
                .title(StrUtil.trim(requestParam.getTitle()))
                .content(StrUtil.trim(requestParam.getContent()))
                .createdBy(uname)
                .updatedBy(uname)
                .build();
        feedbackMapper.insert(row);
        return row.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        FeedbackDO row = load(id);
        feedbackMapper.deleteById(row.getId());
    }

    @Override
    public FeedbackVO queryById(String id) {
        return toVo(load(id));
    }

    @Override
    public IPage<FeedbackVO> pageQuery(FeedbackPageRequest requestParam) {
        String kw = StrUtil.trim(requestParam.getKeyword());
        Page<FeedbackDO> page = new Page<>(requestParam.getCurrent(), requestParam.getSize());
        IPage<FeedbackDO> result = feedbackMapper.selectPage(
                page,
                Wrappers.lambdaQuery(FeedbackDO.class)
                        .eq(FeedbackDO::getDeleted, 0)
                        .and(StrUtil.isNotBlank(kw), w -> w
                                .like(FeedbackDO::getTitle, kw)
                                .or()
                                .like(FeedbackDO::getContent, kw))
                        .orderByDesc(FeedbackDO::getCreateTime)
        );
        return result.convert(this::toVo);
    }

    private FeedbackDO load(String id) {
        FeedbackDO row = feedbackMapper.selectOne(
                Wrappers.lambdaQuery(FeedbackDO.class)
                        .eq(FeedbackDO::getId, id)
                        .eq(FeedbackDO::getDeleted, 0)
        );
        Assert.notNull(row, () -> new ClientException("反馈不存在"));
        return row;
    }

    private FeedbackVO toVo(FeedbackDO row) {
        return FeedbackVO.builder()
                .id(row.getId())
                .name(row.getName())
                .email(row.getEmail())
                .title(row.getTitle())
                .content(row.getContent())
                .createdBy(row.getCreatedBy())
                .createTime(row.getCreateTime())
                .updateTime(row.getUpdateTime())
                .build();
    }
}
