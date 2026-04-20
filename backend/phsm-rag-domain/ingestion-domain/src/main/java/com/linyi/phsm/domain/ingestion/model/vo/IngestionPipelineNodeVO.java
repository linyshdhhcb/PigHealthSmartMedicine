package com.linyi.phsm.domain.ingestion.model.vo;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public class IngestionPipelineNodeVO {

    private String id;

    private String nodeId;

    private String nodeType;

    private JsonNode settings;

    private JsonNode condition;

    private String nextNodeId;
}
