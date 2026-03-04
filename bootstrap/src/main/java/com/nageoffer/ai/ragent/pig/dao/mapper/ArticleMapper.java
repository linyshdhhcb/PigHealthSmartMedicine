package com.nageoffer.ai.ragent.pig.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nageoffer.ai.ragent.pig.dao.entity.ArticleDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文章信息Mapper接口
 */
@Mapper
public interface ArticleMapper extends BaseMapper<ArticleDO> {
}
