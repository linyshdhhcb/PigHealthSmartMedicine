

package com.nageoffer.ai.ragent.ingestion.controller.vo;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public class IngestionPipelineNodeVO {

    private Long id;

    private String nodeId;

    private String nodeType;

    private JsonNode settings;

    private JsonNode condition;

    private String nextNodeId;
}
