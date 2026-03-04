package com.nageoffer.ai.ragent.pig.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nageoffer.ai.ragent.pig.controller.vo.ArticleVO;
import com.nageoffer.ai.ragent.pig.dao.entity.ArticleDO;
import com.nageoffer.ai.ragent.pig.dao.mapper.ArticleMapper;
import com.nageoffer.ai.ragent.pig.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 文章信息服务实现类
 */
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleMapper articleMapper;

    private static final Map<String, String> CATEGORY_MAP = new HashMap<>();
    private static final Map<String, String> STATUS_MAP = new HashMap<>();

    static {
        CATEGORY_MAP.put("BREEDING", "养殖知识");
        CATEGORY_MAP.put("DISEASE", "疾病防治");
        CATEGORY_MAP.put("NUTRITION", "营养饲料");
        CATEGORY_MAP.put("MANAGEMENT", "管理技术");
        CATEGORY_MAP.put("NEWS", "新闻资讯");

        STATUS_MAP.put("DRAFT", "草稿");
        STATUS_MAP.put("PUBLISHED", "已发布");
        STATUS_MAP.put("ARCHIVED", "已归档");
    }

    @Override
    public Page<ArticleVO> listArticles(String category, String status, Integer pageNum, Integer pageSize) {
        Page<ArticleDO> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<ArticleDO> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(category)) {
            wrapper.eq(ArticleDO::getCategory, category);
        }
        if (StringUtils.hasText(status)) {
            wrapper.eq(ArticleDO::getStatus, status);
        } else {
            // 默认只显示已发布的文章
            wrapper.eq(ArticleDO::getStatus, "PUBLISHED");
        }

        wrapper.orderByDesc(ArticleDO::getPublishTime);
        Page<ArticleDO> articlePage = articleMapper.selectPage(page, wrapper);

        Page<ArticleVO> voPage = new Page<>(articlePage.getCurrent(), articlePage.getSize(), articlePage.getTotal());
        List<ArticleVO> voList = articlePage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);

        return voPage;
    }

    @Override
    public ArticleVO getArticleById(Long id) {
        ArticleDO articleDO = articleMapper.selectById(id);
        if (articleDO == null) {
            return null;
        }
        return convertToVO(articleDO);
    }

    @Override
    @Transactional
    public Long createArticle(ArticleVO articleVO) {
        ArticleDO articleDO = new ArticleDO();
        BeanUtils.copyProperties(articleVO, articleDO);

        // 设置作者信息
        Long userId = Long.parseLong(StpUtil.getLoginIdAsString());
        articleDO.setAuthorId(userId);
        articleDO.setStatus("DRAFT");

        articleMapper.insert(articleDO);
        return articleDO.getId();
    }

    @Override
    @Transactional
    public void updateArticle(ArticleVO articleVO) {
        ArticleDO articleDO = new ArticleDO();
        BeanUtils.copyProperties(articleVO, articleDO);
        articleMapper.updateById(articleDO);
    }

    @Override
    @Transactional
    public void deleteArticle(Long id) {
        articleMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void publishArticle(Long id) {
        ArticleDO articleDO = articleMapper.selectById(id);
        if (articleDO != null) {
            articleDO.setStatus("PUBLISHED");
            articleDO.setPublishTime(new Date());
            articleMapper.updateById(articleDO);
        }
    }

    @Override
    @Transactional
    public void incrementViewCount(Long id) {
        ArticleDO articleDO = articleMapper.selectById(id);
        if (articleDO != null) {
            articleDO.setViewCount(articleDO.getViewCount() + 1);
            articleMapper.updateById(articleDO);
        }
    }

    @Override
    @Transactional
    public void likeArticle(Long id) {
        ArticleDO articleDO = articleMapper.selectById(id);
        if (articleDO != null) {
            articleDO.setLikeCount(articleDO.getLikeCount() + 1);
            articleMapper.updateById(articleDO);
        }
    }

    private ArticleVO convertToVO(ArticleDO articleDO) {
        ArticleVO vo = new ArticleVO();
        BeanUtils.copyProperties(articleDO, vo);
        vo.setCategoryName(CATEGORY_MAP.getOrDefault(articleDO.getCategory(), articleDO.getCategory()));
        vo.setStatusName(STATUS_MAP.getOrDefault(articleDO.getStatus(), articleDO.getStatus()));
        return vo;
    }
}
