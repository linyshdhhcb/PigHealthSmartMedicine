package com.nageoffer.ai.ragent.pig.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nageoffer.ai.ragent.pig.dao.entity.CaseDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 病例信息Mapper接口
 */
@Mapper
public interface CaseMapper extends BaseMapper<CaseDO> {
}
