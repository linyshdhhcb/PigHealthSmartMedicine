package com.linyi.phsm.domain.rag.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IllnessMedicineVO {

    private String id;
    private String illnessId;
    private String illnessName;
    private String medicineId;
    private String medicineName;
    private String createdBy;
    private Date createTime;
}
