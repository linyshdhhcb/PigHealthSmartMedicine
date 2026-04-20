package com.linyi.phsm.application.rag.news.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.linyi.phsm.application.rag.news.service.NewsArticleService;
import com.linyi.phsm.domain.rag.model.entity.NewsArticleDO;
import com.linyi.phsm.domain.rag.model.request.NewsArticleCreateRequest;
import com.linyi.phsm.domain.rag.model.request.NewsArticlePageRequest;
import com.linyi.phsm.domain.rag.model.request.NewsArticleUpdateRequest;
import com.linyi.phsm.domain.rag.model.vo.NewsArticleVO;
import com.linyi.phsm.framework.context.UserContext;
import com.linyi.phsm.framework.exception.ClientException;
import com.linyi.phsm.infrastructure.persistence.rag.mapper.NewsArticleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class NewsArticleServiceImpl implements NewsArticleService {

    private final NewsArticleMapper newsArticleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String create(NewsArticleCreateRequest requestParam) {
        String uname = UserContext.requireUser().getUsername();
        Date publish = requestParam.getPublishTime() != null ? requestParam.getPublishTime() : new Date();
        assertPublishTime(publish);
        String author = StrUtil.isNotBlank(requestParam.getAuthor()) ? StrUtil.trim(requestParam.getAuthor()) : uname;
        NewsArticleDO row = NewsArticleDO.builder()
                .url(StrUtil.trimToNull(requestParam.getUrl()))
                .title(StrUtil.trim(requestParam.getTitle()))
                .content(requestParam.getContent())
                .author(author)
                .publishTime(publish)
                .source(StrUtil.trimToNull(requestParam.getSource()))
                .summary(StrUtil.trimToNull(requestParam.getSummary()))
                .createdBy(uname)
                .updatedBy(uname)
                .build();
        newsArticleMapper.insert(row);
        return row.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(String id, NewsArticleUpdateRequest requestParam) {
        NewsArticleDO row = load(id);
        Date publish = requestParam.getPublishTime() != null ? requestParam.getPublishTime() : row.getPublishTime();
        if (requestParam.getPublishTime() != null) {
            assertPublishTime(publish);
        }
        row.setUrl(StrUtil.trimToNull(requestParam.getUrl()));
        row.setTitle(StrUtil.trim(requestParam.getTitle()));
        row.setContent(requestParam.getContent());
        row.setAuthor(StrUtil.trimToNull(requestParam.getAuthor()));
        row.setPublishTime(publish);
        row.setSource(StrUtil.trimToNull(requestParam.getSource()));
        row.setSummary(StrUtil.trimToNull(requestParam.getSummary()));
        row.setUpdatedBy(UserContext.requireUser().getUsername());
        newsArticleMapper.updateById(row);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        NewsArticleDO row = load(id);
        newsArticleMapper.deleteById(row.getId());
    }

    @Override
    public NewsArticleVO queryById(String id) {
        return toVo(load(id));
    }

    @Override
    public IPage<NewsArticleVO> pageQuery(NewsArticlePageRequest requestParam) {
        String kw = StrUtil.trim(requestParam.getKeyword());
        String src = StrUtil.trim(requestParam.getSource());
        Page<NewsArticleDO> page = new Page<>(requestParam.getCurrent(), requestParam.getSize());
        IPage<NewsArticleDO> result = newsArticleMapper.selectPage(
                page,
                Wrappers.lambdaQuery(NewsArticleDO.class)
                        .eq(NewsArticleDO::getDeleted, 0)
                        .like(StrUtil.isNotBlank(src), NewsArticleDO::getSource, src)
                        .and(StrUtil.isNotBlank(kw), w -> w
                                .like(NewsArticleDO::getTitle, kw)
                                .or()
                                .like(NewsArticleDO::getContent, kw))
                        .orderByDesc(NewsArticleDO::getPublishTime)
        );
        return result.convert(this::toVo);
    }

    private void assertPublishTime(Date publish) {
        Assert.isTrue(!publish.after(new Date()), () -> new ClientException("发布时间不能晚于当前时间"));
    }

    private NewsArticleDO load(String id) {
        NewsArticleDO row = newsArticleMapper.selectOne(
                Wrappers.lambdaQuery(NewsArticleDO.class)
                        .eq(NewsArticleDO::getId, id)
                        .eq(NewsArticleDO::getDeleted, 0)
        );
        Assert.notNull(row, () -> new ClientException("新闻不存在"));
        return row;
    }

    private NewsArticleVO toVo(NewsArticleDO row) {
        return NewsArticleVO.builder()
                .id(row.getId())
                .url(row.getUrl())
                .title(row.getTitle())
                .content(row.getContent())
                .author(row.getAuthor())
                .publishTime(row.getPublishTime())
                .source(row.getSource())
                .summary(row.getSummary())
                .createdBy(row.getCreatedBy())
                .updatedBy(row.getUpdatedBy())
                .createTime(row.getCreateTime())
                .updateTime(row.getUpdateTime())
                .build();
    }
}
