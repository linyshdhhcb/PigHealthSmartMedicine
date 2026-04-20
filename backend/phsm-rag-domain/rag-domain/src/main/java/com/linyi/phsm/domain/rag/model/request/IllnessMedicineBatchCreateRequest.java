package com.linyi.phsm.domain.rag.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class IllnessMedicineBatchCreateRequest {

    @NotBlank(message = "疾病不能为空")
    private String illnessId;

    @NotEmpty(message = "药品列表不能为空")
    private List<String> medicineIds;
}
