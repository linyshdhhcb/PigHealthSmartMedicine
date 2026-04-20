package com.linyi.phsm.domain.rag.model.entity;

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
 * 示例问题实体（用于欢迎页展示）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("t_sample_question")
public class SampleQuestionDO {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 展示标题（可选）
     */
    private String title;

    /**
     * 描述或提示（可选）
     */
    private String description;

    /**
     * 示例问题内容
     */
    private String question;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableLogic
    private Integer deleted;
}
