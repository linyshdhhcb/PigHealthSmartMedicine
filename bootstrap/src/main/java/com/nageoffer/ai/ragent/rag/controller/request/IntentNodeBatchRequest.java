

package com.nageoffer.ai.ragent.rag.controller.request;

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
    private List<Long> ids;
}
