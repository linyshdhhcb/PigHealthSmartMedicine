package com.nageoffer.ai.ragent.pig.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 病例信息实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("t_case")
public class CaseDO {

    /**
     * 病例唯一标识
     */
    @TableId(type = IdType.ASSIGN_ID)
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
     * 创建用户ID（养殖户或兽医）
     */
    private Long userId;

    /**
     * 诊断兽医ID
     */
    private Long veterinarianId;

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
     * 病例状态：UNTREATED-未治疗，TREATING-治疗中，CURED-已治愈，DEAD-死亡
     */
    private String status;

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
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer deleted;
}
