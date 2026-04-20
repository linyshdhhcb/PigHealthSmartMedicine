package com.linyi.phsm.application.rag.illness.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.linyi.phsm.domain.rag.model.request.IllnessMedicineBatchCreateRequest;
import com.linyi.phsm.domain.rag.model.request.IllnessMedicineBatchDeleteRequest;
import com.linyi.phsm.domain.rag.model.request.IllnessMedicineCreateRequest;
import com.linyi.phsm.domain.rag.model.request.IllnessMedicinePageRequest;
import com.linyi.phsm.domain.rag.model.vo.IllnessMedicineVO;
import com.linyi.phsm.domain.rag.model.vo.IllnessVO;
import com.linyi.phsm.domain.rag.model.vo.MedicineVO;

import java.util.List;

public interface IllnessMedicineService {

    String create(IllnessMedicineCreateRequest requestParam);

    void delete(String id);

    void batchCreate(IllnessMedicineBatchCreateRequest requestParam);

    void batchDelete(IllnessMedicineBatchDeleteRequest requestParam);

    IllnessMedicineVO queryById(String id);

    IPage<IllnessMedicineVO> pageQuery(IllnessMedicinePageRequest requestParam);

    List<MedicineVO> listMedicinesByIllness(String illnessId);

    List<IllnessVO> listIllnessesByMedicine(String medicineId);
}
