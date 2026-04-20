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
public class FeedbackVO {

    private String id;
    private String name;
    private String email;
    private String title;
    private String content;
    private String createdBy;
    private Date createTime;
    private Date updateTime;
}
