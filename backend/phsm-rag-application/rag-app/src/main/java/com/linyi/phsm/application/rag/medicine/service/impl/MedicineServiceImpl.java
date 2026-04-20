package com.linyi.phsm.application.rag.medicine.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.linyi.phsm.application.rag.medicine.service.MedicineService;
import com.linyi.phsm.application.rag.service.FileStorageService;
import com.linyi.phsm.domain.rag.model.dto.StoredFileDTO;
import com.linyi.phsm.domain.rag.model.entity.MedicineDO;
import com.linyi.phsm.domain.rag.model.request.MedicineCreateRequest;
import com.linyi.phsm.domain.rag.model.request.MedicinePageRequest;
import com.linyi.phsm.domain.rag.model.request.MedicineUpdateRequest;
import com.linyi.phsm.domain.rag.model.vo.MedicineVO;
import com.linyi.phsm.framework.context.UserContext;
import com.linyi.phsm.framework.exception.ClientException;
import com.linyi.phsm.infrastructure.persistence.rag.mapper.MedicineMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class MedicineServiceImpl implements MedicineService {

    private static final String MEDICINE_IMAGE_BUCKET = "medicine-images";

    private final MedicineMapper medicineMapper;
    private final FileStorageService fileStorageService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String create(MedicineCreateRequest requestParam) {
        assertMedicineType(requestParam.getMedicineType());
        String uname = UserContext.requireUser().getUsername();
        MedicineDO row = MedicineDO.builder()
                .medicineName(StrUtil.trim(requestParam.getMedicineName()))
                .keyword(StrUtil.trimToNull(requestParam.getKeyword()))
                .medicineBrand(StrUtil.trimToNull(requestParam.getMedicineBrand()))
                .medicineType(requestParam.getMedicineType())
                .medicinePrice(requestParam.getMedicinePrice() != null ? requestParam.getMedicinePrice() : BigDecimal.ZERO)
                .medicineEffect(StrUtil.trimToNull(requestParam.getMedicineEffect()))
                .interaction(StrUtil.trimToNull(requestParam.getInteraction()))
                .taboo(StrUtil.trimToNull(requestParam.getTaboo()))
                .usAge(StrUtil.trimToNull(requestParam.getUsAge()))
                .imgPath(StrUtil.trimToNull(requestParam.getImgPath()))
                .createdBy(uname)
                .updatedBy(uname)
                .build();
        medicineMapper.insert(row);
        return row.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(String id, MedicineUpdateRequest requestParam) {
        MedicineDO row = load(id);
        assertMedicineType(requestParam.getMedicineType());
        row.setMedicineName(StrUtil.trim(requestParam.getMedicineName()));
        row.setKeyword(StrUtil.trimToNull(requestParam.getKeyword()));
        row.setMedicineBrand(StrUtil.trimToNull(requestParam.getMedicineBrand()));
        row.setMedicineType(requestParam.getMedicineType());
        row.setMedicinePrice(requestParam.getMedicinePrice() != null ? requestParam.getMedicinePrice() : BigDecimal.ZERO);
        row.setMedicineEffect(StrUtil.trimToNull(requestParam.getMedicineEffect()));
        row.setInteraction(StrUtil.trimToNull(requestParam.getInteraction()));
        row.setTaboo(StrUtil.trimToNull(requestParam.getTaboo()));
        row.setUsAge(StrUtil.trimToNull(requestParam.getUsAge()));
        row.setImgPath(StrUtil.trimToNull(requestParam.getImgPath()));
        row.setUpdatedBy(UserContext.requireUser().getUsername());
        medicineMapper.updateById(row);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        MedicineDO row = load(id);
        medicineMapper.deleteById(row.getId());
    }

    @Override
    public MedicineVO queryById(String id) {
        return toVo(load(id));
    }

    @Override
    public IPage<MedicineVO> pageQuery(MedicinePageRequest requestParam) {
        String kw = StrUtil.trim(requestParam.getKeyword());
        Integer type = requestParam.getMedicineType();
        Page<MedicineDO> page = new Page<>(requestParam.getCurrent(), requestParam.getSize());
        IPage<MedicineDO> result = medicineMapper.selectPage(
                page,
                Wrappers.lambdaQuery(MedicineDO.class)
                        .eq(MedicineDO::getDeleted, 0)
                        .eq(type != null, MedicineDO::getMedicineType, type)
                        .and(StrUtil.isNotBlank(kw), w -> w
                                .like(MedicineDO::getMedicineName, kw)
                                .or()
                                .like(MedicineDO::getKeyword, kw))
                        .orderByDesc(MedicineDO::getUpdateTime)
        );
        return result.convert(this::toVo);
    }

    @Override
    public String uploadImage(MultipartFile file) {
        Assert.notNull(file, () -> new ClientException("文件不能为空"));
        Assert.isTrue(!file.isEmpty(), () -> new ClientException("文件不能为空"));
        StoredFileDTO stored = fileStorageService.upload(MEDICINE_IMAGE_BUCKET, file);
        return stored.getUrl();
    }

    private void assertMedicineType(Integer t) {
        Assert.isTrue(t != null && t >= 0 && t <= 2, () -> new ClientException("药品类型不合法"));
    }

    private MedicineDO load(String id) {
        MedicineDO row = medicineMapper.selectOne(
                Wrappers.lambdaQuery(MedicineDO.class)
                        .eq(MedicineDO::getId, id)
                        .eq(MedicineDO::getDeleted, 0)
        );
        Assert.notNull(row, () -> new ClientException("药品不存在"));
        return row;
    }

    private MedicineVO toVo(MedicineDO row) {
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
}
