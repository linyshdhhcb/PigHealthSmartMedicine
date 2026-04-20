package com.linyi.phsm.domain.rag.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class NewsArticleCreateRequest {

    @Size(max = 500, message = "转载URL最长500字符")
    private String url;

    @NotBlank(message = "标题不能为空")
    @Size(max = 255, message = "标题最长255字符")
    private String title;

    @NotBlank(message = "内容不能为空")
    private String content;

    @Size(max = 100, message = "作者最长100字符")
    private String author;

    private Date publishTime;

    @Size(max = 255, message = "来源最长255字符")
    private String source;

    @Size(max = 500, message = "摘要最长500字符")
    private String summary;
}
