package com.linyi.phsm.application.rag.illness.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.linyi.phsm.application.rag.illness.service.IllnessService;
import com.linyi.phsm.domain.rag.model.entity.IllnessDO;
import com.linyi.phsm.domain.rag.model.entity.IllnessKindDO;
import com.linyi.phsm.domain.rag.model.request.IllnessCreateRequest;
import com.linyi.phsm.domain.rag.model.request.IllnessPageRequest;
import com.linyi.phsm.domain.rag.model.request.IllnessUpdateRequest;
import com.linyi.phsm.domain.rag.model.vo.IllnessVO;
import com.linyi.phsm.framework.context.UserContext;
import com.linyi.phsm.framework.exception.ClientException;
import com.linyi.phsm.infrastructure.persistence.rag.mapper.IllnessKindMapper;
import com.linyi.phsm.infrastructure.persistence.rag.mapper.IllnessMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class IllnessServiceImpl implements IllnessService {

    private final IllnessMapper illnessMapper;
    private final IllnessKindMapper illnessKindMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String create(IllnessCreateRequest requestParam) {
        assertKindExists(requestParam.getKindId());
        String uname = UserContext.requireUser().getUsername();
        IllnessDO row = IllnessDO.builder()
                .kindId(requestParam.getKindId())
                .illnessName(StrUtil.trim(requestParam.getIllnessName()))
                .includeReason(StrUtil.trimToNull(requestParam.getIncludeReason()))
                .illnessSymptom(StrUtil.trimToNull(requestParam.getIllnessSymptom()))
                .specialSymptom(StrUtil.trimToNull(requestParam.getSpecialSymptom()))
                .createdBy(uname)
                .updatedBy(uname)
                .build();
        illnessMapper.insert(row);
        return row.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(String id, IllnessUpdateRequest requestParam) {
        IllnessDO row = load(id);
        assertKindExists(requestParam.getKindId());
        row.setKindId(requestParam.getKindId());
        row.setIllnessName(StrUtil.trim(requestParam.getIllnessName()));
        row.setIncludeReason(StrUtil.trimToNull(requestParam.getIncludeReason()));
        row.setIllnessSymptom(StrUtil.trimToNull(requestParam.getIllnessSymptom()));
        row.setSpecialSymptom(StrUtil.trimToNull(requestParam.getSpecialSymptom()));
        row.setUpdatedBy(UserContext.requireUser().getUsername());
        illnessMapper.updateById(row);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        IllnessDO row = load(id);
        illnessMapper.deleteById(row.getId());
    }

    @Override
    public IllnessVO queryById(String id) {
        IllnessDO row = load(id);
        return toVo(row, resolveKindNames(Set.of(row.getKindId())).get(row.getKindId()));
    }

    @Override
    public IPage<IllnessVO> pageQuery(IllnessPageRequest requestParam) {
        String kindId = StrUtil.trim(requestParam.getKindId());
        String kw = StrUtil.trim(requestParam.getKeyword());
        Page<IllnessDO> page = new Page<>(requestParam.getCurrent(), requestParam.getSize());
        IPage<IllnessDO> result = illnessMapper.selectPage(
                page,
                Wrappers.lambdaQuery(IllnessDO.class)
                        .eq(IllnessDO::getDeleted, 0)
                        .eq(StrUtil.isNotBlank(kindId), IllnessDO::getKindId, kindId)
                        .and(StrUtil.isNotBlank(kw), w -> w
                                .like(IllnessDO::getIllnessName, kw)
                                .or()
                                .like(IllnessDO::getIllnessSymptom, kw))
                        .orderByDesc(IllnessDO::getUpdateTime)
        );
        Set<String> kindIds = new HashSet<>();
        for (IllnessDO r : result.getRecords()) {
            if (StrUtil.isNotBlank(r.getKindId())) {
                kindIds.add(r.getKindId());
            }
        }
        Map<String, String> kindNames = resolveKindNames(kindIds);
        return result.convert(i -> toVo(i, kindNames.get(i.getKindId())));
    }

    private void assertKindExists(String kindId) {
        IllnessKindDO k = illnessKindMapper.selectOne(
                Wrappers.lambdaQuery(IllnessKindDO.class)
                        .eq(IllnessKindDO::getId, kindId)
                        .eq(IllnessKindDO::getDeleted, 0)
        );
        Assert.notNull(k, () -> new ClientException("疾病分类不存在"));
    }

    private Map<String, String> resolveKindNames(Set<String> kindIds) {
        if (kindIds.isEmpty()) {
            return Map.of();
        }
        List<IllnessKindDO> list = illnessKindMapper.selectList(
                Wrappers.lambdaQuery(IllnessKindDO.class)
                        .in(IllnessKindDO::getId, kindIds)
                        .eq(IllnessKindDO::getDeleted, 0)
        );
        Map<String, String> map = new HashMap<>();
        for (IllnessKindDO k : list) {
            map.put(k.getId(), k.getName());
        }
        return map;
    }

    private IllnessDO load(String id) {
        IllnessDO row = illnessMapper.selectOne(
                Wrappers.lambdaQuery(IllnessDO.class)
                        .eq(IllnessDO::getId, id)
                        .eq(IllnessDO::getDeleted, 0)
        );
        Assert.notNull(row, () -> new ClientException("疾病不存在"));
        return row;
    }

    private IllnessVO toVo(IllnessDO row, String kindName) {
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
