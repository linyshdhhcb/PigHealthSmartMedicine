package com.linyi.pig.entity.vo.illness;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
* @Author: linyi
* @Date: 2025-02-25 17:38:38
* @ClassName: IllnessAddVo
* @Version: 1.0
* @Description: 疾病新增实体
*/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "疾病新增实体")
public class IllnessAddVo implements Serializable {

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

}
