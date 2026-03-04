package com.nageoffer.ai.ragent.pig.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nageoffer.ai.ragent.pig.controller.vo.CaseVO;
import com.nageoffer.ai.ragent.pig.dao.entity.CaseDO;
import com.nageoffer.ai.ragent.pig.dao.mapper.CaseMapper;
import com.nageoffer.ai.ragent.pig.service.CaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 病例信息服务实现类
 */
@Service
@RequiredArgsConstructor
public class CaseServiceImpl implements CaseService {

    private final CaseMapper caseMapper;

    private static final Map<String, String> STATUS_MAP = new HashMap<>();

    static {
        STATUS_MAP.put("UNTREATED", "未治疗");
        STATUS_MAP.put("TREATING", "治疗中");
        STATUS_MAP.put("CURED", "已治愈");
        STATUS_MAP.put("DEAD", "死亡");
    }

    @Override
    public Page<CaseVO> listCases(Long pigId, Long userId, String status, Integer pageNum, Integer pageSize) {
        Page<CaseDO> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<CaseDO> wrapper = new LambdaQueryWrapper<>();

        if (pigId != null) {
            wrapper.eq(CaseDO::getPigId, pigId);
        }
        if (userId != null) {
            wrapper.eq(CaseDO::getUserId, userId);
        }
        if (StringUtils.hasText(status)) {
            wrapper.eq(CaseDO::getStatus, status);
        }

        wrapper.orderByDesc(CaseDO::getCreateTime);
        Page<CaseDO> casePage = caseMapper.selectPage(page, wrapper);

        Page<CaseVO> voPage = new Page<>(casePage.getCurrent(), casePage.getSize(), casePage.getTotal());
        List<CaseVO> voList = casePage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);

        return voPage;
    }

    @Override
    public CaseVO getCaseById(Long id) {
        CaseDO caseDO = caseMapper.selectById(id);
        if (caseDO == null) {
            return null;
        }
        return convertToVO(caseDO);
    }

    @Override
    public Long createCase(CaseVO caseVO) {
        CaseDO caseDO = new CaseDO();
        BeanUtils.copyProperties(caseVO, caseDO);
        caseMapper.insert(caseDO);
        return caseDO.getId();
    }

    @Override
    public void updateCase(CaseVO caseVO) {
        CaseDO caseDO = new CaseDO();
        BeanUtils.copyProperties(caseVO, caseDO);
        caseMapper.updateById(caseDO);
    }

    @Override
    public void deleteCase(Long id) {
        caseMapper.deleteById(id);
    }

    @Override
    public CaseVO getCaseByConversationId(String conversationId) {
        LambdaQueryWrapper<CaseDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CaseDO::getConversationId, conversationId);
        CaseDO caseDO = caseMapper.selectOne(wrapper);
        if (caseDO == null) {
            return null;
        }
        return convertToVO(caseDO);
    }

    private CaseVO convertToVO(CaseDO caseDO) {
        CaseVO vo = new CaseVO();
        BeanUtils.copyProperties(caseDO, vo);
        vo.setStatusName(STATUS_MAP.getOrDefault(caseDO.getStatus(), caseDO.getStatus()));
        return vo;
    }
}
