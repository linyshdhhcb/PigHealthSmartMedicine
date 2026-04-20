package com.linyi.phsm.domain.rag.model.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class IllnessMedicineBatchDeleteRequest {

    @NotEmpty(message = "关联ID列表不能为空")
    private List<String> ids;
}
