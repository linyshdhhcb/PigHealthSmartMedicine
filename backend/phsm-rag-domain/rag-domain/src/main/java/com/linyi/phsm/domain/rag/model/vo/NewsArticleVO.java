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
public class NewsArticleVO {

    private String id;
    private String url;
    private String title;
    private String content;
    private String author;
    private Date publishTime;
    private String source;
    private String summary;
    private String createdBy;
    private String updatedBy;
    private Date createTime;
    private Date updateTime;
}
