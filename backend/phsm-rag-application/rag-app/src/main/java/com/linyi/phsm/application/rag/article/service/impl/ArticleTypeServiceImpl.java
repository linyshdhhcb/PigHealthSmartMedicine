package com.linyi.phsm.application.rag.article.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.linyi.phsm.application.rag.article.service.ArticleTypeService;
import com.linyi.phsm.domain.rag.model.entity.ArticleTypeDO;
import com.linyi.phsm.domain.rag.model.request.ArticleTypeCreateRequest;
import com.linyi.phsm.domain.rag.model.request.ArticleTypePageRequest;
import com.linyi.phsm.domain.rag.model.request.ArticleTypeUpdateRequest;
import com.linyi.phsm.domain.rag.model.vo.ArticleTypeVO;
import com.linyi.phsm.framework.context.UserContext;
import com.linyi.phsm.framework.exception.ClientException;
import com.linyi.phsm.infrastructure.persistence.rag.mapper.ArticleTypeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArticleTypeServiceImpl implements ArticleTypeService {

    private final ArticleTypeMapper articleTypeMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String create(ArticleTypeCreateRequest requestParam) {
        String uname = UserContext.requireUser().getUsername();
        String name = StrUtil.trim(requestParam.getTypeName());
        Assert.notBlank(name, () -> new ClientException("文章类型名称不能为空"));
        ArticleTypeDO row = ArticleTypeDO.builder()
                .typeName(name)
                .createdBy(uname)
                .updatedBy(uname)
                .build();
        articleTypeMapper.insert(row);
        return row.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(String id, ArticleTypeUpdateRequest requestParam) {
        ArticleTypeDO row = load(id);
        String name = StrUtil.trim(requestParam.getTypeName());
        Assert.notBlank(name, () -> new ClientException("文章类型名称不能为空"));
        row.setTypeName(name);
        row.setUpdatedBy(UserContext.requireUser().getUsername());
        articleTypeMapper.updateById(row);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        ArticleTypeDO row = load(id);
        articleTypeMapper.deleteById(row.getId());
    }

    @Override
    public ArticleTypeVO queryById(String id) {
        return toVo(load(id));
    }

    @Override
    public IPage<ArticleTypeVO> pageQuery(ArticleTypePageRequest requestParam) {
        String cond = StrUtil.trim(requestParam.getTypeName());
        Page<ArticleTypeDO> page = new Page<>(requestParam.getCurrent(), requestParam.getSize());
        IPage<ArticleTypeDO> result = articleTypeMapper.selectPage(
                page,
                Wrappers.lambdaQuery(ArticleTypeDO.class)
                        .eq(ArticleTypeDO::getDeleted, 0)
                        .like(StrUtil.isNotBlank(cond), ArticleTypeDO::getTypeName, cond)
                        .orderByDesc(ArticleTypeDO::getUpdateTime)
        );
        return result.convert(this::toVo);
    }

    private ArticleTypeDO load(String id) {
        ArticleTypeDO row = articleTypeMapper.selectOne(
                Wrappers.lambdaQuery(ArticleTypeDO.class)
                        .eq(ArticleTypeDO::getId, id)
                        .eq(ArticleTypeDO::getDeleted, 0)
        );
        Assert.notNull(row, () -> new ClientException("文章类型不存在"));
        return row;
    }

    private ArticleTypeVO toVo(ArticleTypeDO row) {
        return ArticleTypeVO.builder()
                .id(row.getId())
                .typeName(row.getTypeName())
                .createdBy(row.getCreatedBy())
                .updatedBy(row.getUpdatedBy())
                .createTime(row.getCreateTime())
                .updateTime(row.getUpdateTime())
                .build();
    }
}
