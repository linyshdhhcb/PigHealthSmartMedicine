package com.nageoffer.ai.ragent.pig.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nageoffer.ai.ragent.pig.controller.vo.DrugVO;
import com.nageoffer.ai.ragent.pig.dao.entity.DrugDO;
import com.nageoffer.ai.ragent.pig.dao.mapper.DrugMapper;
import com.nageoffer.ai.ragent.pig.service.DrugService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 兽药信息服务实现类
 */
@Service
@RequiredArgsConstructor
public class DrugServiceImpl implements DrugService {

    private final DrugMapper drugMapper;

    private static final Map<String, String> DRUG_TYPE_MAP = new HashMap<>();
    private static final Map<String, String> DOSAGE_FORM_MAP = new HashMap<>();

    static {
        DRUG_TYPE_MAP.put("CHINESE", "中药");
        DRUG_TYPE_MAP.put("WESTERN", "西药");

        DOSAGE_FORM_MAP.put("TABLET", "片剂");
        DOSAGE_FORM_MAP.put("INJECTION", "注射剂");
        DOSAGE_FORM_MAP.put("POWDER", "粉剂");
        DOSAGE_FORM_MAP.put("LIQUID", "液体");
    }

    @Override
    public Page<DrugVO> listDrugs(String name, String drugType, Integer pageNum, Integer pageSize) {
        Page<DrugDO> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<DrugDO> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(name)) {
            wrapper.like(DrugDO::getName, name);
        }
        if (StringUtils.hasText(drugType)) {
            wrapper.eq(DrugDO::getDrugType, drugType);
        }

        wrapper.orderByDesc(DrugDO::getCreateTime);
        Page<DrugDO> drugPage = drugMapper.selectPage(page, wrapper);

        Page<DrugVO> voPage = new Page<>(drugPage.getCurrent(), drugPage.getSize(), drugPage.getTotal());
        List<DrugVO> voList = drugPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);

        return voPage;
    }

    @Override
    public DrugVO getDrugById(Long id) {
        DrugDO drugDO = drugMapper.selectById(id);
        if (drugDO == null) {
            return null;
        }
        return convertToVO(drugDO);
    }

    @Override
    public Page<DrugVO> searchDrugs(String keyword, Integer pageNum, Integer pageSize) {
        Page<DrugDO> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<DrugDO> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(keyword)) {
            wrapper.like(DrugDO::getName, keyword)
                    .or()
                    .like(DrugDO::getIndication, keyword);
        }

        Page<DrugDO> drugPage = drugMapper.selectPage(page, wrapper);

        Page<DrugVO> voPage = new Page<>(drugPage.getCurrent(), drugPage.getSize(), drugPage.getTotal());
        List<DrugVO> voList = drugPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);

        return voPage;
    }

    private DrugVO convertToVO(DrugDO drugDO) {
        DrugVO vo = new DrugVO();
        BeanUtils.copyProperties(drugDO, vo);
        vo.setDrugTypeName(DRUG_TYPE_MAP.getOrDefault(drugDO.getDrugType(), drugDO.getDrugType()));
        vo.setDosageFormName(DOSAGE_FORM_MAP.getOrDefault(drugDO.getDosageForm(), drugDO.getDosageForm()));
        return vo;
    }
}
