package com.linyi.pig.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linyi.pig.common.model.PageResult;
import com.linyi.pig.entity.Sessions;
import com.linyi.pig.entity.vo.sessions.SessionsAddVo;
import com.linyi.pig.entity.vo.sessions.SessionsQueryVo;
import com.linyi.pig.entity.vo.sessions.SessionsUpdateVo;

/**
* @Author: linyi
* @Date: 2025-02-26 08:42:14
* @ClassName: SessionsService
* @Version: 1.0
* @Description:  服务层
*/
public interface SessionsService extends IService<Sessions> {
    /**
     * 分页查询
     *
     * @param sessionsQueryVo 分页查询实体
     * @return PageResult<Sessions>
     */
    PageResult<Sessions> sessionsPage(SessionsQueryVo sessionsQueryVo);

    /**
     * 新增
     *
     * @param sessionsAddVo 新增实体
     * @return Boolean
     */
    Boolean sessionsAdd(SessionsAddVo sessionsAddVo);

    /**
     * 修改
     *
     * @param sessionsUpdateVo 修改实体
     * @return Boolean
     */
    Boolean sessionsUpdate(SessionsUpdateVo sessionsUpdateVo);
}
