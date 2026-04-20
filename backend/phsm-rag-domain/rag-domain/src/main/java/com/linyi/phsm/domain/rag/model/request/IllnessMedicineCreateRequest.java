package com.linyi.phsm.domain.rag.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class IllnessMedicineCreateRequest {

    @NotBlank(message = "疾病不能为空")
    private String illnessId;

    @NotBlank(message = "药品不能为空")
    private String medicineId;
}
