package com.linyi.phsm.domain.knowledge.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class ChunkStrategyVO {

    private String value;

    private String label;

    private Map<String, Integer> defaultConfig;
}
