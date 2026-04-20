package com.linyi.phsm.application.rag.article.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.linyi.phsm.application.rag.article.service.ArticleService;
import com.linyi.phsm.domain.rag.model.entity.ArticleDO;
import com.linyi.phsm.domain.rag.model.entity.ArticleTypeDO;
import com.linyi.phsm.domain.rag.model.request.ArticleCreateRequest;
import com.linyi.phsm.domain.rag.model.request.ArticlePageRequest;
import com.linyi.phsm.domain.rag.model.request.ArticleUpdateRequest;
import com.linyi.phsm.domain.rag.model.vo.ArticleVO;
import com.linyi.phsm.framework.context.UserContext;
import com.linyi.phsm.framework.exception.ClientException;
import com.linyi.phsm.infrastructure.persistence.rag.mapper.ArticleMapper;
import com.linyi.phsm.infrastructure.persistence.rag.mapper.ArticleTypeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleMapper articleMapper;
    private final ArticleTypeMapper articleTypeMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String create(ArticleCreateRequest requestParam) {
        assertTypeExists(requestParam.getTypeId());
        String uname = UserContext.requireUser().getUsername();
        ArticleDO row = ArticleDO.builder()
                .title(StrUtil.trim(requestParam.getTitle()))
                .content(requestParam.getContent())
                .author(StrUtil.trim(requestParam.getAuthor()))
                .typeId(requestParam.getTypeId())
                .createdBy(uname)
                .updatedBy(uname)
                .build();
        articleMapper.insert(row);
        return row.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(String id, ArticleUpdateRequest requestParam) {
        ArticleDO row = load(id);
        assertTypeExists(requestParam.getTypeId());
        row.setTitle(StrUtil.trim(requestParam.getTitle()));
        row.setContent(requestParam.getContent());
        row.setAuthor(StrUtil.trim(requestParam.getAuthor()));
        row.setTypeId(requestParam.getTypeId());
        row.setUpdatedBy(UserContext.requireUser().getUsername());
        articleMapper.updateById(row);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        ArticleDO row = load(id);
        articleMapper.deleteById(row.getId());
    }

    @Override
    public ArticleVO queryById(String id) {
        ArticleDO row = load(id);
        return toVo(row, resolveTypeNames(Set.of(row.getTypeId())).get(row.getTypeId()));
    }

    @Override
    public IPage<ArticleVO> pageQuery(ArticlePageRequest requestParam) {
        String typeId = StrUtil.trim(requestParam.getTypeId());
        String kw = StrUtil.trim(requestParam.getKeyword());
        Page<ArticleDO> page = new Page<>(requestParam.getCurrent(), requestParam.getSize());
        IPage<ArticleDO> result = articleMapper.selectPage(
                page,
                Wrappers.lambdaQuery(ArticleDO.class)
                        .eq(ArticleDO::getDeleted, 0)
                        .eq(StrUtil.isNotBlank(typeId), ArticleDO::getTypeId, typeId)
                        .and(StrUtil.isNotBlank(kw), w -> w
                                .like(ArticleDO::getTitle, kw)
                                .or()
                                .like(ArticleDO::getContent, kw)
                                .or()
                                .like(ArticleDO::getAuthor, kw))
                        .orderByDesc(ArticleDO::getUpdateTime)
        );
        Set<String> typeIds = new HashSet<>();
        for (ArticleDO r : result.getRecords()) {
            if (StrUtil.isNotBlank(r.getTypeId())) {
                typeIds.add(r.getTypeId());
            }
        }
        Map<String, String> typeNames = resolveTypeNames(typeIds);
        return result.convert(a -> toVo(a, typeNames.get(a.getTypeId())));
    }

    private void assertTypeExists(String typeId) {
        ArticleTypeDO t = articleTypeMapper.selectOne(
                Wrappers.lambdaQuery(ArticleTypeDO.class)
                        .eq(ArticleTypeDO::getId, typeId)
                        .eq(ArticleTypeDO::getDeleted, 0)
        );
        Assert.notNull(t, () -> new ClientException("文章类型不存在"));
    }

    private Map<String, String> resolveTypeNames(Set<String> typeIds) {
        if (typeIds.isEmpty()) {
            return Map.of();
        }
        List<ArticleTypeDO> list = articleTypeMapper.selectList(
                Wrappers.lambdaQuery(ArticleTypeDO.class)
                        .in(ArticleTypeDO::getId, typeIds)
                        .eq(ArticleTypeDO::getDeleted, 0)
        );
        Map<String, String> map = new HashMap<>();
        for (ArticleTypeDO t : list) {
            map.put(t.getId(), t.getTypeName());
        }
        return map;
    }

    private ArticleDO load(String id) {
        ArticleDO row = articleMapper.selectOne(
                Wrappers.lambdaQuery(ArticleDO.class)
                        .eq(ArticleDO::getId, id)
                        .eq(ArticleDO::getDeleted, 0)
        );
        Assert.notNull(row, () -> new ClientException("文章不存在"));
        return row;
    }

    private ArticleVO toVo(ArticleDO row, String typeName) {
        return ArticleVO.builder()
                .id(row.getId())
                .title(row.getTitle())
                .content(row.getContent())
                .author(row.getAuthor())
                .typeId(row.getTypeId())
                .typeName(typeName)
                .createdBy(row.getCreatedBy())
                .updatedBy(row.getUpdatedBy())
                .createTime(row.getCreateTime())
                .updateTime(row.getUpdateTime())
                .build();
    }
}
