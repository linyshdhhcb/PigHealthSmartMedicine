package com.linyi.phsm.domain.rag.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicineVO {

    private String id;
    private String medicineName;
    private String keyword;
    private String medicineEffect;
    private String medicineBrand;
    private String interaction;
    private String taboo;
    private String usAge;
    private Integer medicineType;
    private String imgPath;
    private BigDecimal medicinePrice;
    private String createdBy;
    private String updatedBy;
    private Date createTime;
    private Date updateTime;
}
