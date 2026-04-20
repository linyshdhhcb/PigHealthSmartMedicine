package com.linyi.phsm.domain.rag.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class HistoryCreateRequest {

    @NotBlank(message = "关键字不能为空")
    private String keyword;

    /**
     * 1搜索，2科目，3药品
     */
    @NotNull(message = "操作类型不能为空")
    private Integer operateType;
}
