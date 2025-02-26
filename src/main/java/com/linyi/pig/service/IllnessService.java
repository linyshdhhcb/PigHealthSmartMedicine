package com.linyi.pig.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linyi.pig.common.model.PageResult;
import com.linyi.pig.entity.Illness;
import com.linyi.pig.entity.vo.illness.IllnessAddVo;
import com.linyi.pig.entity.vo.illness.IllnessQueryVo;
import com.linyi.pig.entity.vo.illness.IllnessUpdateVo;

/**
* @Author: linyi
* @Date: 2025-02-25 17:38:38
* @ClassName: IllnessService
* @Version: 1.0
* @Description: 疾病 服务层
*/
public interface IllnessService extends IService<Illness> {
    /**
     * 分页查询
     *
     * @param illnessQueryVo 分页查询实体
     * @return PageResult<Illness>
     */
    PageResult<Illness> illnessPage(IllnessQueryVo illnessQueryVo);

    /**
     * 新增
     *
     * @param illnessAddVo 新增实体
     * @return Boolean
     */
    Boolean illnessAdd(IllnessAddVo illnessAddVo);

    /**
     * 修改
     *
     * @param illnessUpdateVo 修改实体
     * @return Boolean
     */
    Boolean illnessUpdate(IllnessUpdateVo illnessUpdateVo);
}
