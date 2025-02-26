package com.linyi.pig.entity.vo.illnessMedicine;

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
* @ClassName: IllnessMedicineAddVo
* @Version: 1.0
* @Description: 疾病-药物新增实体
*/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "疾病-药物新增实体")
public class IllnessMedicineAddVo implements Serializable {
    /**
     * 病id
     */
    @TableField("illness_id")
    @Schema(name = "illnessId",description = "病id",type = "int")
    private Integer illnessId;

    /**
     * 药品id
     */
    @TableField("medicine_id")
    @Schema(name = "medicineId",description = "药品id",type = "int")
    private Integer medicineId;
}
