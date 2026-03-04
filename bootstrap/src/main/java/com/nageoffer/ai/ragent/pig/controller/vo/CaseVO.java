package com.nageoffer.ai.ragent.pig.controller.vo;

import lombok.Data;

import java.util.Date;

/**
 * 病例信息VO
 */
@Data
public class CaseVO {

    /**
     * 病例唯一标识
     */
    private Long id;

    /**
     * 病例编号
     */
    private String caseNumber;

    /**
     * 生猪标识
     */
    private Long pigId;

    /**
     * 生猪耳标号
     */
    private String pigEarTag;

    /**
     * 创建用户ID
     */
    private Long userId;

    /**
     * 创建用户姓名
     */
    private String userName;

    /**
     * 诊断兽医ID
     */
    private Long veterinarianId;

    /**
     * 诊断兽医姓名
     */
    private String veterinarianName;

    /**
     * 症状描述
     */
    private String symptoms;

    /**
     * 诊断结果
     */
    private String diagnosis;

    /**
     * 疾病名称
     */
    private String diseaseName;

    /**
     * 治疗方案
     */
    private String treatmentPlan;

    /**
     * 处方药品（JSON格式）
     */
    private String prescribedDrugs;

    /**
     * 病例状态
     */
    private String status;

    /**
     * 病例状态名称
     */
    private String statusName;

    /**
     * 诊断时间
     */
    private Date diagnosisTime;

    /**
     * 治疗开始时间
     */
    private Date treatmentStartTime;

    /**
     * 治疗结束时间
     */
    private Date treatmentEndTime;

    /**
     * 随访信息
     */
    private String followUpInfo;

    /**
     * 关联生猪（JSON数组）
     */
    private String relatedPigs;

    /**
     * 关联病例（JSON数组）
     */
    private String relatedCases;

    /**
     * AI诊断结果
     */
    private String aiDiagnosis;

    /**
     * 关联的对话ID
     */
    private String conversationId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
