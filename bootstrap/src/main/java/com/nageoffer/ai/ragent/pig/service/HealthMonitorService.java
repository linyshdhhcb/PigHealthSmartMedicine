package com.nageoffer.ai.ragent.pig.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nageoffer.ai.ragent.pig.controller.vo.HealthMonitorVO;

/**
 * 健康监测服务接口
 */
public interface HealthMonitorService {

    /**
     * 分页查询健康监测记录列表
     */
    Page<HealthMonitorVO> listHealthMonitors(Long pigId, Integer pageNum, Integer pageSize);

    /**
     * 根据ID查询健康监测记录详情
     */
    HealthMonitorVO getHealthMonitorById(Long id);

    /**
     * 创建健康监测记录
     */
    Long createHealthMonitor(HealthMonitorVO healthMonitorVO);

    /**
     * 更新健康监测记录
     */
    void updateHealthMonitor(HealthMonitorVO healthMonitorVO);

    /**
     * 删除健康监测记录
     */
    void deleteHealthMonitor(Long id);
}
