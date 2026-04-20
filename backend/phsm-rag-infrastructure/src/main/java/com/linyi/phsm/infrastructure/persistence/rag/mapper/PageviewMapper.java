package com.linyi.phsm.infrastructure.persistence.rag.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linyi.phsm.domain.rag.model.entity.PageviewDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface PageviewMapper extends BaseMapper<PageviewDO> {

    @Select("""
            SELECT pv.illness_id AS illness_id,
                   MAX(i.illness_name) AS illness_name,
                   SUM(pv.pageviews)::bigint AS pageviews
            FROM t_pageview pv
            LEFT JOIN t_illness i ON i.id = pv.illness_id AND i.deleted = 0
            WHERE pv.deleted = 0
            GROUP BY pv.illness_id
            ORDER BY SUM(pv.pageviews) DESC
            LIMIT #{limit}
            """)
    List<Map<String, Object>> selectHotIllnessAggregated(@Param("limit") int limit);
}
