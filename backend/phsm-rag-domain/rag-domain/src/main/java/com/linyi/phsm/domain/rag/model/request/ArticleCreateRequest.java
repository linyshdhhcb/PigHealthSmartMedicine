package com.linyi.phsm.domain.rag.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ArticleCreateRequest {

    @NotBlank(message = "标题不能为空")
    @Size(max = 255, message = "标题最长255字符")
    private String title;

    @NotBlank(message = "内容不能为空")
    private String content;

    @Size(max = 100, message = "作者最长100字符")
    private String author;

    @NotBlank(message = "文章类型不能为空")
    private String typeId;
}
