package com.nageoffer.ai.ragent.pig.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nageoffer.ai.ragent.pig.controller.vo.FarmVO;
import com.nageoffer.ai.ragent.pig.dao.entity.FarmDO;
import com.nageoffer.ai.ragent.pig.dao.mapper.FarmMapper;
import com.nageoffer.ai.ragent.pig.service.FarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 养殖场管理服务实现类
 */
@Service
@RequiredArgsConstructor
public class FarmServiceImpl implements FarmService {

    private final FarmMapper farmMapper;

    private static final Map<String, String> SCALE_MAP = new HashMap<>();

    static {
        SCALE_MAP.put("SMALL", "小型");
        SCALE_MAP.put("MEDIUM", "中型");
        SCALE_MAP.put("LARGE", "大型");
    }

    @Override
    public Page<FarmVO> listFarms(Long ownerId, Integer pageNum, Integer pageSize) {
        Page<FarmDO> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<FarmDO> wrapper = new LambdaQueryWrapper<>();

        // 数据权限过滤：非管理员只能查看自己的养殖场
        if (!StpUtil.hasRole("admin")) {
            Long currentUserId = Long.parseLong(StpUtil.getLoginIdAsString());
            wrapper.eq(FarmDO::getOwnerId, currentUserId);
        } else if (ownerId != null) {
            wrapper.eq(FarmDO::getOwnerId, ownerId);
        }

        wrapper.orderByDesc(FarmDO::getCreateTime);
        Page<FarmDO> farmPage = farmMapper.selectPage(page, wrapper);

        Page<FarmVO> voPage = new Page<>(farmPage.getCurrent(), farmPage.getSize(), farmPage.getTotal());
        List<FarmVO> voList = farmPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);

        return voPage;
    }

    @Override
    public FarmVO getFarmById(Long id) {
        FarmDO farmDO = farmMapper.selectById(id);
        if (farmDO == null) {
            return null;
        }

        // 数据权限检查
        if (!StpUtil.hasRole("admin")) {
            Long currentUserId = Long.parseLong(StpUtil.getLoginIdAsString());
            if (!farmDO.getOwnerId().equals(currentUserId)) {
                throw new RuntimeException("无权访问该养殖场信息");
            }
        }

        return convertToVO(farmDO);
    }

    @Override
    @Transactional
    public Long createFarm(FarmVO farmVO) {
        FarmDO farmDO = new FarmDO();
        BeanUtils.copyProperties(farmVO, farmDO);

        // 设置场主为当前用户
        if (farmDO.getOwnerId() == null) {
            Long currentUserId = Long.parseLong(StpUtil.getLoginIdAsString());
            farmDO.setOwnerId(currentUserId);
        }

        farmMapper.insert(farmDO);
        return farmDO.getId();
    }

    @Override
    @Transactional
    public void updateFarm(FarmVO farmVO) {
        FarmDO existingFarm = farmMapper.selectById(farmVO.getId());
        if (existingFarm == null) {
            throw new RuntimeException("养殖场不存在");
        }

        // 数据权限检查
        if (!StpUtil.hasRole("admin")) {
            Long currentUserId = Long.parseLong(StpUtil.getLoginIdAsString());
            if (!existingFarm.getOwnerId().equals(currentUserId)) {
                throw new RuntimeException("无权修改该养殖场信息");
            }
        }

        FarmDO farmDO = new FarmDO();
        BeanUtils.copyProperties(farmVO, farmDO);
        farmMapper.updateById(farmDO);
    }

    @Override
    @Transactional
    public void deleteFarm(Long id) {
        FarmDO existingFarm = farmMapper.selectById(id);
        if (existingFarm == null) {
            throw new RuntimeException("养殖场不存在");
        }

        // 数据权限检查
        if (!StpUtil.hasRole("admin")) {
            Long currentUserId = Long.parseLong(StpUtil.getLoginIdAsString());
            if (!existingFarm.getOwnerId().equals(currentUserId)) {
                throw new RuntimeException("无权删除该养殖场");
            }
        }

        farmMapper.deleteById(id);
    }

    private FarmVO convertToVO(FarmDO farmDO) {
        FarmVO vo = new FarmVO();
        BeanUtils.copyProperties(farmDO, vo);
        vo.setScaleName(SCALE_MAP.getOrDefault(farmDO.getScale(), farmDO.getScale()));
        return vo;
    }
}
