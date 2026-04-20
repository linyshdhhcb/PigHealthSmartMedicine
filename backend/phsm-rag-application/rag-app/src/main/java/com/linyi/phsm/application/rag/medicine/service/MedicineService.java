package com.linyi.phsm.application.rag.medicine.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.linyi.phsm.domain.rag.model.request.MedicineCreateRequest;
import com.linyi.phsm.domain.rag.model.request.MedicinePageRequest;
import com.linyi.phsm.domain.rag.model.request.MedicineUpdateRequest;
import com.linyi.phsm.domain.rag.model.vo.MedicineVO;
import org.springframework.web.multipart.MultipartFile;

public interface MedicineService {

    String create(MedicineCreateRequest requestParam);

    void update(String id, MedicineUpdateRequest requestParam);

    void delete(String id);

    MedicineVO queryById(String id);

    IPage<MedicineVO> pageQuery(MedicinePageRequest requestParam);

    String uploadImage(MultipartFile file);
}
