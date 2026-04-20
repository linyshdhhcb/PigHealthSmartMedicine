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
public class IllnessVO {

    private String id;
    private String kindId;
    private String kindName;
    private String illnessName;
    private String includeReason;
    private String illnessSymptom;
    private String specialSymptom;
    private String createdBy;
    private String updatedBy;
    private Date createTime;
    private Date updateTime;
}
