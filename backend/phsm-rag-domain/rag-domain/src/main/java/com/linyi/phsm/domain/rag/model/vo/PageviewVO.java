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
public class PageviewVO {

    private String id;
    private Integer pageviews;
    private String illnessId;
    private String illnessName;
    private Date updateTime;
}
