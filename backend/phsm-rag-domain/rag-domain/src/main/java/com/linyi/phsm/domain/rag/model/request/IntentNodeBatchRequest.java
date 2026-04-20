package com.linyi.phsm.domain.rag.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntentNodeBatchRequest {

    /**
     * 节点 ID 列表
     */
    private List<String> ids;
}
