package com.linyi.phsm.domain.rag.model.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MedicineUpdateRequest {

    @NotBlank(message = "药品名称不能为空")
    @Size(max = 100, message = "药品名称最长100字符")
    private String medicineName;

    @Size(max = 255, message = "关键字最长255字符")
    private String keyword;

    @Size(max = 255, message = "品牌最长255字符")
    private String medicineBrand;

    @NotNull(message = "药品类型不能为空")
    private Integer medicineType;

    @DecimalMin(value = "0", message = "价格不能为负")
    private BigDecimal medicinePrice;

    @Size(max = 5000, message = "功效内容过长")
    private String medicineEffect;

    @Size(max = 5000, message = "相互作用内容过长")
    private String interaction;

    @Size(max = 5000, message = "禁忌内容过长")
    private String taboo;

    @Size(max = 5000, message = "用法用量过长")
    private String usAge;

    @Size(max = 255, message = "图片路径最长255字符")
    private String imgPath;
}
