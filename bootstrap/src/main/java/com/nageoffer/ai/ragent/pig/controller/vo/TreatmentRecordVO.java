package com.nageoffer.ai.ragent.pig.controller.vo;

import lombok.Data;

import java.util.Date;

/**
 * 治疗记录VO
 */
@Data
public class TreatmentRecordVO {

    /**
     * 治疗记录唯一标识
     */
    private Long id;

    /**
     * 病例ID
     */
    private Long caseId;

    /**
     * 病例编号
     */
    private String caseNumber;

    /**
     * 生猪ID
     */
    private Long pigId;

    /**
     * 生猪耳标号
     */
    private String earTagNumber;

    /**
     * 治疗日期
     */
    private Date treatmentDate;

    /**
     * 操作人ID
     */
    private Long operatorId;

    /**
     * 操作人姓名
     */
    private String operatorName;

    /**
     * 治疗类型
     */
    private String treatmentType;

    /**
     * 治疗类型名称
     */
    private String treatmentTypeName;

    /**
     * 使用药品（JSON格式）
     */
    private String drugsUsed;

    /**
     * 用药详情
     */
    private String dosageDetail;

    /**
     * 治疗效果
     */
    private String treatmentEffect;

    /**
     * 治疗效果名称
     */
    private String treatmentEffectName;

    /**
     * 副作用
     */
    private String sideEffects;

    /**
     * 备注
     */
    private String notes;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
