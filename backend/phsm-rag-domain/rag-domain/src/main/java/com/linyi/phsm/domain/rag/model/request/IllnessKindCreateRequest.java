package com.linyi.phsm.domain.rag.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class IllnessKindCreateRequest {

    @NotBlank(message = "分类名称不能为空")
    @Size(max = 255, message = "分类名称最长255字符")
    private String name;

    @Size(max = 255, message = "描述最长255字符")
    private String info;
}
