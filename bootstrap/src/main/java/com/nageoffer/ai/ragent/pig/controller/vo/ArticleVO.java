package com.nageoffer.ai.ragent.pig.controller.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 文章信息VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleVO {

    private Long id;
    private String title;
    private String category;
    private String categoryName;
    private Long authorId;
    private String authorName;
    private String summary;
    private String content;
    private String coverImage;
    private String tags;
    private Integer viewCount;
    private Integer likeCount;
    private String status;
    private String statusName;
    private Date publishTime;
    private Date createTime;
    private Date updateTime;
}
