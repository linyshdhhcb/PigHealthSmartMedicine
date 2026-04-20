package com.linyi.phsm.domain.rag.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class IllnessUpdateRequest {

    @NotBlank(message = "疾病分类不能为空")
    private String kindId;

    @NotBlank(message = "疾病名称不能为空")
    @Size(max = 100, message = "疾病名称最长100字符")
    private String illnessName;

    @Size(max = 2000, message = "诱发因素过长")
    private String includeReason;

    @Size(max = 5000, message = "疾病症状过长")
    private String illnessSymptom;

    @Size(max = 5000, message = "特殊症状过长")
    private String specialSymptom;
}
