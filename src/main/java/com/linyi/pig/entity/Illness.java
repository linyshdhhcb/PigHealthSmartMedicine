package com.linyi.pig.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
* @Author: linyi
* @Date: 2025-02-25 17:38:38
* @ClassName: Illness
* @Version: 1.0
* @Description: 疾病
*/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("illness")
@Schema(name = "疾病")
public class Illness implements Serializable {


    private static final long serialVersionUID = 1L;

    /**
     * 疾病id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(name = "id",description = "疾病id",type = "int")
    private Integer id;

    /**
     * 疾病分类ID
     */
    @TableField("kind_id")
    @Schema(name = "kindId",description = "疾病分类ID",type = "int")
    private Integer kindId;

    /**
     * 疾病名字
     */
    @TableField("illness_name")
    @Schema(name = "illnessName",description = "疾病名字",type = "varchar")
    private String illnessName;

    /**
     * 诱发因素
     */
    @TableField("include_reason")
    @Schema(name = "includeReason",description = "诱发因素",type = "mediumtext")
    private String includeReason;

    /**
     * 疾病症状
     */
    @TableField("illness_symptom")
    @Schema(name = "illnessSymptom",description = "疾病症状",type = "mediumtext")
    private String illnessSymptom;

    /**
     * 特殊症状
     */
    @TableField("special_symptom")
    @Schema(name = "specialSymptom",description = "特殊症状",type = "mediumtext")
    private String specialSymptom;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @Schema(name = "createTime",description = "创建时间",type = "datetime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    @Schema(name = "updateTime",description = "更新时间",type = "datetime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
