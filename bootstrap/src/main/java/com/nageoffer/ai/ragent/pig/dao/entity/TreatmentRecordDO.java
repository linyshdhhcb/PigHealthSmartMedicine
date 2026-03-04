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
 * 治疗记录实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("t_treatment_record")
public class TreatmentRecordDO {

    /**
     * 治疗记录唯一标识
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 病例ID
     */
    private Long caseId;

    /**
     * 生猪ID
     */
    private Long pigId;

    /**
     * 治疗日期
     */
    private Date treatmentDate;

    /**
     * 操作人ID
     */
    private Long operatorId;

    /**
     * 治疗类型：MEDICATION-药物治疗，SURGERY-手术，NURSING-护理
     */
    private String treatmentType;

    /**
     * 使用药品（JSON格式）
     */
    private String drugsUsed;

    /**
     * 用药详情
     */
    private String dosageDetail;

    /**
     * 治疗效果：EFFECTIVE-有效，INEFFECTIVE-无效，IMPROVED-好转
     */
    private String treatmentEffect;

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
