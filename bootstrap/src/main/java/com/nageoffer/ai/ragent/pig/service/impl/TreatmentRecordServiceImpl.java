package com.nageoffer.ai.ragent.pig.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nageoffer.ai.ragent.pig.controller.vo.TreatmentRecordVO;
import com.nageoffer.ai.ragent.pig.dao.entity.TreatmentRecordDO;
import com.nageoffer.ai.ragent.pig.dao.mapper.TreatmentRecordMapper;
import com.nageoffer.ai.ragent.pig.service.TreatmentRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 治疗记录服务实现类
 */
@Service
@RequiredArgsConstructor
public class TreatmentRecordServiceImpl implements TreatmentRecordService {

    private final TreatmentRecordMapper treatmentRecordMapper;

    private static final Map<String, String> TREATMENT_TYPE_MAP = new HashMap<>();
    private static final Map<String, String> TREATMENT_EFFECT_MAP = new HashMap<>();

    static {
        TREATMENT_TYPE_MAP.put("MEDICATION", "药物治疗");
        TREATMENT_TYPE_MAP.put("SURGERY", "手术");
        TREATMENT_TYPE_MAP.put("NURSING", "护理");

        TREATMENT_EFFECT_MAP.put("EFFECTIVE", "有效");
        TREATMENT_EFFECT_MAP.put("INEFFECTIVE", "无效");
        TREATMENT_EFFECT_MAP.put("IMPROVED", "好转");
    }

    @Override
    public Page<TreatmentRecordVO> listTreatmentRecords(Long caseId, Long pigId, Integer pageNum, Integer pageSize) {
        Page<TreatmentRecordDO> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<TreatmentRecordDO> wrapper = new LambdaQueryWrapper<>();

        if (caseId != null) {
            wrapper.eq(TreatmentRecordDO::getCaseId, caseId);
        }
        if (pigId != null) {
            wrapper.eq(TreatmentRecordDO::getPigId, pigId);
        }

        wrapper.orderByDesc(TreatmentRecordDO::getTreatmentDate);
        Page<TreatmentRecordDO> recordPage = treatmentRecordMapper.selectPage(page, wrapper);

        Page<TreatmentRecordVO> voPage = new Page<>(recordPage.getCurrent(), recordPage.getSize(), recordPage.getTotal());
        List<TreatmentRecordVO> voList = recordPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);

        return voPage;
    }

    @Override
    public TreatmentRecordVO getTreatmentRecordById(Long id) {
        TreatmentRecordDO recordDO = treatmentRecordMapper.selectById(id);
        if (recordDO == null) {
            return null;
        }
        return convertToVO(recordDO);
    }

    @Override
    @Transactional
    public Long createTreatmentRecord(TreatmentRecordVO treatmentRecordVO) {
        TreatmentRecordDO recordDO = new TreatmentRecordDO();
        BeanUtils.copyProperties(treatmentRecordVO, recordDO);

        // 设置操作人为当前用户
        if (recordDO.getOperatorId() == null) {
            Long currentUserId = Long.parseLong(StpUtil.getLoginIdAsString());
            recordDO.setOperatorId(currentUserId);
        }

        treatmentRecordMapper.insert(recordDO);
        return recordDO.getId();
    }

    @Override
    @Transactional
    public void updateTreatmentRecord(TreatmentRecordVO treatmentRecordVO) {
        TreatmentRecordDO recordDO = new TreatmentRecordDO();
        BeanUtils.copyProperties(treatmentRecordVO, recordDO);
        treatmentRecordMapper.updateById(recordDO);
    }

    @Override
    @Transactional
    public void deleteTreatmentRecord(Long id) {
        treatmentRecordMapper.deleteById(id);
    }

    private TreatmentRecordVO convertToVO(TreatmentRecordDO recordDO) {
        TreatmentRecordVO vo = new TreatmentRecordVO();
        BeanUtils.copyProperties(recordDO, vo);
        vo.setTreatmentTypeName(TREATMENT_TYPE_MAP.getOrDefault(recordDO.getTreatmentType(), recordDO.getTreatmentType()));
        vo.setTreatmentEffectName(TREATMENT_EFFECT_MAP.getOrDefault(recordDO.getTreatmentEffect(), recordDO.getTreatmentEffect()));
        return vo;
    }
}
