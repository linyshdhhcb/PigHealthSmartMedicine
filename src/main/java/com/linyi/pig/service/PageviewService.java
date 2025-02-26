package com.linyi.pig.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linyi.pig.common.model.PageResult;
import com.linyi.pig.entity.Pageview;
import com.linyi.pig.entity.vo.pageview.PageviewAddVo;
import com.linyi.pig.entity.vo.pageview.PageviewQueryVo;
import com.linyi.pig.entity.vo.pageview.PageviewUpdateVo;

/**
* @Author: linyi
* @Date: 2025-02-25 17:38:38
* @ClassName: PageviewService
* @Version: 1.0
* @Description: 浏览量 服务层
*/
public interface PageviewService extends IService<Pageview> {
    /**
     * 分页查询
     *
     * @param pageviewQueryVo 分页查询实体
     * @return PageResult<Pageview>
     */
    PageResult<Pageview> pageviewPage(PageviewQueryVo pageviewQueryVo);

    /**
     * 新增
     *
     * @param pageviewAddVo 新增实体
     * @return Boolean
     */
    Boolean pageviewAdd(PageviewAddVo pageviewAddVo);

    /**
     * 修改
     *
     * @param pageviewUpdateVo 修改实体
     * @return Boolean
     */
    Boolean pageviewUpdate(PageviewUpdateVo pageviewUpdateVo);
}
