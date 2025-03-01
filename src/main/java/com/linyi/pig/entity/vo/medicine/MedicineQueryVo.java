package com.linyi.pig.entity.vo.medicine;

import com.baomidou.mybatisplus.annotation.TableField;
import com.linyi.pig.common.model.PageResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
* @Author: linyi
* @Date: 2025-02-25 17:38:38
* @ClassName: MedicineQueryVo
* @Version: 1.0
* @Description: 药品查询实体
*/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "药品查询实体")
public class MedicineQueryVo extends PageResponse implements Serializable {

    /**
     * 药的名字
     */
    @TableField("medicine_name")
    @Schema(name = "medicineName",description = "药的名字",type = "varchar")
    private String medicineName;

    /**
     * 关键字搜索
     */
    @TableField("keyword")
    @Schema(name = "keyword",description = "关键字搜索",type = "varchar")
    private String keyword;

    /**
     * 药的功效
     */
    @TableField("medicine_effect")
    @Schema(name = "medicineEffect",description = "药的功效",type = "mediumtext")
    private String medicineEffect;

    /**
     * 药的品牌
     */
    @TableField("medicine_brand")
    @Schema(name = "medicineBrand",description = "药的品牌",type = "varchar")
    private String medicineBrand;

    /**
     * 药的相互作用
     */
    @TableField("interaction")
    @Schema(name = "interaction",description = "药的相互作用",type = "mediumtext")
    private String interaction;

    /**
     * 禁忌
     */
    @TableField("taboo")
    @Schema(name = "taboo",description = "禁忌",type = "mediumtext")
    private String taboo;

    /**
     * 用法用量
     */
    @TableField("us_age")
    @Schema(name = "usAge",description = "用法用量",type = "mediumtext")
    private String usAge;

    /**
     * 药的类型，0西药，1中药，2中成药
     */
    @TableField("medicine_type")
    @Schema(name = "medicineType",description = "药的类型，0西药，1中药，2中成药",type = "int")
    private Integer medicineType;


    /**
     * 药的价格
     */
    @Schema(name = "medicinePriceMin",description = "药的价格最低",type = "decimal")
    private BigDecimal medicinePriceMin;
    /**
     * 药的价格
     */
    @Schema(name = "medicinePriceMax",description = "药的价格最高",type = "decimal")
    private BigDecimal medicinePriceMax;

}
