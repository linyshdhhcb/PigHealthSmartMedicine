package com.linyi.phsm.domain.rag.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ArticleTypeUpdateRequest {

    @NotBlank(message = "文章类型名称不能为空")
    @Size(max = 20, message = "文章类型名称最长20字符")
    private String typeName;
}
