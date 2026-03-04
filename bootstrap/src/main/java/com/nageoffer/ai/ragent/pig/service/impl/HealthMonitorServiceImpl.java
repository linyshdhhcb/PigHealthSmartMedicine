package com.nageoffer.ai.ragent.pig.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nageoffer.ai.ragent.pig.controller.vo.HealthMonitorVO;
import com.nageoffer.ai.ragent.pig.dao.entity.HealthMonitorDO;
import com.nageoffer.ai.ragent.pig.dao.mapper.HealthMonitorMapper;
import com.nageoffer.ai.ragent.pig.service.HealthMonitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 健康监测服务实现类
 */
@Service
@RequiredArgsConstructor
public class HealthMonitorServiceImpl implements HealthMonitorService {

    private final HealthMonitorMapper healthMonitorMapper;

    private static final Map<String, String> APPETITE_MAP = new HashMap<>();
    private static final Map<String, String> ACTIVITY_LEVEL_MAP = new HashMap<>();

    static {
        APPETITE_MAP.put("GOOD", "良好");
        APPETITE_MAP.put("NORMAL", "正常");
        APPETITE_MAP.put("POOR", "差");

        ACTIVITY_LEVEL_MAP.put("ACTIVE", "活跃");
        ACTIVITY_LEVEL_MAP.put("NORMAL", "正常");
        ACTIVITY_LEVEL_MAP.put("LETHARGIC", "嗜睡");
    }

    @Override
    public Page<HealthMonitorVO> listHealthMonitors(Long pigId, Integer pageNum, Integer pageSize) {
        Page<HealthMonitorDO> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<HealthMonitorDO> wrapper = new LambdaQueryWrapper<>();

        if (pigId != null) {
            wrapper.eq(HealthMonitorDO::getPigId, pigId);
        }

        wrapper.orderByDesc(HealthMonitorDO::getMonitorDate);
        Page<HealthMonitorDO> monitorPage = healthMonitorMapper.selectPage(page, wrapper);

        Page<HealthMonitorVO> voPage = new Page<>(monitorPage.getCurrent(), monitorPage.getSize(), monitorPage.getTotal());
        List<HealthMonitorVO> voList = monitorPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);

        return voPage;
    }

    @Override
    public HealthMonitorVO getHealthMonitorById(Long id) {
        HealthMonitorDO monitorDO = healthMonitorMapper.selectById(id);
        if (monitorDO == null) {
            return null;
        }
        return convertToVO(monitorDO);
    }

    @Override
    @Transactional
    public Long createHealthMonitor(HealthMonitorVO healthMonitorVO) {
        HealthMonitorDO monitorDO = new HealthMonitorDO();
        BeanUtils.copyProperties(healthMonitorVO, monitorDO);

        // 设置监测人为当前用户
        if (monitorDO.getMonitorBy() == null) {
            Long currentUserId = Long.parseLong(StpUtil.getLoginIdAsString());
            monitorDO.setMonitorBy(currentUserId);
        }

        healthMonitorMapper.insert(monitorDO);
        return monitorDO.getId();
    }

    @Override
    @Transactional
    public void updateHealthMonitor(HealthMonitorVO healthMonitorVO) {
        HealthMonitorDO monitorDO = new HealthMonitorDO();
        BeanUtils.copyProperties(healthMonitorVO, monitorDO);
        healthMonitorMapper.updateById(monitorDO);
    }

    @Override
    @Transactional
    public void deleteHealthMonitor(Long id) {
        healthMonitorMapper.deleteById(id);
    }

    private HealthMonitorVO convertToVO(HealthMonitorDO monitorDO) {
        HealthMonitorVO vo = new HealthMonitorVO();
        BeanUtils.copyProperties(monitorDO, vo);
        vo.setAppetiteName(APPETITE_MAP.getOrDefault(monitorDO.getAppetite(), monitorDO.getAppetite()));
        vo.setActivityLevelName(ACTIVITY_LEVEL_MAP.getOrDefault(monitorDO.getActivityLevel(), monitorDO.getActivityLevel()));
        return vo;
    }
}
