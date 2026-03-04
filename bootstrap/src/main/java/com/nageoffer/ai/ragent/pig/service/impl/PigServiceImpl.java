package com.nageoffer.ai.ragent.pig.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nageoffer.ai.ragent.pig.controller.vo.PigVO;
import com.nageoffer.ai.ragent.pig.dao.entity.PigDO;
import com.nageoffer.ai.ragent.pig.dao.mapper.PigMapper;
import com.nageoffer.ai.ragent.pig.service.PigService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 生猪信息服务实现类
 */
@Service
@RequiredArgsConstructor
public class PigServiceImpl implements PigService {

    private final PigMapper pigMapper;

    private static final Map<String, String> HEALTH_STATUS_MAP = new HashMap<>();
    private static final Map<String, String> FEED_STATUS_MAP = new HashMap<>();

    static {
        HEALTH_STATUS_MAP.put("HEALTHY", "健康");
        HEALTH_STATUS_MAP.put("SICK", "患病");
        HEALTH_STATUS_MAP.put("RECOVERING", "康复中");
        HEALTH_STATUS_MAP.put("DEAD", "死亡");

        FEED_STATUS_MAP.put("NORMAL", "正常");
        FEED_STATUS_MAP.put("WEANING", "断奶");
        FEED_STATUS_MAP.put("FATTENING", "育肥");
    }

    @Override
    public Page<PigVO> listPigs(Long farmId, Long userId, String healthStatus, Integer pageNum, Integer pageSize) {
        Page<PigDO> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<PigDO> wrapper = new LambdaQueryWrapper<>();

        if (farmId != null) {
            wrapper.eq(PigDO::getFarmId, farmId);
        }
        if (userId != null) {
            wrapper.eq(PigDO::getUserId, userId);
        }
        if (StringUtils.hasText(healthStatus)) {
            wrapper.eq(PigDO::getHealthStatus, healthStatus);
        }

        wrapper.orderByDesc(PigDO::getCreateTime);
        Page<PigDO> pigPage = pigMapper.selectPage(page, wrapper);

        Page<PigVO> voPage = new Page<>(pigPage.getCurrent(), pigPage.getSize(), pigPage.getTotal());
        List<PigVO> voList = pigPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);

        return voPage;
    }

    @Override
    public PigVO getPigById(Long id) {
        PigDO pigDO = pigMapper.selectById(id);
        if (pigDO == null) {
            return null;
        }
        return convertToVO(pigDO);
    }

    @Override
    public Long createPig(PigVO pigVO) {
        PigDO pigDO = new PigDO();
        BeanUtils.copyProperties(pigVO, pigDO);
        pigMapper.insert(pigDO);
        return pigDO.getId();
    }

    @Override
    public void updatePig(PigVO pigVO) {
        PigDO pigDO = new PigDO();
        BeanUtils.copyProperties(pigVO, pigDO);
        pigMapper.updateById(pigDO);
    }

    @Override
    public void deletePig(Long id) {
        pigMapper.deleteById(id);
    }

    @Override
    public PigVO getPigByEarTag(String earTagNumber) {
        LambdaQueryWrapper<PigDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PigDO::getEarTagNumber, earTagNumber);
        PigDO pigDO = pigMapper.selectOne(wrapper);
        if (pigDO == null) {
            return null;
        }
        return convertToVO(pigDO);
    }

    private PigVO convertToVO(PigDO pigDO) {
        PigVO vo = new PigVO();
        BeanUtils.copyProperties(pigDO, vo);
        vo.setGenderName(pigDO.getGender() == 0 ? "母猪" : "公猪");
        vo.setHealthStatusName(HEALTH_STATUS_MAP.getOrDefault(pigDO.getHealthStatus(), pigDO.getHealthStatus()));
        vo.setFeedStatusName(FEED_STATUS_MAP.getOrDefault(pigDO.getFeedStatus(), pigDO.getFeedStatus()));

        // 计算年龄（天数）
        if (pigDO.getBirthDate() != null) {
            LocalDate birthDate = pigDO.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate now = LocalDate.now();
            long days = ChronoUnit.DAYS.between(birthDate, now);
            vo.setAge((int) days);
        }

        return vo;
    }
}
