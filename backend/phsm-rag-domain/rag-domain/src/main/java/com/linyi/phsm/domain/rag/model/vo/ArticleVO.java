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
public class ArticleVO {

    private String id;
    private String title;
    private String content;
    private String author;
    private String typeId;
    private String typeName;
    private String createdBy;
    private String updatedBy;
    private Date createTime;
    private Date updateTime;
}
