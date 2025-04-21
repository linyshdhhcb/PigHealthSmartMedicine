package com.linyi.pig.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linyi.pig.common.model.PageResult;
import com.linyi.pig.entity.History;
import com.linyi.pig.entity.vo.history.HistoryAddVo;
import com.linyi.pig.entity.vo.history.HistoryQueryVo;
import com.linyi.pig.entity.vo.history.HistoryUpdateVo;

/**
* @Author: linyi
* @Date: 2025-02-25 17:38:38
* @ClassName: HistoryService
* @Version: 1.0
* @Description: 操作记录 服务层
*/
public interface HistoryService extends IService<History> {
    /**
     * 分页查询
     *
     * @param historyQueryVo 分页查询实体
     * @return PageResult<History>
     */
    PageResult<History> historyPage(HistoryQueryVo historyQueryVo);

    /**
     * 新增
     *
     * @param historyAddVo 新增实体
     * @return Boolean
     */
    Boolean historyAdd(HistoryAddVo historyAddVo);

    /**
     * 修改
     *
     * @param historyUpdateVo 修改实体
     * @return Boolean
     */
    Boolean historyUpdate(HistoryUpdateVo historyUpdateVo);
}
