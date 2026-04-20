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
public class IllnessKindVO {

    private String id;
    private String name;
    private String info;
    private String createdBy;
    private String updatedBy;
    private Date createTime;
    private Date updateTime;
}
