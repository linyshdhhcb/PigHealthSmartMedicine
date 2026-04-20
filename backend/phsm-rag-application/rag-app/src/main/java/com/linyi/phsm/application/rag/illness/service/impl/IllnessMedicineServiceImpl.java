package com.linyi.phsm.application.rag.illness.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.linyi.phsm.application.rag.illness.service.IllnessMedicineService;
import com.linyi.phsm.domain.rag.model.entity.IllnessDO;
import com.linyi.phsm.domain.rag.model.entity.IllnessKindDO;
import com.linyi.phsm.domain.rag.model.entity.IllnessMedicineDO;
import com.linyi.phsm.domain.rag.model.entity.MedicineDO;
import com.linyi.phsm.domain.rag.model.request.IllnessMedicineBatchCreateRequest;
import com.linyi.phsm.domain.rag.model.request.IllnessMedicineBatchDeleteRequest;
import com.linyi.phsm.domain.rag.model.request.IllnessMedicineCreateRequest;
import com.linyi.phsm.domain.rag.model.request.IllnessMedicinePageRequest;
import com.linyi.phsm.domain.rag.model.vo.IllnessMedicineVO;
import com.linyi.phsm.domain.rag.model.vo.IllnessVO;
import com.linyi.phsm.domain.rag.model.vo.MedicineVO;
import com.linyi.phsm.framework.context.UserContext;
import com.linyi.phsm.framework.exception.ClientException;
import com.linyi.phsm.infrastructure.persistence.rag.mapper.IllnessKindMapper;
import com.linyi.phsm.infrastructure.persistence.rag.mapper.IllnessMapper;
import com.linyi.phsm.infrastructure.persistence.rag.mapper.IllnessMedicineMapper;
import com.linyi.phsm.infrastructure.persistence.rag.mapper.MedicineMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IllnessMedicineServiceImpl implements IllnessMedicineService {

    private final IllnessMedicineMapper illnessMedicineMapper;
    private final IllnessMapper illnessMapper;
    private final MedicineMapper medicineMapper;
    private final IllnessKindMapper illnessKindMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String create(IllnessMedicineCreateRequest requestParam) {
        assertNotDuplicate(requestParam.getIllnessId(), requestParam.getMedicineId());
        IllnessDO ill = loadIllness(requestParam.getIllnessId());
        loadMedicine(requestParam.getMedicineId());
        String uname = UserContext.requireUser().getUsername();
        IllnessMedicineDO row = IllnessMedicineDO.builder()
                .illnessId(ill.getId())
                .medicineId(requestParam.getMedicineId())
                .createdBy(uname)
                .updatedBy(uname)
                .build();
        illnessMedicineMapper.insert(row);
        return row.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        IllnessMedicineDO row = loadRel(id);
        illnessMedicineMapper.deleteById(row.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchCreate(IllnessMedicineBatchCreateRequest requestParam) {
        String illnessId = requestParam.getIllnessId();
        loadIllness(illnessId);
        String uname = UserContext.requireUser().getUsername();
        for (String medicineId : requestParam.getMedicineIds()) {
            if (StrUtil.isBlank(medicineId)) {
                continue;
            }
            loadMedicine(medicineId);
            Long cnt = illnessMedicineMapper.selectCount(
                    Wrappers.lambdaQuery(IllnessMedicineDO.class)
                            .eq(IllnessMedicineDO::getIllnessId, illnessId)
                            .eq(IllnessMedicineDO::getMedicineId, medicineId)
                            .eq(IllnessMedicineDO::getDeleted, 0)
            );
            if (cnt != null && cnt > 0) {
                continue;
            }
            IllnessMedicineDO row = IllnessMedicineDO.builder()
                    .illnessId(illnessId)
                    .medicineId(medicineId)
                    .createdBy(uname)
                    .updatedBy(uname)
                    .build();
            illnessMedicineMapper.insert(row);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(IllnessMedicineBatchDeleteRequest requestParam) {
        if (requestParam.getIds() == null || requestParam.getIds().isEmpty()) {
            return;
        }
        illnessMedicineMapper.deleteBatchIds(requestParam.getIds());
    }

    @Override
    public IllnessMedicineVO queryById(String id) {
        IllnessMedicineDO row = loadRel(id);
        return enrich(List.of(row)).get(0);
    }

    @Override
    public IPage<IllnessMedicineVO> pageQuery(IllnessMedicinePageRequest requestParam) {
        String illnessId = StrUtil.trim(requestParam.getIllnessId());
        String medicineId = StrUtil.trim(requestParam.getMedicineId());
        Page<IllnessMedicineDO> page = new Page<>(requestParam.getCurrent(), requestParam.getSize());
        IPage<IllnessMedicineDO> result = illnessMedicineMapper.selectPage(
                page,
                Wrappers.lambdaQuery(IllnessMedicineDO.class)
                        .eq(IllnessMedicineDO::getDeleted, 0)
                        .eq(StrUtil.isNotBlank(illnessId), IllnessMedicineDO::getIllnessId, illnessId)
                        .eq(StrUtil.isNotBlank(medicineId), IllnessMedicineDO::getMedicineId, medicineId)
                        .orderByDesc(IllnessMedicineDO::getCreateTime)
        );
        List<IllnessMedicineVO> vos = enrich(result.getRecords());
        Page<IllnessMedicineVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        voPage.setRecords(vos);
        return voPage;
    }

    @Override
    public List<MedicineVO> listMedicinesByIllness(String illnessId) {
        loadIllness(illnessId);
        List<IllnessMedicineDO> rels = illnessMedicineMapper.selectList(
                Wrappers.lambdaQuery(IllnessMedicineDO.class)
                        .eq(IllnessMedicineDO::getIllnessId, illnessId)
                        .eq(IllnessMedicineDO::getDeleted, 0)
        );
        if (rels.isEmpty()) {
            return List.of();
        }
        Set<String> mids = rels.stream().map(IllnessMedicineDO::getMedicineId).collect(Collectors.toSet());
        List<MedicineDO> medicines = medicineMapper.selectList(
                Wrappers.lambdaQuery(MedicineDO.class)
                        .in(MedicineDO::getId, mids)
                        .eq(MedicineDO::getDeleted, 0)
        );
        Map<String, MedicineDO> map = medicines.stream().collect(Collectors.toMap(MedicineDO::getId, m -> m));
        List<MedicineVO> list = new ArrayList<>();
        for (IllnessMedicineDO rel : rels) {
            MedicineDO m = map.get(rel.getMedicineId());
            if (m != null) {
                list.add(toMedicineVo(m));
            }
        }
        return list;
    }

    @Override
    public List<IllnessVO> listIllnessesByMedicine(String medicineId) {
        loadMedicine(medicineId);
        List<IllnessMedicineDO> rels = illnessMedicineMapper.selectList(
                Wrappers.lambdaQuery(IllnessMedicineDO.class)
                        .eq(IllnessMedicineDO::getMedicineId, medicineId)
                        .eq(IllnessMedicineDO::getDeleted, 0)
        );
        if (rels.isEmpty()) {
            return List.of();
        }
        Set<String> iids = rels.stream().map(IllnessMedicineDO::getIllnessId).collect(Collectors.toSet());
        List<IllnessDO> illnesses = illnessMapper.selectList(
                Wrappers.lambdaQuery(IllnessDO.class)
                        .in(IllnessDO::getId, iids)
                        .eq(IllnessDO::getDeleted, 0)
        );
        Set<String> kindIds = illnesses.stream().map(IllnessDO::getKindId).filter(StrUtil::isNotBlank).collect(Collectors.toSet());
        Map<String, String> kindNames = resolveKindNames(kindIds);
        Map<String, IllnessDO> imap = illnesses.stream().collect(Collectors.toMap(IllnessDO::getId, i -> i));
        List<IllnessVO> list = new ArrayList<>();
        for (IllnessMedicineDO rel : rels) {
            IllnessDO i = imap.get(rel.getIllnessId());
            if (i != null) {
                list.add(toIllnessVo(i, kindNames.get(i.getKindId())));
            }
        }
        return list;
    }

    private void assertNotDuplicate(String illnessId, String medicineId) {
        Long cnt = illnessMedicineMapper.selectCount(
                Wrappers.lambdaQuery(IllnessMedicineDO.class)
                        .eq(IllnessMedicineDO::getIllnessId, illnessId)
                        .eq(IllnessMedicineDO::getMedicineId, medicineId)
                        .eq(IllnessMedicineDO::getDeleted, 0)
        );
        Assert.isTrue(cnt == null || cnt == 0, () -> new ClientException("该疾病与药品已关联"));
    }

    private IllnessDO loadIllness(String id) {
        IllnessDO row = illnessMapper.selectOne(
                Wrappers.lambdaQuery(IllnessDO.class)
                        .eq(IllnessDO::getId, id)
                        .eq(IllnessDO::getDeleted, 0)
        );
        Assert.notNull(row, () -> new ClientException("疾病不存在"));
        return row;
    }

    private MedicineDO loadMedicine(String id) {
        MedicineDO row = medicineMapper.selectOne(
                Wrappers.lambdaQuery(MedicineDO.class)
                        .eq(MedicineDO::getId, id)
                        .eq(MedicineDO::getDeleted, 0)
        );
        Assert.notNull(row, () -> new ClientException("药品不存在"));
        return row;
    }

    private IllnessMedicineDO loadRel(String id) {
        IllnessMedicineDO row = illnessMedicineMapper.selectOne(
                Wrappers.lambdaQuery(IllnessMedicineDO.class)
                        .eq(IllnessMedicineDO::getId, id)
                        .eq(IllnessMedicineDO::getDeleted, 0)
        );
        Assert.notNull(row, () -> new ClientException("关联不存在"));
        return row;
    }

    private List<IllnessMedicineVO> enrich(List<IllnessMedicineDO> rows) {
        if (rows.isEmpty()) {
            return List.of();
        }
        Set<String> iids = rows.stream().map(IllnessMedicineDO::getIllnessId).collect(Collectors.toSet());
        Set<String> mids = rows.stream().map(IllnessMedicineDO::getMedicineId).collect(Collectors.toSet());
        Map<String, String> illnessNames = illnessMapper.selectList(
                        Wrappers.lambdaQuery(IllnessDO.class).in(IllnessDO::getId, iids))
                .stream()
                .collect(Collectors.toMap(IllnessDO::getId, IllnessDO::getIllnessName, (a, b) -> a));
        Map<String, String> medicineNames = medicineMapper.selectList(
                        Wrappers.lambdaQuery(MedicineDO.class).in(MedicineDO::getId, mids))
                .stream()
                .collect(Collectors.toMap(MedicineDO::getId, MedicineDO::getMedicineName, (a, b) -> a));
        List<IllnessMedicineVO> list = new ArrayList<>();
        for (IllnessMedicineDO r : rows) {
            list.add(IllnessMedicineVO.builder()
                    .id(r.getId())
                    .illnessId(r.getIllnessId())
                    .illnessName(illnessNames.get(r.getIllnessId()))
                    .medicineId(r.getMedicineId())
                    .medicineName(medicineNames.get(r.getMedicineId()))
                    .createdBy(r.getCreatedBy())
                    .createTime(r.getCreateTime())
                    .build());
        }
        return list;
    }

    private Map<String, String> resolveKindNames(Set<String> kindIds) {
        if (kindIds.isEmpty()) {
            return Map.of();
        }
        List<IllnessKindDO> kinds = illnessKindMapper.selectList(
                Wrappers.lambdaQuery(IllnessKindDO.class)
                        .in(IllnessKindDO::getId, kindIds)
                        .eq(IllnessKindDO::getDeleted, 0)
        );
        Map<String, String> map = new HashMap<>();
        for (IllnessKindDO k : kinds) {
            map.put(k.getId(), k.getName());
        }
        return map;
    }

    private MedicineVO toMedicineVo(MedicineDO row) {
        return MedicineVO.builder()
                .id(row.getId())
                .medicineName(row.getMedicineName())
                .keyword(row.getKeyword())
                .medicineEffect(row.getMedicineEffect())
                .medicineBrand(row.getMedicineBrand())
                .interaction(row.getInteraction())
                .taboo(row.getTaboo())
                .usAge(row.getUsAge())
                .medicineType(row.getMedicineType())
                .imgPath(row.getImgPath())
                .medicinePrice(row.getMedicinePrice())
                .createdBy(row.getCreatedBy())
                .updatedBy(row.getUpdatedBy())
                .createTime(row.getCreateTime())
                .updateTime(row.getUpdateTime())
                .build();
    }

    private IllnessVO toIllnessVo(IllnessDO row, String kindName) {
        return IllnessVO.builder()
                .id(row.getId())
                .kindId(row.getKindId())
                .kindName(kindName)
                .illnessName(row.getIllnessName())
                .includeReason(row.getIncludeReason())
                .illnessSymptom(row.getIllnessSymptom())
                .specialSymptom(row.getSpecialSymptom())
                .createdBy(row.getCreatedBy())
                .updatedBy(row.getUpdatedBy())
                .createTime(row.getCreateTime())
                .updateTime(row.getUpdateTime())
                .build();
    }
}
