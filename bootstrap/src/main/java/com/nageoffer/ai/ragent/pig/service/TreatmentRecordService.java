package com.nageoffer.ai.ragent.pig.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nageoffer.ai.ragent.pig.controller.vo.TreatmentRecordVO;

/**
 * 治疗记录服务接口
 */
public interface TreatmentRecordService {

    /**
     * 分页查询治疗记录列表
     */
    Page<TreatmentRecordVO> listTreatmentRecords(Long caseId, Long pigId, Integer pageNum, Integer pageSize);

    /**
     * 根据ID查询治疗记录详情
     */
    TreatmentRecordVO getTreatmentRecordById(Long id);

    /**
     * 创建治疗记录
     */
    Long createTreatmentRecord(TreatmentRecordVO treatmentRecordVO);

    /**
     * 更新治疗记录
     */
    void updateTreatmentRecord(TreatmentRecordVO treatmentRecordVO);

    /**
     * 删除治疗记录
     */
    void deleteTreatmentRecord(Long id);
}
