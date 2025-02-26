package com.linyi.pig.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linyi.pig.common.model.PageResult;
import com.linyi.pig.entity.ArticleTypes;
import com.linyi.pig.entity.vo.articleTypes.ArticleTypesAddVo;
import com.linyi.pig.entity.vo.articleTypes.ArticleTypesQueryVo;
import com.linyi.pig.entity.vo.articleTypes.ArticleTypesUpdateVo;

/**
* @Author: linyi
* @Date: 2025-02-26 08:42:14
* @ClassName: ArticleTypesService
* @Version: 1.0
* @Description: 文章类型 服务层
*/
public interface ArticleTypesService extends IService<ArticleTypes> {
    /**
     * 分页查询
     *
     * @param articleTypesQueryVo 分页查询实体
     * @return PageResult<ArticleTypes>
     */
    PageResult<ArticleTypes> articleTypesPage(ArticleTypesQueryVo articleTypesQueryVo);

    /**
     * 新增
     *
     * @param articleTypesAddVo 新增实体
     * @return Boolean
     */
    Boolean articleTypesAdd(ArticleTypesAddVo articleTypesAddVo);

    /**
     * 修改
     *
     * @param articleTypesUpdateVo 修改实体
     * @return Boolean
     */
    Boolean articleTypesUpdate(ArticleTypesUpdateVo articleTypesUpdateVo);
}
