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
* @ClassName: IllnessMedicineUpdateVo
* @Version: 1.0
* @Description: 疾病-药物修改实体
*/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "疾病-药物修改实体")
public class IllnessMedicineUpdateVo implements Serializable {
    /**
    * 主键ID
    */
    private Serializable id;
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
