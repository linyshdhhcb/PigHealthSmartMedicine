package com.linyi.phsm.application.rag.illness.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.linyi.phsm.application.rag.illness.service.IllnessKindService;
import com.linyi.phsm.domain.rag.model.entity.IllnessKindDO;
import com.linyi.phsm.domain.rag.model.request.IllnessKindCreateRequest;
import com.linyi.phsm.domain.rag.model.request.IllnessKindPageRequest;
import com.linyi.phsm.domain.rag.model.request.IllnessKindUpdateRequest;
import com.linyi.phsm.domain.rag.model.vo.IllnessKindVO;
import com.linyi.phsm.framework.context.UserContext;
import com.linyi.phsm.framework.exception.ClientException;
import com.linyi.phsm.infrastructure.persistence.rag.mapper.IllnessKindMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IllnessKindServiceImpl implements IllnessKindService {

    private final IllnessKindMapper illnessKindMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String create(IllnessKindCreateRequest requestParam) {
        String uname = UserContext.requireUser().getUsername();
        String name = StrUtil.trim(requestParam.getName());
        Assert.notBlank(name, () -> new ClientException("分类名称不能为空"));
        IllnessKindDO row = IllnessKindDO.builder()
                .name(name)
                .info(StrUtil.trimToNull(requestParam.getInfo()))
                .createdBy(uname)
                .updatedBy(uname)
                .build();
        illnessKindMapper.insert(row);
        return row.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(String id, IllnessKindUpdateRequest requestParam) {
        IllnessKindDO row = load(id);
        String name = StrUtil.trim(requestParam.getName());
        Assert.notBlank(name, () -> new ClientException("分类名称不能为空"));
        row.setName(name);
        row.setInfo(StrUtil.trimToNull(requestParam.getInfo()));
        row.setUpdatedBy(UserContext.requireUser().getUsername());
        illnessKindMapper.updateById(row);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        IllnessKindDO row = load(id);
        illnessKindMapper.deleteById(row.getId());
    }

    @Override
    public IllnessKindVO queryById(String id) {
        return toVo(load(id));
    }

    @Override
    public IPage<IllnessKindVO> pageQuery(IllnessKindPageRequest requestParam) {
        String name = StrUtil.trim(requestParam.getName());
        Page<IllnessKindDO> page = new Page<>(requestParam.getCurrent(), requestParam.getSize());
        IPage<IllnessKindDO> result = illnessKindMapper.selectPage(
                page,
                Wrappers.lambdaQuery(IllnessKindDO.class)
                        .eq(IllnessKindDO::getDeleted, 0)
                        .like(StrUtil.isNotBlank(name), IllnessKindDO::getName, name)
                        .orderByDesc(IllnessKindDO::getUpdateTime)
        );
        return result.convert(this::toVo);
    }

    private IllnessKindDO load(String id) {
        IllnessKindDO row = illnessKindMapper.selectOne(
                Wrappers.lambdaQuery(IllnessKindDO.class)
                        .eq(IllnessKindDO::getId, id)
                        .eq(IllnessKindDO::getDeleted, 0)
        );
        Assert.notNull(row, () -> new ClientException("疾病分类不存在"));
        return row;
    }

    private IllnessKindVO toVo(IllnessKindDO row) {
        return IllnessKindVO.builder()
                .id(row.getId())
                .name(row.getName())
                .info(row.getInfo())
                .createdBy(row.getCreatedBy())
                .updatedBy(row.getUpdatedBy())
                .createTime(row.getCreateTime())
                .updateTime(row.getUpdateTime())
                .build();
    }
}
